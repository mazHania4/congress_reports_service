package ayd2.ps2026.congress.common.utils;

import ayd2.ps2026.congress.common.config.files.FilesStorageConf;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;


@Service
public class FilesService {

    public static final String EDITIONS_FOLDER = "editions";
    public static final String PROFILE_PHOTOS_FOLDER = "profiles";
    public static final String ADS_FOLDER = "ads";

    private final FilesStorageConf FilesStorageConf;
    private final S3Client s3Client;

    public FilesService(FilesStorageConf FilesStorageConf) {
        this.FilesStorageConf = FilesStorageConf;
        this.s3Client = S3Client.builder()
                .region(Region.of(FilesStorageConf.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(FilesStorageConf.getAccessKey(), FilesStorageConf.getSecretKey())
                        )
                )
                .build();
    }

    /**
     * Sube un archivo al servidor de G3 y devuelve el path
     * */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        String extension = "";
        String originalName = file.getOriginalFilename();
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        // Generar nombre único
        String key = folder + "/" + UUID.randomUUID() + extension; // <-- aquí agregamos el folder

        // Subir a S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(FilesStorageConf.getBucketName())
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        // URL pública
        return "https://" + FilesStorageConf.getBucketName() + ".s3.amazonaws.com/" + key;
    }

}



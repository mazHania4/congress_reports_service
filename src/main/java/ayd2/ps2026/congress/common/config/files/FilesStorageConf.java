package ayd2.ps2026.congress.common.config.files;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuraciones para subir archivos de S3 de AWS
 * @author Yennifer de Leon
 * @since 2025-09-10
 * */
@Getter
@Setter
@Configuration
public class FilesStorageConf {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    String region;

    @Value("${aws.accessKey}")
    String accessKey;

    @Value("${aws.secretKey}")
    String secretKey;

}

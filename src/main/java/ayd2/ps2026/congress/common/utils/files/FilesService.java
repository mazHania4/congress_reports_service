package ayd2.ps2026.congress.common.utils.files;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FilesService {

    String uploadFile(MultipartFile file, String folder) throws IOException;

}

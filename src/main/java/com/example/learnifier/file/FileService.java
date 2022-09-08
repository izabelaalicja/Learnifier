package com.example.learnifier.file;

import com.example.learnifier.metadata.FileMetadataResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {

    FileResponse upload(MultipartFile file);

    Resource getFileResource(File file);

    void update(UUID fileId, MultipartFile file);

    void delete(UUID fileId);

    FileMetadataResponse getFileMetadata(UUID fileId);

    File getFile(UUID fileId);

}

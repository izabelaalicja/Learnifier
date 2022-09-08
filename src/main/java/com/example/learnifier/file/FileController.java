package com.example.learnifier.file;

import com.example.learnifier.metadata.FileMetadata;
import com.example.learnifier.metadata.FileMetadataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<FileResponse> upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok()
                .body(fileService.upload(file));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> read(@PathVariable("fileId") UUID fileId) {
        File file = fileService.getFile(fileId);
        Resource fileResource = fileService.getFileResource(file);
        FileMetadata fileMetadata = file.getMetadata();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        fileResource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, fileMetadata.getFileFormat().getMimeType())
                .body(fileResource);
    }

    @PutMapping("/{fileId}")
    public void update(@PathVariable("fileId") UUID fileId, @RequestBody MultipartFile file) {
        fileService.update(fileId, file);
    }

    @DeleteMapping("/{fileId}")
    public void delete(@PathVariable("fileId") UUID fileId) {
        fileService.delete(fileId);
    }

    @GetMapping("/{fileId}/metadata")
    public ResponseEntity<FileMetadataResponse> getFileMetadata(@PathVariable("fileId") UUID fileId) {
        return ResponseEntity.ok()
                .body(fileService.getFileMetadata(fileId));
    }

}

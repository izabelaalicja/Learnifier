package com.example.learnifier.file;

import com.example.learnifier.file.exception.DeleteFileException;
import com.example.learnifier.file.exception.DifferentFileTypeException;
import com.example.learnifier.file.exception.FileNotFoundException;
import com.example.learnifier.file.exception.MalformedFileURLException;
import com.example.learnifier.file.exception.StoreFileException;
import com.example.learnifier.metadata.FileFormat;
import com.example.learnifier.metadata.FileMetadata;
import com.example.learnifier.metadata.FileMetadataMapper;
import com.example.learnifier.metadata.FileMetadataRepository;
import com.example.learnifier.metadata.FileMetadataResponse;
import com.example.learnifier.metadata.UnsupportedFileFormatException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMetadataRepository metadataRepository;
    private final FileMetadataMapper fileMetadataMapper;
    private final FileMapper fileMapper;

    @Setter
    @Value("${storage}")
    private Path rootLocation;


    @Override
    public FileResponse upload(MultipartFile file) {
        String fileExtension = getFileExtension(file);
        Optional<FileFormat> fileFormatOptional = FileFormat.getFromExtension(fileExtension);
        if (fileFormatOptional.isPresent()) {
            UUID generatedFileName = UUID.randomUUID();
            Path targetLocation = rootLocation.resolve(generatedFileName + "." + getFileExtension(file));
            try {
                Files.copy(file.getInputStream(), targetLocation);
            } catch (IOException e) {
                throw new StoreFileException();
            }
            return getSavedFileResponse(file, fileFormatOptional.get(), targetLocation.toString());
        } else {
            throw new UnsupportedFileFormatException();
        }
    }

    @Override
    public Resource getFileResource(File file) {
        try {
            Path filePath = Path.of(file.getUrlLocation());
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new MalformedFileURLException();
        }
    }

    @Override
    public void update(UUID fileId, MultipartFile file) {
        File originalFile = getFile(fileId);
        String originalExtension = originalFile.getMetadata().getFileFormat().getExtension();
        if (getFileExtension(file).equals(originalExtension)) {
            try {
                Files.write(Path.of(originalFile.getUrlLocation()), file.getBytes());
            } catch (IOException e) {
                throw new StoreFileException();
            }
            FileMetadata fileMetadata = originalFile.getMetadata();
            fileMetadata.setSize(file.getSize());
            metadataRepository.save(fileMetadata);
        } else {
            throw new DifferentFileTypeException();
        }
    }

    @Override
    public void delete(UUID fileId) {
        File file = getFile(fileId);
        try {
            Files.delete(Path.of(file.getUrlLocation()));
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new DeleteFileException();
        }
    }

    @Override
    public FileMetadataResponse getFileMetadata(UUID fileId) {
        File file = getFile(fileId);
        return fileMetadataMapper.mapToResponse(file.getMetadata());
    }

    @Override
    public File getFile(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);
    }

    private FileResponse getSavedFileResponse(MultipartFile file, FileFormat fileFormat, String urlLocation) {
        FileMetadata metadataEntity = buildFileMetadata(file, fileFormat);
        File fileEntity = buildFileEntity(metadataEntity, urlLocation);

        metadataRepository.save(metadataEntity);
        return fileMapper.mapToResponse(fileRepository.save(fileEntity));
    }

    private FileMetadata buildFileMetadata(MultipartFile file, FileFormat fileFormat) {
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());

        return FileMetadata.builder()
                .size(file.getSize())
                .originalName(originalFileName)
                .fileFormat(fileFormat)
                .build();
    }

    private String getFileExtension(MultipartFile file) {
        return FilenameUtils.getExtension(Objects.requireNonNull(file.getOriginalFilename()));
    }

    private File buildFileEntity(FileMetadata metadataEntity, String urlLocation) {
        return File.builder()
                .urlLocation(urlLocation)
                .metadata(metadataEntity)
                .build();
    }

}

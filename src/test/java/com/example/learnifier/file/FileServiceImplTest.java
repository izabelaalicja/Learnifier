package com.example.learnifier.file;

import com.example.learnifier.metadata.FileFormat;
import com.example.learnifier.metadata.FileMetadata;
import com.example.learnifier.metadata.FileMetadataMapper;
import com.example.learnifier.metadata.FileMetadataRepository;
import com.example.learnifier.metadata.FileMetadataResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileMetadataRepository metadataRepository;

    @Mock
    private FileMetadataMapper fileMetadataMapper;

    @Mock
    private FileMapper fileMapper;

    @InjectMocks
    private FileServiceImpl fileService;

    private final UUID id = UUID.fromString("405786b2-043e-4488-9512-57352ea2eb0e");

    private final String testRootLocation = "test-uploads";

    @BeforeEach
    void init() {
        fileService.setRootLocation(Path.of(testRootLocation));
        try {
            Files.createDirectories(Path.of(testRootLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Given multipart file when upload file, then return file response.")
    void givenMultipartFile_whenUpload_thenReturnFileResponse() {
        //given
        MockMultipartFile file = new MockMultipartFile("file.txt", "originalFile.txt",
                MediaType.TEXT_PLAIN_VALUE, "Some text here...".getBytes());

        File fileEntity = buildFile(id, buildFileMetadata());
        FileResponse expectedFileResponse = new FileResponse(id, "originalFile.txt", FileFormat.TXT);
        Mockito.when(fileRepository.save(Mockito.any(File.class))).thenReturn(fileEntity);
        Mockito.when(fileMapper.mapToResponse(fileEntity)).thenReturn(expectedFileResponse);

        //when
        var actualFileResponse = fileService.upload(file);

        //then
        Assertions.assertThat(actualFileResponse.getId()).isEqualTo(expectedFileResponse.getId());
        Assertions.assertThat(actualFileResponse.getOriginalName()).isEqualTo(expectedFileResponse.getOriginalName());
        Assertions.assertThat(actualFileResponse.getFileFormat()).isEqualTo(expectedFileResponse.getFileFormat());
    }

    @Test
    @DisplayName("Given file id when get file metadata, then return file metadata response.")
    void givenId_whenGetFileMetadata_thenReturnFileMetadataResponse() {
        //given
        FileMetadata metadata = buildFileMetadata();
        File file = buildFile(id, metadata);
        FileMetadataResponse expectedFileMetadataResponse = buildFileMetadataResponse(metadata);
        Mockito.when(fileRepository.findById(id)).thenReturn(Optional.ofNullable(file));
        Mockito.when(fileMetadataMapper.mapToResponse(Objects.requireNonNull(file).getMetadata()))
                .thenReturn(expectedFileMetadataResponse);

        //when
        FileMetadataResponse actualMetadataResponse = fileService.getFileMetadata(id);

        //then
        Assertions.assertThat(actualMetadataResponse.getOriginalName()).isEqualTo(expectedFileMetadataResponse.getOriginalName());
        Assertions.assertThat(actualMetadataResponse.getExtension()).isEqualTo(expectedFileMetadataResponse.getExtension());
        Assertions.assertThat(actualMetadataResponse.getSize()).isEqualTo(expectedFileMetadataResponse.getSize());
    }

    @AfterEach
    void cleanUp() {
        FileSystemUtils.deleteRecursively(Path.of(testRootLocation).toFile());
    }

    private FileMetadata buildFileMetadata() {
        return FileMetadata.builder()
                .size(333)
                .originalName("originalFile")
                .fileFormat(FileFormat.TXT)
                .build();
    }

    private File buildFile(UUID fileId, FileMetadata metadata) {
        return File.builder()
                .id(fileId)
                .urlLocation("uploads/file.txt")
                .metadata(metadata)
                .build();
    }

    private FileMetadataResponse buildFileMetadataResponse(FileMetadata metadata) {
        return FileMetadataResponse.builder()
                .extension(metadata.getFileFormat().getExtension())
                .originalName(metadata.getOriginalName())
                .size(metadata.getSize())
                .build();
    }

}
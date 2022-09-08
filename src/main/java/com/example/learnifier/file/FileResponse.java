package com.example.learnifier.file;

import com.example.learnifier.metadata.FileFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class FileResponse {
    private UUID id;
    private String originalName;
    private FileFormat fileFormat;
}

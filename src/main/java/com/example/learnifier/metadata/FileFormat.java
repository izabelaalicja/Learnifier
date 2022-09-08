package com.example.learnifier.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum FileFormat {

    PNG("image/png", "png"),
    TXT("text/plain", "txt"),
    CSV("text/csv", "csv"),;

    private final String mimeType;
    private final String extension;

    public static Optional<FileFormat> getFromExtension(String fileExtension) {
        return Arrays.stream(FileFormat.values())
                .filter(format -> format.getExtension().equals(fileExtension))
                .findAny();
    }
}

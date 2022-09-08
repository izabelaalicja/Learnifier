package com.example.learnifier.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "file_metadata")
public class FileMetadata {

    @Id
    @GeneratedValue
    private UUID id;

    private long size;

    private String originalName;

    private FileFormat fileFormat;

}

package com.example.learnifier.file;

import com.example.learnifier.metadata.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {

    @Id
    @GeneratedValue
    private UUID id;

    private String urlLocation;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private FileMetadata metadata;

}

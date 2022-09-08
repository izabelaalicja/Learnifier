package com.example.learnifier.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMetadataMapper {

    @Mapping(target = "extension", source = "entity.fileFormat.extension")
    FileMetadataResponse mapToResponse(FileMetadata entity);

}

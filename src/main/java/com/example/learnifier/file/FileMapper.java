package com.example.learnifier.file;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(target = "originalName", source = "entity.metadata.originalName")
    @Mapping(target = "fileFormat", source = "entity.metadata.fileFormat")
    FileResponse mapToResponse(File entity);
}

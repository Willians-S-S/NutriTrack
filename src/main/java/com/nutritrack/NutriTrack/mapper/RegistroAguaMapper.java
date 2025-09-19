package com.nutritrack.mapper;

import com.nutritrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.dto.RegistroAguaResponseDTO;
import com.nutritrack.entity.RegistroAgua;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroAguaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "observadoEm", ignore = true)
    RegistroAgua toEntity(RegistroAguaRequestDTO requestDTO);

    @Mapping(source = "usuario.id", target = "usuarioId")
    RegistroAguaResponseDTO toResponseDTO(RegistroAgua registroAgua);
}

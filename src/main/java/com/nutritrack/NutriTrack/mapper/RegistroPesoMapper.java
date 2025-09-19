package com.nutritrack.mapper;

import com.nutritrack.dto.RegistroPesoRequestDTO;
import com.nutritrack.dto.RegistroPesoResponseDTO;
import com.nutritrack.entity.RegistroPeso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroPesoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "observadoEm", ignore = true)
    RegistroPeso toEntity(RegistroPesoRequestDTO requestDTO);

    @Mapping(source = "usuario.id", target = "usuarioId")
    RegistroPesoResponseDTO toResponseDTO(RegistroPeso registroPeso);
}

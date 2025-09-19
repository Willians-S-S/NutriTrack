package com.nutritrack.mapper;

import com.nutritrack.dto.AlimentoRequestDTO;
import com.nutritrack.dto.AlimentoResponseDTO;
import com.nutritrack.entity.Alimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlimentoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Alimento toEntity(AlimentoRequestDTO alimentoRequestDTO);

    AlimentoResponseDTO toResponseDTO(Alimento alimento);
}

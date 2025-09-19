package com.nutritrack.mapper;

import com.nutritrack.dto.UserRequestDTO;
import com.nutritrack.dto.UserResponseDTO;
import com.nutritrack.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "senhaHash", ignore = true), // Será definido no service
        @Mapping(target = "role", ignore = true),      // Será definido no service
        @Mapping(target = "criadoEm", ignore = true),
        @Mapping(target = "atualizadoEm", ignore = true),
        @Mapping(target = "refeicoes", ignore = true),
        @Mapping(target = "registrosPeso", ignore = true),
        @Mapping(target = "registrosAgua", ignore = true)
    })
    Usuario toEntity(UserRequestDTO userRequestDTO);

    UserResponseDTO toResponseDTO(Usuario usuario);
}

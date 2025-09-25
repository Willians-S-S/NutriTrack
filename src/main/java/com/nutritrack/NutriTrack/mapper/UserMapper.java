package com.nutritrack.mapper;

import com.nutritrack.dto.UserRequestDTO;
import com.nutritrack.dto.UserResponseDTO;
import com.nutritrack.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper para conversão entre a entidade {@link Usuario} e seus DTOs
 * {@link UserRequestDTO} e {@link UserResponseDTO}.
 * 
 * Utiliza MapStruct para gerar automaticamente a implementação.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte um {@link UserRequestDTO} em {@link Usuario}.
     * 
     * Campos gerenciados pelo serviço ou pelo banco de dados são ignorados,
     * como {@code id}, {@code senhaHash}, {@code role}, {@code criadoEm}, {@code atualizadoEm}
     * e listas de registros do usuário.
     *
     * @param userRequestDTO DTO de requisição com os dados do usuário
     * @return entidade Usuario pronta para persistência
     */
    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "senhaHash", ignore = true),
        @Mapping(target = "role", ignore = true),
        @Mapping(target = "criadoEm", ignore = true),
        @Mapping(target = "atualizadoEm", ignore = true),
        @Mapping(target = "refeicoes", ignore = true),
        @Mapping(target = "registrosPeso", ignore = true),
        @Mapping(target = "registrosAgua", ignore = true)
    })
    Usuario toEntity(UserRequestDTO userRequestDTO);

    /**
     * Converte um {@link Usuario} em {@link UserResponseDTO}.
     *
     * @param usuario entidade Usuario
     * @return DTO de resposta com os dados do usuário
     */
    UserResponseDTO toResponseDTO(Usuario usuario);
}

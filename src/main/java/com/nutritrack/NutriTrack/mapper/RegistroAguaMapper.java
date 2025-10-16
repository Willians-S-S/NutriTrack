package com.nutritrack.NutriTrack.mapper;

import com.nutritrack.NutriTrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.NutriTrack.dto.RegistroAguaResponseDTO;
import com.nutritrack.NutriTrack.entity.RegistroAgua;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre a entidade {@link RegistroAgua} e seus DTOs
 * {@link RegistroAguaRequestDTO} e {@link RegistroAguaResponseDTO}.
 * 
 * Utiliza MapStruct para gerar automaticamente a implementação.
 */
@Mapper(componentModel = "spring")
public interface RegistroAguaMapper {

    /**
     * Converte um {@link RegistroAguaRequestDTO} em {@link RegistroAgua}.
     * 
     * Campos {@code id}, {@code usuario} e {@code observadoEm} são ignorados,
     * pois serão gerenciados pelo serviço ou pelo banco de dados.
     *
     * @param requestDTO DTO de requisição contendo os dados do registro de água
     * @return entidade RegistroAgua pronta para persistência
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "observadoEm", ignore = true)
    RegistroAgua toEntity(RegistroAguaRequestDTO requestDTO);

    /**
     * Converte um {@link RegistroAgua} em {@link RegistroAguaResponseDTO}.
     * 
     * O campo {@code usuarioId} é extraído da entidade {@code usuario}.
     *
     * @param registroAgua entidade RegistroAgua
     * @return DTO de resposta com os dados do registro de água
     */
    @Mapping(source = "usuario.id", target = "usuarioId")
    RegistroAguaResponseDTO toResponseDTO(RegistroAgua registroAgua);
}

package com.nutritrack.mapper;

import com.nutritrack.dto.AlimentoRequestDTO;
import com.nutritrack.dto.AlimentoResponseDTO;
import com.nutritrack.entity.Alimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para converter entre a entidade {@link Alimento} e seus DTOs {@link AlimentoRequestDTO}
 * e {@link AlimentoResponseDTO}.
 * 
 * Utiliza MapStruct para gerar automaticamente as implementações.
 */
@Mapper(componentModel = "spring")
public interface AlimentoMapper {

    /**
     * Converte um {@link AlimentoRequestDTO} em uma entidade {@link Alimento}.
     * 
     * Campos {@code id} e {@code criadoEm} são ignorados, pois são gerenciados pelo banco.
     *
     * @param alimentoRequestDTO DTO de requisição contendo os dados do alimento
     * @return entidade Alimento pronta para persistência
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Alimento toEntity(AlimentoRequestDTO alimentoRequestDTO);

    /**
     * Converte uma entidade {@link Alimento} em um {@link AlimentoResponseDTO}.
     *
     * @param alimento entidade de alimento
     * @return DTO de resposta com os dados do alimento
     */
    AlimentoResponseDTO toResponseDTO(Alimento alimento);
}

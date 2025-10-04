package com.nutritrack.NutriTrack.mapper;

import com.nutritrack.NutriTrack.dto.*;
import com.nutritrack.NutriTrack.entity.Alimento;
import com.nutritrack.NutriTrack.entity.ItemRefeicao;
import com.nutritrack.NutriTrack.entity.Refeicao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre entidades de refeição ({@link Refeicao}, {@link ItemRefeicao}, {@link Alimento})
 * e seus respectivos DTOs ({@link RefeicaoRequestDTO}, {@link RefeicaoResponseDTO},
 * {@link ItemRefeicaoRequestDTO}, {@link ItemRefeicaoResponseDTO}, {@link AlimentoResumidoDTO}).
 * 
 * Utiliza MapStruct para gerar automaticamente as implementações.
 */
@Mapper(componentModel = "spring")
public interface RefeicaoMapper {

    /**
     * Converte um {@link ItemRefeicao} em {@link ItemRefeicaoRequestDTO}.
     *
     * @param item entidade de item de refeição
     * @return DTO de requisição com os dados do item
     */
    @Mapping(source = "alimento.id", target = "alimentoId")
    ItemRefeicaoRequestDTO itemToRequestDTO(ItemRefeicao item);

    /**
     * Converte um {@link ItemRefeicaoRequestDTO} em {@link ItemRefeicao}.
     *
     * Campos {@code id} e {@code refeicao} são ignorados, pois serão gerenciados pelo serviço.
     *
     * @param itemRequestDTO DTO de requisição do item
     * @return entidade ItemRefeicao pronta para persistência
     */
    @Mapping(source = "alimentoId", target = "alimento.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refeicao", ignore = true)
    ItemRefeicao itemRequestToEntity(ItemRefeicaoRequestDTO itemRequestDTO);

    /**
     * Converte um {@link Alimento} em {@link AlimentoResumidoDTO}.
     *
     * @param alimento entidade de alimento
     * @return DTO resumido contendo apenas ID e nome
     */
    AlimentoResumidoDTO alimentoToResumidoDTO(Alimento alimento);

    /**
     * Converte um {@link ItemRefeicao} em {@link ItemRefeicaoResponseDTO}.
     *
     * @param item entidade de item de refeição
     * @return DTO de resposta com dados do item
     */
    @Mapping(source = "alimento", target = "alimento")
    ItemRefeicaoResponseDTO itemToResponseDTO(ItemRefeicao item);

    /**
     * Converte um {@link RefeicaoRequestDTO} em {@link Refeicao}.
     *
     * Campos {@code id}, {@code usuario} e {@code criadoEm} são ignorados,
     * pois serão gerenciados pelo serviço ou banco de dados.
     *
     * @param requestDTO DTO de requisição da refeição
     * @return entidade Refeicao pronta para persistência
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Refeicao toEntity(RefeicaoRequestDTO requestDTO);

    /**
     * Converte um {@link Refeicao} em {@link RefeicaoResponseDTO}.
     *
     * Campos de totais nutricionais são ignorados, pois serão calculados no serviço.
     *
     * @param refeicao entidade de refeição
     * @return DTO de resposta com dados da refeição
     */
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(target = "totalCalorias", ignore = true)
    @Mapping(target = "totalProteinasG", ignore = true)
    @Mapping(target = "totalCarboidratosG", ignore = true)
    @Mapping(target = "totalGordurasG", ignore = true)
    RefeicaoResponseDTO toResponseDTO(Refeicao refeicao);
}

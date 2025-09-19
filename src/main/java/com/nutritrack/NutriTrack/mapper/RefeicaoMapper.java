package com.nutritrack.mapper;

import com.nutritrack.dto.*;
import com.nutritrack.entity.Alimento;
import com.nutritrack.entity.ItemRefeicao;
import com.nutritrack.entity.Refeicao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefeicaoMapper {

    @Mapping(source = "alimento.id", target = "alimentoId")
    ItemRefeicaoRequestDTO itemToRequestDTO(ItemRefeicao item);

    @Mapping(source = "alimentoId", target = "alimento.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refeicao", ignore = true)
    ItemRefeicao itemRequestToEntity(ItemRefeicaoRequestDTO itemRequestDTO);

    AlimentoResumidoDTO alimentoToResumidoDTO(Alimento alimento);

    @Mapping(source = "alimento", target = "alimento")
    ItemRefeicaoResponseDTO itemToResponseDTO(ItemRefeicao item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Refeicao toEntity(RefeicaoRequestDTO requestDTO);

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(target = "totalCalorias", ignore = true) // Será calculado no serviço
    @Mapping(target = "totalProteinasG", ignore = true)
    @Mapping(target = "totalCarboidratosG", ignore = true)
    @Mapping(target = "totalGordurasG", ignore = true)
    RefeicaoResponseDTO toResponseDTO(Refeicao refeicao);
}

package com.nutritrack.NutriTrack.mapper;

import com.nutritrack.NutriTrack.dto.MetaRequestDTO;
import com.nutritrack.NutriTrack.dto.MetaResponseDTO;
import com.nutritrack.NutriTrack.entity.Meta;
import org.springframework.stereotype.Component;

@Component
public class MetaMapper {

    public Meta toEntity(MetaRequestDTO dto) {
        Meta meta = new Meta();
        meta.setTipo(dto.tipo());
        meta.setCaloriasObjetivo(dto.caloriasObjetivo());
        meta.setProteinasObjetivo(dto.proteinasObjetivo());
        meta.setCarboidratosObjetivo(dto.carboidratosObjetivo());
        meta.setGordurasObjetivo(dto.gordurasObjetivo());
        meta.setDataInicio(dto.dataInicio());
        meta.setDataFim(dto.dataFim());
        return meta;
    }

    public MetaResponseDTO toResponseDTO(Meta meta) {
        return new MetaResponseDTO(
                meta.getId(),
                meta.getUsuario().getId(),
                meta.getTipo(),
                meta.getCaloriasObjetivo(),
                meta.getProteinasObjetivo(),
                meta.getCarboidratosObjetivo(),
                meta.getGordurasObjetivo(),
                meta.getDataInicio(),
                meta.getDataFim(),
                meta.getCriadoEm(),
                meta.getAtualizadoEm()
        );
    }
}

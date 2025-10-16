package com.nutritrack.NutriTrack.mapper;

import com.nutritrack.NutriTrack.dto.RegistroPesoRequestDTO;
import com.nutritrack.NutriTrack.dto.RegistroPesoResponseDTO;
import com.nutritrack.NutriTrack.entity.RegistroPeso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para conversão entre a entidade {@link RegistroPeso} e seus DTOs
 * {@link RegistroPesoRequestDTO} e {@link RegistroPesoResponseDTO}.
 * 
 * Utiliza MapStruct para gerar automaticamente a implementação.
 */
@Mapper(componentModel = "spring")
public interface RegistroPesoMapper {

    /**
     * Converte um {@link RegistroPesoRequestDTO} em {@link RegistroPeso}.
     * 
     * Campos {@code id}, {@code usuario} e {@code observadoEm} são ignorados,
     * pois serão gerenciados pelo serviço ou pelo banco de dados.
     *
     * @param requestDTO DTO de requisição contendo os dados do registro de peso
     * @return entidade RegistroPeso pronta para persistência
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "observadoEm", ignore = true)
    RegistroPeso toEntity(RegistroPesoRequestDTO requestDTO);

    /**
     * Converte um {@link RegistroPeso} em {@link RegistroPesoResponseDTO}.
     * 
     * O campo {@code usuarioId} é extraído da entidade {@code usuario}.
     *
     * @param registroPeso entidade RegistroPeso
     * @return DTO de resposta com os dados do registro de peso
     */
    @Mapping(source = "usuario.id", target = "usuarioId")
    RegistroPesoResponseDTO toResponseDTO(RegistroPeso registroPeso);
}

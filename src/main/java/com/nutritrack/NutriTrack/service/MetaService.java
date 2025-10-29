package com.nutritrack.NutriTrack.service;

import com.nutritrack.NutriTrack.dto.MetaProgressoDTO;
import com.nutritrack.NutriTrack.dto.MetaRequestDTO;
import com.nutritrack.NutriTrack.dto.MetaResponseDTO;
import com.nutritrack.NutriTrack.entity.ItemRefeicao;
import com.nutritrack.NutriTrack.entity.Meta;
import com.nutritrack.NutriTrack.entity.Refeicao;
import com.nutritrack.NutriTrack.entity.Usuario;
import com.nutritrack.NutriTrack.enums.TipoMeta;
import com.nutritrack.NutriTrack.exception.ConflictException;
import com.nutritrack.NutriTrack.mapper.MetaMapper;
import com.nutritrack.NutriTrack.repository.MetaRepository;
import com.nutritrack.NutriTrack.repository.RefeicaoRepository;
import com.nutritrack.NutriTrack.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciar a lógica de negócios relacionada às metas nutricionais.
 */
@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RefeicaoRepository refeicaoRepository;
    private final MetaMapper metaMapper;

    /**
     * Cria uma nova meta diária para um usuário, e automaticamente gera as metas semanais e mensais correspondentes.
     *
     * @param usuarioId O ID do usuário.
     * @param metaRequestDTO DTO com os dados da meta diária.
     * @return Uma lista de DTOs representando as metas diária, semanal e mensal criadas.
     * @throws EntityNotFoundException se o usuário não for encontrado.
     * @throws ConflictException se já existir uma meta do mesmo tipo para o usuário.
     */
    public List<MetaResponseDTO> create(UUID usuarioId, MetaRequestDTO metaRequestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + usuarioId));

        if (metaRequestDTO.tipo() == TipoMeta.DIARIA) {
            Optional<Meta> existingMetaDiaria = metaRepository.findByUsuarioIdAndTipo(usuarioId, TipoMeta.DIARIA).stream().findFirst();

            if (existingMetaDiaria.isPresent()) {
                // Update existing goals
                Meta metaDiaria = existingMetaDiaria.get();
                metaDiaria.setCaloriasObjetivo(metaRequestDTO.caloriasObjetivo());
                metaDiaria.setProteinasObjetivo(metaRequestDTO.proteinasObjetivo());
                metaDiaria.setCarboidratosObjetivo(metaRequestDTO.carboidratosObjetivo());
                metaDiaria.setGordurasObjetivo(metaRequestDTO.gordurasObjetivo());

                Meta metaSemanal = metaRepository.findByUsuarioIdAndTipo(usuarioId, TipoMeta.SEMANAL).stream().findFirst()
                        .orElseGet(() -> cloneMeta(metaDiaria, TipoMeta.SEMANAL, BigDecimal.valueOf(7)));
                metaMultiply(metaSemanal, metaDiaria, BigDecimal.valueOf(7));

                Meta metaMensal = metaRepository.findByUsuarioIdAndTipo(usuarioId, TipoMeta.MENSAL).stream().findFirst()
                        .orElseGet(() -> cloneMeta(metaDiaria, TipoMeta.MENSAL, BigDecimal.valueOf(30)));
                metaMultiply(metaMensal, metaDiaria, BigDecimal.valueOf(30));

                List<Meta> updatedMetas = metaRepository.saveAll(List.of(metaDiaria, metaSemanal, metaMensal));
                return updatedMetas.stream().map(metaMapper::toResponseDTO).toList();
            }
        }
        
        // Original create logic for when no daily goal exists
        Meta meta = metaMapper.toEntity(metaRequestDTO);
        meta.setUsuario(usuario);

        Meta metaSemanal = cloneMeta(meta, TipoMeta.SEMANAL, BigDecimal.valueOf(7));
        Meta metaMensal = cloneMeta(meta, TipoMeta.MENSAL, BigDecimal.valueOf(30));

        List<Meta> savedMeta = metaRepository.saveAll(List.of(meta, metaSemanal, metaMensal));

        return savedMeta.stream().map(metaMapper::toResponseDTO).toList();
    }

    /**
     * Clona uma meta base, multiplicando seus valores nutricionais por um fator.
     *
     * @param base A meta base a ser clonada.
     * @param tipo O novo tipo da meta.
     * @param fator O fator de multiplicação.
     * @return A nova meta clonada e modificada.
     */
    private Meta cloneMeta(Meta base, TipoMeta tipo, BigDecimal fator) {
        Meta meta = new Meta();
        meta.setUsuario(base.getUsuario());
        meta.setTipo(tipo);
        meta.setCaloriasObjetivo(base.getCaloriasObjetivo().multiply(fator));
        meta.setCarboidratosObjetivo(base.getCarboidratosObjetivo().multiply(fator));
        meta.setProteinasObjetivo(base.getProteinasObjetivo().multiply(fator));
        meta.setGordurasObjetivo(base.getGordurasObjetivo().multiply(fator));
        return meta;
    }

    /**
     * Busca todas as metas de um usuário para um tipo específico.
     *
     * @param usuarioId O ID do usuário.
     * @param tipo O tipo de meta.
     * @return Uma lista de DTOs das metas encontradas.
     */
    public List<MetaResponseDTO> findByUsuarioIdAndTipo(UUID usuarioId, TipoMeta tipo) {
        return metaRepository.findByUsuarioIdAndTipo(usuarioId, tipo).stream()
                .map(metaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza uma meta e recalcula as metas relacionadas (semanal, mensal) com base nos novos valores.
     *
     * @param metaId O ID da meta a ser atualizada.
     * @param metaRequestDTO DTO com os novos dados da meta.
     * @return Uma lista de DTOs das metas atualizadas.
     * @throws EntityNotFoundException se a meta principal ou as metas relacionadas não forem encontradas.
     */
    public List<MetaResponseDTO> update(UUID metaId, MetaRequestDTO metaRequestDTO) {
        Meta meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada com id: " + metaId));

        meta.setCaloriasObjetivo(metaRequestDTO.caloriasObjetivo());
        meta.setProteinasObjetivo(metaRequestDTO.proteinasObjetivo());
        meta.setCarboidratosObjetivo(metaRequestDTO.carboidratosObjetivo());
        meta.setGordurasObjetivo(metaRequestDTO.gordurasObjetivo());

        Meta semanal = metaRepository
                .findActiveMetaForDate(meta.getUsuario().getId(), TipoMeta.SEMANAL.name())
                .orElseThrow(() -> new EntityNotFoundException("Meta semanal não encontrada para atualização."));
        metaMultiply(semanal, meta, BigDecimal.valueOf(7));

        Meta mensal = metaRepository
                .findActiveMetaForDate(meta.getUsuario().getId(), TipoMeta.MENSAL.name())
                .orElseThrow(() -> new EntityNotFoundException("Meta mensal não encontrada para atualização."));
        metaMultiply(mensal, meta, BigDecimal.valueOf(30));

        List<Meta> updatedMetas = metaRepository.saveAll(List.of(meta, semanal, mensal));
        return updatedMetas.stream().map(metaMapper::toResponseDTO).toList();
    }

    /**
     * Multiplica os valores nutricionais de uma meta com base em uma meta de referência e um fator.
     *
     * @param meta A meta a ser modificada.
     * @param base A meta de referência.
     * @param fator O fator de multiplicação.
     */
    private void metaMultiply(Meta meta, Meta base, BigDecimal fator) {
        meta.setCaloriasObjetivo(base.getCaloriasObjetivo().multiply(fator));
        meta.setCarboidratosObjetivo(base.getCarboidratosObjetivo().multiply(fator));
        meta.setProteinasObjetivo(base.getProteinasObjetivo().multiply(fator));
        meta.setGordurasObjetivo(base.getGordurasObjetivo().multiply(fator));
    }

    /**
     * Calcula o progresso nutricional de um usuário em relação a uma meta ativa.
     *
     * @param usuarioId O ID do usuário.
     * @param tipoMeta O tipo de meta (DIARIA, SEMANAL, MENSAL).
     * @return Um DTO com o progresso detalhado de calorias, proteínas, carboidratos e gorduras.
     * @throws EntityNotFoundException se nenhuma meta ativa for encontrada.
     */
    public MetaProgressoDTO calculateProgresso(UUID usuarioId, TipoMeta tipoMeta) {
        LocalDate hoje = LocalDate.now();
        Meta meta = metaRepository.findActiveMetaForDate(usuarioId, tipoMeta.name())
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma meta ativa do tipo " + tipoMeta + " encontrada para o usuário."));

        LocalDate inicioPeriodo = getInicioPeriodo(tipoMeta, hoje);
        LocalDate fimPeriodo = getFimPeriodo(tipoMeta, hoje);

        List<Refeicao> refeicoes = refeicaoRepository.findByUsuarioIdAndDateBetween(
                usuarioId,
                inicioPeriodo.atStartOfDay().atOffset(ZoneOffset.UTC),
                fimPeriodo.atTime(LocalTime.MAX).atOffset(ZoneOffset.UTC)
        );

        BigDecimal caloriasConsumidas = BigDecimal.ZERO;
        BigDecimal proteinasConsumidas = BigDecimal.ZERO;
        BigDecimal carboidratosConsumidos = BigDecimal.ZERO;
        BigDecimal gordurasConsumidas = BigDecimal.ZERO;

        for (Refeicao refeicao : refeicoes) {
            for (ItemRefeicao item : refeicao.getItens()) {
                BigDecimal quantidade = item.getQuantidade();
                caloriasConsumidas = caloriasConsumidas.add(item.getAlimento().getCalorias().multiply(quantidade));
                proteinasConsumidas = proteinasConsumidas.add(item.getAlimento().getProteinasG().multiply(quantidade));
                carboidratosConsumidos = carboidratosConsumidos.add(item.getAlimento().getCarboidratosG().multiply(quantidade));
                gordurasConsumidas = gordurasConsumidas.add(item.getAlimento().getGordurasG().multiply(quantidade));
            }
        }

        return new MetaProgressoDTO(
                createProgressoItem(meta.getCaloriasObjetivo(), caloriasConsumidas),
                createProgressoItem(meta.getProteinasObjetivo(), proteinasConsumidas),
                createProgressoItem(meta.getCarboidratosObjetivo(), carboidratosConsumidos),
                createProgressoItem(meta.getGordurasObjetivo(), gordurasConsumidas)
        );
    }

    /**
     * Cria um item de progresso, calculando a porcentagem do valor consumido em relação ao objetivo.
     *
     * @param objetivo O valor alvo.
     * @param consumido O valor consumido.
     * @return Um objeto ProgressoItem com os valores e a porcentagem.
     */
    private MetaProgressoDTO.ProgressoItem createProgressoItem(BigDecimal objetivo, BigDecimal consumido) {
        BigDecimal porcentagem = BigDecimal.ZERO;
        if (objetivo != null && objetivo.compareTo(BigDecimal.ZERO) > 0) {
            porcentagem = consumido.divide(objetivo, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }
        return new MetaProgressoDTO.ProgressoItem(objetivo, consumido, porcentagem);
    }

    /**
     * Determina a data de início do período com base no tipo de meta.
     *
     * @param tipoMeta O tipo de meta.
     * @param dataBase A data de referência.
     * @return A data de início do período.
     */
    private LocalDate getInicioPeriodo(TipoMeta tipoMeta, LocalDate dataBase) {
        return switch (tipoMeta) {
            case DIARIA -> dataBase;
            case SEMANAL -> dataBase.with(DayOfWeek.MONDAY);
            case MENSAL -> dataBase.withDayOfMonth(1);
        };
    }

    /**
     * Determina a data de fim do período com base no tipo de meta.
     *
     * @param tipoMeta O tipo de meta.
     * @param dataBase A data de referência.
     * @return A data de fim do período.
     */
    private LocalDate getFimPeriodo(TipoMeta tipoMeta, LocalDate dataBase) {
        return switch (tipoMeta) {
            case DIARIA -> dataBase;
            case SEMANAL -> dataBase.with(DayOfWeek.SUNDAY);
            case MENSAL -> dataBase.withDayOfMonth(dataBase.lengthOfMonth());
        };
    }
}

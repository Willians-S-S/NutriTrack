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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetaService {

    private final MetaRepository metaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RefeicaoRepository refeicaoRepository;
    private final MetaMapper metaMapper;


    public List<MetaResponseDTO> create(UUID usuarioId, MetaRequestDTO metaRequestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + usuarioId));

        Boolean exists = metaRepository.existsByUsuarioIdAndTipo(usuarioId, metaRequestDTO.tipo());
        System.out.println(exists);
        if (exists) {
            String tipo =  metaRequestDTO.tipo().toString();
            throw new ConflictException(String.format("Já existe uma meta do tipo '%s' para este usuário. Atualize a meta existente ou remova-a antes de criar uma meta.", tipo));
        }

        Meta meta = metaMapper.toEntity(metaRequestDTO);
        meta.setUsuario(usuario);

        Meta metaSemanal = cloneMeta(meta, TipoMeta.SEMANAL, BigDecimal.valueOf(7));
        Meta metaMensal = cloneMeta(meta, TipoMeta.MENSAL, BigDecimal.valueOf(30));


        List<Meta> savedMeta = metaRepository.saveAll(List.of(meta, metaSemanal, metaMensal));

        return savedMeta.stream().map(metaMapper::toResponseDTO).toList();
    }

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


    public List<MetaResponseDTO> findByUsuarioIdAndTipo(UUID usuarioId, TipoMeta tipo) {
        return metaRepository.findByUsuarioIdAndTipo(usuarioId, tipo).stream()
                .map(metaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MetaResponseDTO> update(UUID metaId, MetaRequestDTO metaRequestDTO) {
        Meta meta = metaRepository.findById(metaId)
                .orElseThrow(() -> new EntityNotFoundException("Meta não encontrada com id: " + metaId));

        meta.setCaloriasObjetivo(metaRequestDTO.caloriasObjetivo());
        meta.setProteinasObjetivo(metaRequestDTO.proteinasObjetivo());
        meta.setCarboidratosObjetivo(metaRequestDTO.carboidratosObjetivo());
        meta.setGordurasObjetivo(metaRequestDTO.gordurasObjetivo());

        Optional<Meta> semanal = metaRepository
                .findActiveMetaForDate(meta.getUsuario().getId(), TipoMeta.SEMANAL.name());

        semanal.ifPresent(s -> metaMultiply(s, meta, BigDecimal.valueOf(7)));
        Optional<Meta> mensal =  metaRepository
                .findActiveMetaForDate(meta.getUsuario().getId(), TipoMeta.MENSAL.name());

        mensal.ifPresent(s -> metaMultiply(s, meta, BigDecimal.valueOf(30)));

        List<Meta> updatedMetas = metaRepository.saveAll(List.of(meta, semanal.get(), mensal.get()));
        return updatedMetas.stream().map(metaMapper::toResponseDTO).toList();
    }

    private Meta metaMultiply(Meta meta, Meta base, BigDecimal fator) {
        meta.setUsuario(base.getUsuario());
        meta.setCaloriasObjetivo(base.getCaloriasObjetivo().multiply(fator));
        meta.setCarboidratosObjetivo(base.getCarboidratosObjetivo().multiply(fator));
        meta.setProteinasObjetivo(base.getProteinasObjetivo().multiply(fator));
        meta.setGordurasObjetivo(base.getGordurasObjetivo().multiply(fator));
        return meta;
    }

    public MetaProgressoDTO calculateProgresso(UUID usuarioId, TipoMeta tipoMeta) {
        LocalDate hoje = LocalDate.now();
        Meta meta = metaRepository.findActiveMetaForDate(usuarioId, tipoMeta.name())
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma meta ativa do tipo " + tipoMeta + " encontrada para o usuário."));

        LocalDate inicioPeriodo = getInicioPeriodo(tipoMeta, hoje);
        LocalDate fimPeriodo = getFimPeriodo(tipoMeta, hoje);


        List<Refeicao> refeicoes = refeicaoRepository.findByUsuarioIdAndDateBetween(
                usuarioId,
                inicioPeriodo.atStartOfDay().toLocalDate(),
                fimPeriodo.atTime(LocalTime.MAX).toLocalDate()
        );

        BigDecimal caloriasConsumidas = BigDecimal.ZERO;
        BigDecimal proteinasConsumidas = BigDecimal.ZERO;
        BigDecimal carboidratosConsumidos = BigDecimal.ZERO;
        BigDecimal gordurasConsumidas = BigDecimal.ZERO;

        System.out.println("Refeições ---- ");
        System.out.println(inicioPeriodo.atStartOfDay().toLocalDate());
        System.out.println(fimPeriodo.atTime(LocalTime.MAX).toLocalDate());
        for (Refeicao ref : refeicoes) {
            System.out.println(ref.getTipo());
        }

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

    private MetaProgressoDTO.ProgressoItem createProgressoItem(BigDecimal objetivo, BigDecimal consumido) {
        BigDecimal porcentagem = BigDecimal.ZERO;
        if (objetivo.compareTo(BigDecimal.ZERO) > 0) {
            porcentagem = consumido.divide(objetivo, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        }
        return new MetaProgressoDTO.ProgressoItem(objetivo, consumido, porcentagem);
    }

    private LocalDate getInicioPeriodo(TipoMeta tipoMeta, LocalDate dataBase) {
        return switch (tipoMeta) {
            case DIARIA -> dataBase;
            case SEMANAL -> dataBase.with(DayOfWeek.MONDAY);
            case MENSAL -> dataBase.withDayOfMonth(1);
        };
    }

    private LocalDate getFimPeriodo(TipoMeta tipoMeta, LocalDate dataBase) {
        return switch (tipoMeta) {
            case DIARIA -> dataBase;
            case SEMANAL -> dataBase.with(DayOfWeek.SUNDAY);
            case MENSAL -> dataBase.withDayOfMonth(dataBase.lengthOfMonth());
        };
    }
}

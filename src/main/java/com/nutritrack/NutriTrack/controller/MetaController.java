package com.nutritrack.NutriTrack.controller;

import com.nutritrack.NutriTrack.dto.MetaProgressoDTO;
import com.nutritrack.NutriTrack.dto.MetaRequestDTO;
import com.nutritrack.NutriTrack.dto.MetaResponseDTO;
import com.nutritrack.NutriTrack.enums.TipoMeta;
import com.nutritrack.NutriTrack.service.MetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar as metas nutricionais dos usuários.
 */
@RestController
@RequestMapping("/api/v1/usuarios/{usuarioId}/metas")
@RequiredArgsConstructor
public class MetaController {

    private final MetaService metaService;

    /**
     * Cria uma nova meta nutricional para um usuário.
     *
     * @param usuarioId O ID do usuário para o qual a meta será criada.
     * @param metaRequestDTO O DTO com os dados da meta a ser criada.
     * @return Um ResponseEntity com a meta criada e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<List<MetaResponseDTO>> create(@PathVariable UUID usuarioId, @Valid @RequestBody MetaRequestDTO metaRequestDTO) {
        List<MetaResponseDTO> createdMeta = metaService.create(usuarioId, metaRequestDTO);
        return new ResponseEntity<>(createdMeta, HttpStatus.CREATED);
    }

    /**
     * Busca as metas de um usuário por tipo.
     *
     * @param usuarioId O ID do usuário.
     * @param tipo O tipo de meta a ser buscada (DIARIA, SEMANAL, MENSAL).
     * @return Um ResponseEntity com a lista de metas encontradas e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<MetaResponseDTO>> getMetasByUsuarioAndTipo(
            @PathVariable UUID usuarioId,
            @RequestParam TipoMeta tipo) {
        List<MetaResponseDTO> metas = metaService.findByUsuarioIdAndTipo(usuarioId, tipo);
        return ResponseEntity.ok(metas);
    }

    /**
     * Atualiza uma meta nutricional existente.
     *
     * @param metaId O ID da meta a ser atualizada.
     * @param metaRequestDTO O DTO com os novos dados da meta.
     * @return Um ResponseEntity com a meta atualizada e o status HTTP 200 (OK).
     */
    @PatchMapping("/{metaId}")
    public ResponseEntity<List<MetaResponseDTO>> updateMeta(
            @PathVariable UUID metaId,
            @Valid @RequestBody MetaRequestDTO metaRequestDTO) {
        List<MetaResponseDTO> updatedMeta = metaService.update(metaId, metaRequestDTO);
        return ResponseEntity.ok(updatedMeta);
    }

    /**
     * Calcula e retorna o progresso de uma meta nutricional de um usuário.
     *
     * @param usuarioId O ID do usuário.
     * @param tipo O tipo de meta para o qual o progresso será calculado.
     * @return Um ResponseEntity com o DTO de progresso da meta e o status HTTP 200 (OK).
     */
    @GetMapping("/progresso")
    public ResponseEntity<MetaProgressoDTO> getProgressoMeta(
            @PathVariable UUID usuarioId,
            @RequestParam("tipo") TipoMeta tipo) {
        MetaProgressoDTO progresso = metaService.calculateProgresso(usuarioId, tipo);
        return ResponseEntity.ok(progresso);
    }
}

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

@RestController
@RequestMapping("/api/v1//usuarios/{usuarioId}/metas")
@RequiredArgsConstructor
public class MetaController {

    private final MetaService metaService;

    @PostMapping
    public ResponseEntity<List<MetaResponseDTO>> create(@PathVariable UUID usuarioId, @Valid @RequestBody MetaRequestDTO metaRequestDTO) {
        List<MetaResponseDTO> createdMeta = metaService.create(usuarioId, metaRequestDTO);
        return new ResponseEntity<>(createdMeta, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MetaResponseDTO>> getMetasByUsuarioAndTipo(
            @PathVariable UUID usuarioId,
            @RequestParam TipoMeta tipo) {
        List<MetaResponseDTO> metas = metaService.findByUsuarioIdAndTipo(usuarioId, tipo);
        return ResponseEntity.ok(metas);
    }

    @PutMapping("/{metaId}")
    public ResponseEntity<List<MetaResponseDTO>> updateMeta(
            @PathVariable UUID metaId,
            @Valid @RequestBody MetaRequestDTO metaRequestDTO) {
        List<MetaResponseDTO> updatedMeta = metaService.update(metaId, metaRequestDTO);
        return ResponseEntity.ok(updatedMeta);
    }

    @GetMapping("/progresso")
    public ResponseEntity<MetaProgressoDTO> getProgressoMeta(
            @PathVariable UUID usuarioId,
            @RequestParam("tipo") TipoMeta tipo) {
        MetaProgressoDTO progresso = metaService.calculateProgresso(usuarioId, tipo);
        return ResponseEntity.ok(progresso);
    }
}

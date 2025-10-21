package com.nutritrack.NutriTrack.controller;

import com.nutritrack.NutriTrack.dto.MetaRequestDTO;
import com.nutritrack.NutriTrack.dto.MetaResponseDTO;
import com.nutritrack.NutriTrack.service.MetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios/{usuarioId}/metas")
@RequiredArgsConstructor
public class MetaController {

    private final MetaService metaService;

    @PostMapping
    public ResponseEntity<MetaResponseDTO> create(@PathVariable UUID usuarioId, @Valid @RequestBody MetaRequestDTO metaRequestDTO) {
        MetaResponseDTO createdMeta = metaService.create(usuarioId, metaRequestDTO);
        return new ResponseEntity<>(createdMeta, HttpStatus.CREATED);
    }
}

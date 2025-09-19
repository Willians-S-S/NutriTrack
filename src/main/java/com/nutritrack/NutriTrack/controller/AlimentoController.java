package com.nutritrack.controller;

import com.nutritrack.dto.AlimentoRequestDTO;
import com.nutritrack.dto.AlimentoResponseDTO;
import com.nutritrack.service.AlimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alimentos")
@RequiredArgsConstructor
@Tag(name = "Alimentos", description = "Endpoints para gerenciar alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    @GetMapping
    @Operation(summary = "Lista todos os alimentos de forma paginada com filtro opcional por nome")
    public ResponseEntity<Page<AlimentoResponseDTO>> getAllAlimentos(
        @Parameter(description = "Termo para buscar no nome do alimento (case-insensitive)") @RequestParam(required = false) String nome,
        Pageable pageable
    ) {
        return ResponseEntity.ok(alimentoService.findAll(pageable, nome));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um alimento pelo ID")
    public ResponseEntity<AlimentoResponseDTO> getAlimentoById(@PathVariable UUID id) {
        return ResponseEntity.ok(alimentoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo alimento (Requer ROLE_ADMIN)")
    public ResponseEntity<AlimentoResponseDTO> createAlimento(@Valid @RequestBody AlimentoRequestDTO requestDTO) {
        AlimentoResponseDTO createdAlimento = alimentoService.create(requestDTO);
        return new ResponseEntity<>(createdAlimento, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um alimento existente (Requer ROLE_ADMIN)")
    public ResponseEntity<AlimentoResponseDTO> updateAlimento(@PathVariable UUID id, @Valid @RequestBody AlimentoRequestDTO requestDTO) {
        return ResponseEntity.ok(alimentoService.update(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um alimento (Requer ROLE_ADMIN)")
    public ResponseEntity<Void> deleteAlimento(@PathVariable UUID id) {
        alimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

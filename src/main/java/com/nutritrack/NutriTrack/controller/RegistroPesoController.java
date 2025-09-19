package com.nutritrack.controller;

import com.nutritrack.dto.RegistroPesoRequestDTO;
import com.nutritrack.dto.RegistroPesoResponseDTO;
import com.nutritrack.service.RegistroPesoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/registros-peso")
@RequiredArgsConstructor
@Tag(name = "Registros de Peso", description = "Endpoints para gerenciar os registros de peso do usuário")
public class RegistroPesoController {

    private final RegistroPesoService registroPesoService;

    @PostMapping
    @Operation(summary = "Cria um novo registro de peso para o usuário autenticado")
    public ResponseEntity<RegistroPesoResponseDTO> createRegistroPeso(
        UUID id,
        @Valid @RequestBody RegistroPesoRequestDTO requestDTO
    ) {
        RegistroPesoResponseDTO createdRegistro = registroPesoService.create(id, requestDTO);
        return new ResponseEntity<>(createdRegistro, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lista os registros de peso do usuário por um intervalo de datas")
    public ResponseEntity<List<RegistroPesoResponseDTO>> getRegistrosByDateRange(
            UUID id,
        @Parameter(description = "Data de início (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @Parameter(description = "Data de fim (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(registroPesoService.findByDateRange(id, start, end));
    }

    @PutMapping("/{idUser}/{id}")
    @Operation(summary = "Atualiza um registro de peso. Pode ser usado para corrigir um valor ou data.")
    public ResponseEntity<RegistroPesoResponseDTO> updateRegistroPeso(
            @PathVariable UUID idUser,
        @PathVariable UUID id,
        @Valid @RequestBody RegistroPesoRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(registroPesoService.update(idUser, id, requestDTO));
    }

    @DeleteMapping("/{idUser}/{id}")
    @Operation(summary = "Deleta um registro de peso")
    public ResponseEntity<Void> deleteRegistroPeso(
            @PathVariable UUID idUser,
        @PathVariable UUID id
    ) {
        registroPesoService.delete(idUser, id);
        return ResponseEntity.noContent().build();
    }
}

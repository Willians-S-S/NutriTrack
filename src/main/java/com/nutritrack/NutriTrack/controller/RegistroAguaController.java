package com.nutritrack.controller;

import com.nutritrack.dto.RegistroAguaDiarioDTO;
import com.nutritrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.dto.RegistroAguaResponseDTO;
import com.nutritrack.entity.Usuario;
import com.nutritrack.service.RegistroAguaService;
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
@RequestMapping("/api/v1/registros-agua")
@RequiredArgsConstructor
@Tag(name = "Registros de Água", description = "Endpoints para gerenciar os registros de consumo de água")
public class RegistroAguaController {

    private final RegistroAguaService registroAguaService;

    @PostMapping("/{idUser}")
    @Operation(summary = "Adiciona um novo registro de consumo de água para o usuário autenticado")
    public ResponseEntity<RegistroAguaResponseDTO> createRegistroAgua(
            @PathVariable UUID idUser,
        @Valid @RequestBody RegistroAguaRequestDTO requestDTO
    ) {
        RegistroAguaResponseDTO createdRegistro = registroAguaService.create(idUser, requestDTO);
        return new ResponseEntity<>(createdRegistro, HttpStatus.CREATED);
    }

    @GetMapping("/{idUser}")
    @Operation(summary = "Lista os registros de consumo de água do usuário por um intervalo de datas")
    public ResponseEntity<List<RegistroAguaResponseDTO>> getRegistrosByDateRange(
            @PathVariable UUID idUser,
        @Parameter(description = "Data de início (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @Parameter(description = "Data de fim (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(registroAguaService.findByDateRange(idUser, start, end));
    }

    @GetMapping("/summary/{idUser}")
    @Operation(summary = "Retorna um resumo diário do consumo de água por um intervalo de datas")
    public ResponseEntity<List<RegistroAguaDiarioDTO>> getDailySummary(
            @PathVariable UUID idUser,
        @Parameter(description = "Data de início (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @Parameter(description = "Data de fim (YYYY-MM-DD)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(registroAguaService.getDailySummary(idUser, start, end));
    }

    @DeleteMapping("/{idUser}/{id}")
    @Operation(summary = "Deleta um registro de consumo de água")
    public ResponseEntity<Void> deleteRegistroAgua(
            @PathVariable UUID idUser,
        @PathVariable UUID id
    ) {
        registroAguaService.delete(idUser, id);
        return ResponseEntity.noContent().build();
    }
}

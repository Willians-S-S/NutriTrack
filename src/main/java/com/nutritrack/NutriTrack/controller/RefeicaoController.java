package com.nutritrack.controller;

import com.nutritrack.dto.RefeicaoRequestDTO;
import com.nutritrack.dto.RefeicaoResponseDTO;
import com.nutritrack.service.RefeicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/refeicoes")
@RequiredArgsConstructor
@Tag(name = "Refeições", description = "Endpoints para gerenciar as refeições do usuário")
public class RefeicaoController {

    private final RefeicaoService refeicaoService;

    @PostMapping("/{idUser}")
    @Operation(summary = "Cria uma nova refeição para um usuário específico")
    public ResponseEntity<RefeicaoResponseDTO> createRefeicao(
            @PathVariable UUID idUser,
            @Valid @RequestBody RefeicaoRequestDTO requestDTO
    ) {
        RefeicaoResponseDTO createdRefeicao = refeicaoService.create(idUser, requestDTO);
        return new ResponseEntity<>(createdRefeicao, HttpStatus.CREATED);
    }

    @GetMapping("/{idUser}/{id}")
    @Operation(summary = "Busca uma refeição específica de um usuário pelo ID da refeição")
    public ResponseEntity<RefeicaoResponseDTO> getRefeicaoById(
            @PathVariable UUID idUser,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(refeicaoService.findById(idUser, id));
    }

    @GetMapping("/{idUser}")
    @Operation(summary = "Lista as refeições de um usuário por um intervalo de datas")
    public ResponseEntity<List<RefeicaoResponseDTO>> getRefeicoesByDateRange(
            @PathVariable UUID idUser,
            @Parameter(description = "Data de início (ISO-8601)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @Parameter(description = "Data de fim (ISO-8601)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end
    ) {
        return ResponseEntity.ok(refeicaoService.findByDateRange(idUser, start, end));
    }

    @DeleteMapping("/{idUser}/{id}")
    @Operation(summary = "Deleta uma refeição de um usuário")
    public ResponseEntity<Void> deleteRefeicao(
            @PathVariable UUID idUser,
            @PathVariable UUID id
    ) {
        refeicaoService.delete(idUser, id);
        return ResponseEntity.noContent().build();
    }
}
package com.nutritrack.NutriTrack.controller;

import com.nutritrack.NutriTrack.dto.RefeicaoRequestDTO;
import com.nutritrack.NutriTrack.dto.RefeicaoResponseDTO;
import com.nutritrack.NutriTrack.service.RefeicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller para operações relacionadas a refeições de usuários.
 * Este controller oferece endpoints para criar, buscar, listar e deletar refeições.
 */
@RestController
@RequestMapping("/api/v1/refeicoes")
@RequiredArgsConstructor
@Tag(name = "Refeições", description = "Endpoints para gerenciar as refeições do usuário")
public class RefeicaoController {

    private final RefeicaoService refeicaoService;

    /**
     * Endpoint para criar uma nova refeição para um usuário específico.
     *
     * @param idUser      ID do usuário para o qual a refeição será criada.
     * @param requestDTO  Objeto contendo os dados da refeição a ser criada.
     * @return ResponseEntity contendo os dados da refeição criada e o status HTTP CREATED.
     */
    @PostMapping("/{idUser}")
    @Operation(summary = "Cria uma nova refeição para um usuário específico")
    public ResponseEntity<RefeicaoResponseDTO> createRefeicao(
            @PathVariable UUID idUser,
            @Valid @RequestBody RefeicaoRequestDTO requestDTO
    ) {
        RefeicaoResponseDTO createdRefeicao = refeicaoService.create(idUser, requestDTO);
        return new ResponseEntity<>(createdRefeicao, HttpStatus.CREATED);
    }

    /**
     * Endpoint para buscar uma refeição específica de um usuário pelo ID da refeição.
     *
     * @param idUser ID do usuário que possui a refeição.
     * @param id     ID da refeição a ser buscada.
     * @return ResponseEntity contendo os dados da refeição encontrada e o status HTTP OK.
     */
    @GetMapping("/{idUser}/{id}")
    @Operation(summary = "Busca uma refeição específica de um usuário pelo ID da refeição")
    public ResponseEntity<RefeicaoResponseDTO> getRefeicaoById(
            @PathVariable UUID idUser,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(refeicaoService.findById(idUser, id));
    }

    /**
     * Endpoint para listar as refeições de um usuário por um intervalo de datas.
     *
     * @param idUser ID do usuário cujas refeições serão listadas.
     * @param start  Data de início do intervalo (ISO-8601).
     * @param end    Data de fim do intervalo (ISO-8601).
     * @return ResponseEntity contendo a lista de refeições encontradas e o status HTTP OK.
     */
    @GetMapping("/usuario/{idUser}")
    @Operation(summary = "Lista as refeições de um usuário por um intervalo de datas")
    public ResponseEntity<List<RefeicaoResponseDTO>> getRefeicoesByDateRange(
            @PathVariable UUID idUser,
            @Parameter(description = "Data de início (ISO-8601)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @Parameter(description = "Data de fim (ISO-8601)", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end
    ) {
        return ResponseEntity.ok(refeicaoService.findByDateRange(idUser, start, end));
    }

    @PutMapping("/{idUser}/{id}")
    @Operation(summary = "Atualiza uma refeição de um usuário")
    public ResponseEntity<RefeicaoResponseDTO> updateRefeicao(
            @PathVariable UUID idUser,
            @PathVariable UUID id,
            @Valid @RequestBody RefeicaoRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(refeicaoService.update(idUser, id, requestDTO));
    }

    /**
     * Endpoint para deletar uma refeição de um usuário.
     *
     * @param idUser ID do usuário que possui a refeição a ser deletada.
     * @param id     ID da refeição a ser deletada.
     * @return ResponseEntity com status HTTP NO_CONTENT após a deleção bem-sucedida.
     */
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
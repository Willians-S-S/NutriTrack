package com.nutritrack.NutriTrack.controller;

import com.nutritrack.NutriTrack.dto.RegistroAguaDiarioDTO;
import com.nutritrack.NutriTrack.dto.RegistroAguaRequestDTO;
import com.nutritrack.NutriTrack.dto.RegistroAguaResponseDTO;
import com.nutritrack.NutriTrack.service.RegistroAguaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller responsável por gerenciar os registros de consumo de água dos usuários.
 * Fornece endpoints para criação, listagem, resumo diário e exclusão de registros.
 * 
 * <p>Endpoints disponíveis:</p>
 * <ul>
 *     <li>POST /api/v1/registros-agua/{idUser} - Adiciona um novo registro de consumo de água</li>
 *     <li>GET /api/v1/registros-agua/{idUser} - Lista registros de consumo de água por intervalo de datas</li>
 *     <li>GET /api/v1/registros-agua/summary/{idUser} - Retorna resumo diário de consumo de água por intervalo de datas</li>
 *     <li>DELETE /api/v1/registros-agua/{idUser}/{id} - Deleta um registro de consumo de água</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/registros-agua")
@RequiredArgsConstructor
@Tag(name = "Registros de Água", description = "Endpoints para gerenciar os registros de consumo de água")
public class RegistroAguaController {

    private final RegistroAguaService registroAguaService;

    /**
     * Adiciona um novo registro de consumo de água para o usuário especificado.
     *
     * @param idUser UUID do usuário
     * @param requestDTO DTO contendo os dados do registro de água
     * @return Registro criado encapsulado em {@link RegistroAguaResponseDTO} com status 201 CREATED
     */
    @PostMapping("/{idUser}")
    public ResponseEntity<RegistroAguaResponseDTO> createRegistroAgua(
            @PathVariable UUID idUser,
            @Valid @RequestBody RegistroAguaRequestDTO requestDTO
    ) {
        RegistroAguaResponseDTO createdRegistro = registroAguaService.create(idUser, requestDTO);
        return new ResponseEntity<>(createdRegistro, HttpStatus.CREATED);
    }

    /**
     * Lista os registros de consumo de água do usuário em um intervalo de datas.
     *
     * @param idUser UUID do usuário
     * @param start Data de início (YYYY-MM-DD)
     * @param end Data de fim (YYYY-MM-DD)
     * @return Lista de {@link RegistroAguaResponseDTO} com os registros encontrados
     */
    @GetMapping("/usuario/{idUser}")
    public ResponseEntity<List<RegistroAguaResponseDTO>> getRegistrosByDateRange(
            @PathVariable UUID idUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(registroAguaService.findByDateRange(idUser, start, end));
    }

    /**
     * Retorna um resumo diário do consumo de água do usuário em um intervalo de datas.
     *
     * @param idUser UUID do usuário
     * @param start Data de início (YYYY-MM-DD)
     * @param end Data de fim (YYYY-MM-DD)
     * @return Lista de {@link RegistroAguaDiarioDTO} representando o resumo diário
     */
    @GetMapping("/summary/{idUser}")
    public ResponseEntity<List<RegistroAguaDiarioDTO>> getDailySummary(
            @PathVariable UUID idUser,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(registroAguaService.getDailySummary(idUser, start, end));
    }

    /**
     * Deleta um registro de consumo de água específico do usuário.
     *
     * @param idUser UUID do usuário
     * @param id UUID do registro de água a ser deletado
     * @return {@link ResponseEntity} com status 204 NO CONTENT
     */
    @DeleteMapping("/{idUser}/{id}")
    public ResponseEntity<Void> deleteRegistroAgua(
            @PathVariable UUID idUser,
            @PathVariable UUID id
    ) {
        registroAguaService.delete(idUser, id);
        return ResponseEntity.noContent().build();
    }
}

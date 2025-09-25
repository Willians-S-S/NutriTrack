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

/**
 * Controlador REST para gerenciar operações relacionadas a alimentos.
 *
 * Este controlador oferece endpoints para criar, ler, atualizar e deletar informações sobre alimentos.
 * A maioria dos endpoints requer autenticação com a ROLE_ADMIN.
 */
@RestController
@RequestMapping("/api/v1/alimentos")
@RequiredArgsConstructor
@Tag(name = "Alimentos", description = "Endpoints para gerenciar alimentos")
public class AlimentoController {

    private final AlimentoService alimentoService;

    /**
     * Lista todos os alimentos de forma paginada com filtro opcional por nome.
     *
     * @param nome      Termo para buscar no nome do alimento (case-insensitive). Se nulo, retorna todos os alimentos.
     * @param pageable  Objeto de paginação para controlar o tamanho da página e o número da página.
     * @return          ResponseEntity contendo uma Page de AlimentoResponseDTO.
     */
    @GetMapping
    @Operation(summary = "Lista todos os alimentos de forma paginada com filtro opcional por nome")
    public ResponseEntity<Page<AlimentoResponseDTO>> getAllAlimentos(
        @Parameter(description = "Termo para buscar no nome do alimento (case-insensitive)") @RequestParam(required = false) String nome,
        Pageable pageable
    ) {
        return ResponseEntity.ok(alimentoService.findAll(pageable, nome));
    }

    /**
     * Busca um alimento pelo ID.
     *
     * @param id  ID do alimento a ser buscado.
     * @return    ResponseEntity contendo um AlimentoResponseDTO se o alimento for encontrado.
     * @throws    NotFoundException Se o alimento não for encontrado.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Busca um alimento pelo ID")
    public ResponseEntity<AlimentoResponseDTO> getAlimentoById(@PathVariable UUID id) {
        return ResponseEntity.ok(alimentoService.findById(id));
    }

    /**
     * Cria um novo alimento.
     *
     * Este endpoint requer a ROLE_ADMIN.
     *
     * @param requestDTO  Objeto AlimentoRequestDTO contendo os dados do alimento a ser criado.
     * @return            ResponseEntity contendo o AlimentoResponseDTO do alimento criado e o status HTTP 201 (CREATED).
     * @throws            BadRequestException Se os dados de entrada forem inválidos.
     */
    @PostMapping
    @Operation(summary = "Cria um novo alimento (Requer ROLE_ADMIN)")
    public ResponseEntity<AlimentoResponseDTO> createAlimento(@Valid @RequestBody AlimentoRequestDTO requestDTO) {
        AlimentoResponseDTO createdAlimento = alimentoService.create(requestDTO);
        return new ResponseEntity<>(createdAlimento, HttpStatus.CREATED);
    }

    /**
     * Atualiza um alimento existente.
     *
     * Este endpoint requer a ROLE_ADMIN.
     *
     * @param id          ID do alimento a ser atualizado.
     * @param requestDTO  Objeto AlimentoRequestDTO contendo os dados atualizados do alimento.
     * @return            ResponseEntity contendo o AlimentoResponseDTO do alimento atualizado.
     * @throws            NotFoundException Se o alimento não for encontrado.
     * @throws            BadRequestException Se os dados de entrada forem inválidos.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um alimento existente (Requer ROLE_ADMIN)")
    public ResponseEntity<AlimentoResponseDTO> updateAlimento(@PathVariable UUID id, @Valid @RequestBody AlimentoRequestDTO requestDTO) {
        return ResponseEntity.ok(alimentoService.update(id, requestDTO));
    }

    /**
     * Deleta um alimento.
     *
     * Este endpoint requer a ROLE_ADMIN.
     *
     * @param id  ID do alimento a ser deletado.
     * @return    ResponseEntity com status HTTP 204 (NO_CONTENT).
     * @throws    NotFoundException Se o alimento não for encontrado.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um alimento (Requer ROLE_ADMIN)")
    public ResponseEntity<Void> deleteAlimento(@PathVariable UUID id) {
        alimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

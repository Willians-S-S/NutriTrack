/**
 * Controller responsável por gerenciar os registros de peso dos usuários.
 * Fornece endpoints para criação, listagem, atualização e exclusão de registros.
 * 
 * <p>Endpoints disponíveis:</p>
 * <ul>
 *     <li>POST /api/v1/registros-peso - Cria um novo registro de peso</li>
 *     <li>GET /api/v1/registros-peso/{id} - Lista registros de peso por intervalo de datas</li>
 *     <li>PUT /api/v1/registros-peso/{idUser}/{id} - Atualiza um registro de peso específico</li>
 *     <li>DELETE /api/v1/registros-peso/{idUser}/{id} - Deleta um registro de peso</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/registros-peso")
@RequiredArgsConstructor
@Tag(name = "Registros de Peso", description = "Endpoints para gerenciar os registros de peso do usuário")
public class RegistroPesoController {

    private final RegistroPesoService registroPesoService;

    /**
     * Cria um novo registro de peso para o usuário especificado.
     *
     * @param id UUID do usuário
     * @param requestDTO DTO com os dados do registro de peso
     * @return Registro criado encapsulado em {@link RegistroPesoResponseDTO} com status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<RegistroPesoResponseDTO> createRegistroPeso(
        UUID id,
        @Valid @RequestBody RegistroPesoRequestDTO requestDTO
    ) {
        RegistroPesoResponseDTO createdRegistro = registroPesoService.create(id, requestDTO);
        return new ResponseEntity<>(createdRegistro, HttpStatus.CREATED);
    }

    /**
     * Lista os registros de peso do usuário em um intervalo de datas.
     *
     * @param id UUID do usuário
     * @param start Data de início (YYYY-MM-DD)
     * @param end Data de fim (YYYY-MM-DD)
     * @return Lista de {@link RegistroPesoResponseDTO} com os registros encontrados
     */
    @GetMapping("/{id}")
    public ResponseEntity<List<RegistroPesoResponseDTO>> getRegistrosByDateRange(
        UUID id,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(registroPesoService.findByDateRange(id, start, end));
    }

    /**
     * Atualiza um registro de peso existente.
     *
     * @param idUser UUID do usuário
     * @param id UUID do registro de peso
     * @param requestDTO DTO com os novos dados do registro
     * @return Registro atualizado encapsulado em {@link RegistroPesoResponseDTO}
     */
    @PutMapping("/{idUser}/{id}")
    public ResponseEntity<RegistroPesoResponseDTO> updateRegistroPeso(
        @PathVariable UUID idUser,
        @PathVariable UUID id,
        @Valid @RequestBody RegistroPesoRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(registroPesoService.update(idUser, id, requestDTO));
    }

    /**
     * Deleta um registro de peso específico do usuário.
     *
     * @param idUser UUID do usuário
     * @param id UUID do registro de peso a ser deletado
     * @return {@link ResponseEntity} com status 204 NO CONTENT
     */
    @DeleteMapping("/{idUser}/{id}")
    public ResponseEntity<Void> deleteRegistroPeso(
        @PathVariable UUID idUser,
        @PathVariable UUID id
    ) {
        registroPesoService.delete(idUser, id);
        return ResponseEntity.noContent().build();
    }
}

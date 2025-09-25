/**
 * Controller responsável pelo gerenciamento de usuários.
 * Fornece endpoints para listagem, consulta de perfil, atualização e exclusão de usuários.
 *
 * <p>Endpoints disponíveis:</p>
 * <ul>
 *     <li>GET /api/v1/usuarios - Lista todos os usuários com paginação</li>
 *     <li>GET /api/v1/usuarios/me/{id} - Retorna o perfil de um usuário específico</li>
 *     <li>PUT /api/v1/usuarios/me/{id} - Atualiza o perfil do usuário</li>
 *     <li>DELETE /api/v1/usuarios/{id} - Deleta um usuário</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista todos os usuários com suporte a paginação.
     *
     * @param pageable Informações de paginação (número da página, tamanho, ordenação)
     * @return Página de {@link UserResponseDTO} contendo os usuários
     */
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAll(pageable));
    }

    /**
     * Retorna o perfil de um usuário específico.
     *
     * @param id UUID do usuário
     * @return {@link UserResponseDTO} contendo os dados do perfil
     */
    @GetMapping("/me/{id}")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    /**
     * Atualiza o perfil de um usuário específico.
     *
     * @param id UUID do usuário
     * @param updateDTO DTO contendo os dados para atualização do perfil
     * @return {@link UserResponseDTO} com os dados atualizados do usuário
     */
    @PutMapping("/me/{id}")
    public ResponseEntity<UserResponseDTO> updateCurrentUserProfile(
            @PathVariable UUID id,
            @Valid @RequestBody UserProfileUpdateDTO updateDTO
    ) {
        return ResponseEntity.ok(usuarioService.updateProfile(id, updateDTO));
    }

    /**
     * Deleta um usuário específico.
     *
     * @param id UUID do usuário a ser deletado
     * @return {@link ResponseEntity} com status 204 NO CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

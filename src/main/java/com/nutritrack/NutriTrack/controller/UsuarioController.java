package com.nutritrack.controller;

import com.nutritrack.dto.UserProfileUpdateDTO;
import com.nutritrack.dto.UserResponseDTO;
import com.nutritrack.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me/{id}")
    @Operation(summary = "Retorna o perfil do usuário")
    public ResponseEntity<UserResponseDTO> getCurrentUserProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }
    
        @PutMapping("/me/{id}")
    @Operation(summary = "Atualiza o perfil do usuário")
    public ResponseEntity<UserResponseDTO> updateCurrentUserProfile(
            @PathVariable UUID id,
        @Valid @RequestBody UserProfileUpdateDTO updateDTO
    ) {
        return ResponseEntity.ok(usuarioService.updateProfile(id, updateDTO));
    }

}

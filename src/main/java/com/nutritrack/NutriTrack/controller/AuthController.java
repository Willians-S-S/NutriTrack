package com.nutritrack.NutriTrack.controller;

import com.nutritrack.NutriTrack.dto.UserRequestDTO;
import com.nutritrack.NutriTrack.dto.UserResponseDTO;
import com.nutritrack.NutriTrack.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para operações de autenticação, como registro de usuário.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registrar um novo usuário.
     *
     * @param request Objeto contendo os dados do usuário a ser registrado.
     * @return ResponseEntity contendo os dados do usuário registrado e o status HTTP CREATED.
     */
    @PostMapping("/register")
    @Operation(summary = "Registra um novo usuário")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO request) {
        UserResponseDTO registeredUser = authService.register(request);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }
}

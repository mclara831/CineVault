package com.mariaclara.cinevault.controllers;


import com.mariaclara.cinevault.DTOs.requests.LoginRequest;
import com.mariaclara.cinevault.DTOs.requests.RegisterRequest;
import com.mariaclara.cinevault.DTOs.responses.LoginResponse;
import com.mariaclara.cinevault.infra.config.SecurityConfig;
import com.mariaclara.cinevault.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = SecurityConfig.SECURITY)
@Tag(name = "Auth Controller", description = "Controlador responsável por registrar e autenticar os dados dos usuários")
public class AuthenticationController {

    @Autowired
    private UserService userService;



    @Operation(summary = "Autentica o usuário ao sistema", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso!"),
            @ApiResponse(responseCode = "401", description = "Usuário/senha inválidos!")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest data) {
        String token = userService.authenticate(data);

        String expirationTime = userService.dateTimeConveter();

        return ResponseEntity.ok(new LoginResponse(token, expirationTime));
    }

    @Operation(summary = "Remove filmes/séries da lista de desejos do usuários", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso!"),
            @ApiResponse(responseCode = "401", description = "Campos preenchidos incorretamente!")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

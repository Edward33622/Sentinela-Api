package com.cibertec.ApiSentinela.user.controller;

import io.swagger.v3.oas.annotations.Operation;

import com.cibertec.ApiSentinela.user.dtos.request.LoginRequest;
import com.cibertec.ApiSentinela.user.dtos.response.AuthResponse;
import com.cibertec.ApiSentinela.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService usersService;

   public AuthController(UserService usersService) {
        this.usersService = usersService;
    }

    @Operation(summary = "Login de usuario y generación de JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = usersService.login(request);
        return ResponseEntity.ok(response);
    }
}
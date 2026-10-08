package com.cibertec.ApiSentinela.user.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank
    String email,

    @NotBlank
    String password
) {}
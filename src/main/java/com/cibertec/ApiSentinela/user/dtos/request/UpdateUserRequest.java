package com.cibertec.ApiSentinela.user.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
    @NotBlank
    String fullName,

    @Email
    String email,

    String phone
) {}
package com.cibertec.ApiSentinela.user.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank(message = "Full name is required")
    @Size(max = 200)
    String fullName,

    @NotBlank(message = "Email is required")
    @Email
    String email,

    String phone,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    String password
) {}
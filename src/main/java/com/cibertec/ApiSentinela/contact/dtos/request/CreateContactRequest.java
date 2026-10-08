package com.cibertec.ApiSentinela.contact.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateContactRequest(

        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Full name is required")
        @Size(max = 200, message = "Full name cannot exceed 200 characters")
        String fullName,

        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        String phoneNumber,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        Boolean enableWhatsapp
) {}

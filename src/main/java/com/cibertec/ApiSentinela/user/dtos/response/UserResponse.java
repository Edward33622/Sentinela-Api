package com.cibertec.ApiSentinela.user.dtos.response;

import com.cibertec.ApiSentinela.user.model.type.UserStatus;

public record UserResponse(
    Long id,
    String fullName,
    String email,
    String phone,
    UserStatus status
) {}
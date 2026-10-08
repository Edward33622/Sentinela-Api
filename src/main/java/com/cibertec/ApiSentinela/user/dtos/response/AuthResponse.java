package com.cibertec.ApiSentinela.user.dtos.response;

public record AuthResponse(String token, Long userId, String email) {}

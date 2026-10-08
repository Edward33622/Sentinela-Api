package com.cibertec.ApiSentinela.emergencyEvent.dtos.response;

import java.time.LocalDateTime;

import com.cibertec.ApiSentinela.emergencyEvent.model.type.EmergencyEventStatus;


public record EmergencyEventResponse(
        Long id,
        Long userId,

        EmergencyEventStatus status,
        LocalDateTime activatedAt,
        LocalDateTime closedAt
) {}

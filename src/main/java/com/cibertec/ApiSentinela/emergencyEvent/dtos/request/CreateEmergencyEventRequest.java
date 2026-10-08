package com.cibertec.ApiSentinela.emergencyEvent.dtos.request;

import com.cibertec.ApiSentinela.emergencyEvent.model.type.EmergencyEventStatus;

import jakarta.validation.constraints.NotNull;

public record CreateEmergencyEventRequest(

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Event type is required")
        EmergencyEventStatus status
) {}

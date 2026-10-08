package com.cibertec.ApiSentinela.emergencyLocation.dtos.request;

import jakarta.validation.constraints.NotNull;

public record CreateEmergencyLocationRequest(

        @NotNull(message = "Emergency Event ID is required")
        Long emergencyEventId,

        @NotNull(message = "Latitude is required")
        Double latitude,

        @NotNull(message = "Longitude is required")
        Double longitude
) {}

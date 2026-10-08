package com.cibertec.ApiSentinela.emergencyLocation.dtos.response;

import java.time.LocalDateTime;

public record EmergencyLocationResponse(
        Long id,
        Long emergencyEventId,
        Double latitude,
        Double longitude,
        LocalDateTime capturedAt
) {}

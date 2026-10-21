package com.cibertec.ApiSentinela.family.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberDto {
    private Long userId;
    private String fullName;
    private String phone;
    private Double latitude;
    private Double longitude;
    private Integer batteryLevel; // 0-100%
    private Boolean isCharging;
    private Boolean isEmergencyActive;
    private String currentStatus; // "🟢 En Casa", "🚗 En Trayecto", "🚨 SOS ACTIVO", "Desconectado"
    private LocalDateTime lastPing;
    private Boolean isAdmin;
}

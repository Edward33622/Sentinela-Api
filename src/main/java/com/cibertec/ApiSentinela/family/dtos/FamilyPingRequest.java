package com.cibertec.ApiSentinela.family.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyPingRequest {
    private Long userId;
    private Double latitude;
    private Double longitude;
    private Integer batteryLevel;
    private Boolean isCharging;
    private Boolean isEmergencyActive;
    private String statusZone; // e.g. "🟢 En Casa", "🎓 En Universidad", "🚗 En Trayecto"
}

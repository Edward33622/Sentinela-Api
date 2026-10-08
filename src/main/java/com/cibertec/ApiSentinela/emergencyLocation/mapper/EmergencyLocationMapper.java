package com.cibertec.ApiSentinela.emergencyLocation.mapper;

import org.springframework.stereotype.Component;

import com.cibertec.ApiSentinela.emergencyLocation.dtos.request.CreateEmergencyLocationRequest;
import com.cibertec.ApiSentinela.emergencyLocation.dtos.response.EmergencyLocationResponse;
import com.cibertec.ApiSentinela.emergencyLocation.model.EmergencyLocation;

@Component
public class EmergencyLocationMapper {

    // Convierte entidad a response
    public EmergencyLocationResponse toResponse(EmergencyLocation location) {
        Long emergencyEventId = location.getEmergencyEvent() != null ? location.getEmergencyEvent().getId() : null;

        return new EmergencyLocationResponse(
            location.getId(),
            emergencyEventId,
            location.getLatitude(),
            location.getLongitude(),
            location.getCapturedAt()
        );
    }

    // Convierte request a entidad
    public EmergencyLocation toEntity(CreateEmergencyLocationRequest request) {
        EmergencyLocation location = new EmergencyLocation();
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        location.setCapturedAt(java.time.LocalDateTime.now());
        // emergencyEvent se asigna en el service
        return location;
    }
}

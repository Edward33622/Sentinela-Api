package com.cibertec.ApiSentinela.emergencyEvent.mapper;

import com.cibertec.ApiSentinela.emergencyEvent.model.type.EmergencyEventStatus;
import org.springframework.stereotype.Component;

import com.cibertec.ApiSentinela.emergencyEvent.dtos.request.CreateEmergencyEventRequest;
import com.cibertec.ApiSentinela.emergencyEvent.dtos.response.EmergencyEventResponse;
import com.cibertec.ApiSentinela.emergencyEvent.model.EmergencyEvent;

@Component
public class EmergencyEventMapper {

    // Convierte entidad a response
    public EmergencyEventResponse toResponse(EmergencyEvent event) {
        Long userId = event.getUser() != null ? event.getUser().getId() : null;

        return new EmergencyEventResponse(
            event.getId(),
            userId,
            event.getStatus(),
            event.getActivatedAt(),
            event.getClosedAt()
        );
    }

    // Convierte request a entidad
    public EmergencyEvent toEntity(CreateEmergencyEventRequest request) {
        EmergencyEvent event = new EmergencyEvent();
        // La app no envía status al crear: un evento nuevo siempre nace ACTIVE
        event.setStatus(request.status() != null ? request.status() : EmergencyEventStatus.ACTIVE);
        // NOTA: User se asigna en el service, no aquí
        return event;
    }
}

package com.cibertec.ApiSentinela.emergencyMedia.mapper;

import org.springframework.stereotype.Component;

import com.cibertec.ApiSentinela.emergencyMedia.dtos.request.CreateEmergencyMediaRequest;
import com.cibertec.ApiSentinela.emergencyMedia.dtos.response.EmergencyMediaResponse;
import com.cibertec.ApiSentinela.emergencyMedia.model.EmergencyMedia;

@Component
public class EmergencyMediaMapper {

    // Convierte entidad a response
    public EmergencyMediaResponse toResponse(EmergencyMedia media) {
        Long emergencyEventId = media.getEmergencyEvent() != null ? media.getEmergencyEvent().getId() : null;

        return new EmergencyMediaResponse(
            media.getId(),
            emergencyEventId,
            media.getMediaType(),
            media.getStorageUrl()
        );
    }

    // Convierte request a entidad
    public EmergencyMedia toEntity(CreateEmergencyMediaRequest request) {
        EmergencyMedia media = new EmergencyMedia();
        media.setMediaType(request.mediaType());
        media.setStorageUrl(request.storageUrl());
        // emergencyEvent se asigna en el service
        return media;
    }
}

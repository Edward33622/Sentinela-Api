package com.cibertec.ApiSentinela.emergencyMedia.dtos.response;

import com.cibertec.ApiSentinela.emergencyMedia.model.type.EmergencyMediaStatus;

public record EmergencyMediaResponse(

    Long id,
    Long emergencyEventId,
    EmergencyMediaStatus mediaType,
    String storageUrl
    
) {}

package com.cibertec.ApiSentinela.emergencyMedia.service;

import com.cibertec.ApiSentinela.emergencyMedia.dtos.request.CreateEmergencyMediaRequest;
import com.cibertec.ApiSentinela.emergencyMedia.dtos.response.EmergencyMediaResponse;
import com.cibertec.ApiSentinela.emergencyMedia.mapper.EmergencyMediaMapper;
import com.cibertec.ApiSentinela.emergencyMedia.model.EmergencyMedia;
import com.cibertec.ApiSentinela.emergencyMedia.repository.EmergencyMediaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmergencyMediaService {

    private final EmergencyMediaRepository emergencyMediaRepository;
    private final EmergencyMediaMapper emergencyMediaMapper;

   
    @Transactional
    public EmergencyMediaResponse createMedia(CreateEmergencyMediaRequest request) {
        EmergencyMedia media = emergencyMediaMapper.toEntity(request);
        EmergencyMedia savedMedia = emergencyMediaRepository.save(media);
        return emergencyMediaMapper.toResponse(savedMedia);
    }

    // =============================================================
    // UPDATE MEDIA
    // =============================================================
   /*
    @Transactional
    public EmergencyMediaResponse updateMedia(Long mediaId, UpdateEmergencyMediaRequest request) {
        EmergencyMedia media = emergencyMediaRepository.findById(mediaId)
            // Asociar evento requerido (FK NOT NULL)
            EmergencyEvent event = emergencyEventRepository.findById(request.emergencyEventId())
                    .orElseThrow(() -> new RuntimeException("EmergencyEvent not found: " + request.emergencyEventId()));
            media.setEmergencyEvent(event);
                .orElseThrow(() -> new RuntimeException("Media not found"));

        media.setUrl(request.url());
        media.setMediaType(request.mediaType());
        media.setDescription(request.description());

        EmergencyMedia updated = emergencyMediaRepository.save(media);
        return emergencyMediaMapper.toResponse(updated);
    }
    */
   
    @Transactional
    public EmergencyMediaResponse getMediaById(Long mediaId) {
        EmergencyMedia media = emergencyMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        return emergencyMediaMapper.toResponse(media);
    }

  
    @Transactional
    public List<EmergencyMediaResponse> getMediaByEvent(Long eventId) {
        return emergencyMediaRepository.findByEmergencyEventId(eventId)
                .stream()
                .map(emergencyMediaMapper::toResponse)
                .collect(Collectors.toList());
    }

 
    @Transactional
    public List<EmergencyMediaResponse> getMediaByType(String mediaType) {
        return emergencyMediaRepository.findByMediaType(mediaType)
                .stream()
                .map(emergencyMediaMapper::toResponse)
                .collect(Collectors.toList());
    }


 
    @Transactional
    public void deleteMedia(Long mediaId) {
        EmergencyMedia media = emergencyMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        emergencyMediaRepository.delete(media);
    }
}

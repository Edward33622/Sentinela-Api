package com.cibertec.ApiSentinela.emergencyLocation.service;

import com.cibertec.ApiSentinela.emergencyEvent.model.EmergencyEvent;
import com.cibertec.ApiSentinela.emergencyEvent.repository.EmergencyEventRepository;
import com.cibertec.ApiSentinela.emergencyLocation.dtos.request.CreateEmergencyLocationRequest;
import com.cibertec.ApiSentinela.emergencyLocation.dtos.response.EmergencyLocationResponse;
import com.cibertec.ApiSentinela.emergencyLocation.mapper.EmergencyLocationMapper;
import com.cibertec.ApiSentinela.emergencyLocation.model.EmergencyLocation;
import com.cibertec.ApiSentinela.emergencyLocation.repository.EmergencyLocationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmergencyLocationService {

    private final EmergencyLocationRepository emergencyLocationRepository;
    private final EmergencyLocationMapper emergencyLocationMapper;
    private final EmergencyEventRepository emergencyEventRepository;

    @Transactional
    public EmergencyLocationResponse createLocation(CreateEmergencyLocationRequest request) {
        EmergencyLocation location = emergencyLocationMapper.toEntity(request);

        // Asociar el evento de emergencia (FK obligatoria)
        EmergencyEvent event = emergencyEventRepository.findById(request.emergencyEventId())
                .orElseThrow(() -> new RuntimeException("EmergencyEvent not found: " + request.emergencyEventId()));
        location.setEmergencyEvent(event);

        EmergencyLocation saved = emergencyLocationRepository.save(location);
        return emergencyLocationMapper.toResponse(saved);
    }

    // ------------------------------------------------------------
    // UPDATE (comentado por ahora)
    // ------------------------------------------------------------
    /*
    @Transactional
    public EmergencyLocationResponse updateLocation(Long locationId, UpdateEmergencyLocationRequest request) {
        EmergencyLocation location = emergencyLocationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());

        EmergencyLocation updated = emergencyLocationRepository.save(location);
        return emergencyLocationMapper.toResponse(updated);
    }
    */

    @Transactional
    public EmergencyLocationResponse getLocationById(Long locationId) {
        EmergencyLocation location = emergencyLocationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));
        return emergencyLocationMapper.toResponse(location);
    }

 
    @Transactional
    public List<EmergencyLocationResponse> getAllLocations() {
        return emergencyLocationRepository.findAll()
                .stream()
                .map(emergencyLocationMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<EmergencyLocationResponse> getLocationsByEvent(Long emergencyEventId) {
        return emergencyLocationRepository.findByEmergencyEventId(emergencyEventId)
                .stream()
                .map(emergencyLocationMapper::toResponse)
                .collect(Collectors.toList());
    }

    
    @Transactional
    public void deleteLocation(Long locationId) {
        if (!emergencyLocationRepository.existsById(locationId)) {
            throw new RuntimeException("Location not found");
        }
        emergencyLocationRepository.deleteById(locationId);
    }
}

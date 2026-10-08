package com.cibertec.ApiSentinela.emergencyEvent.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cibertec.ApiSentinela.emergencyEvent.dtos.request.CreateEmergencyEventRequest;
import com.cibertec.ApiSentinela.emergencyEvent.dtos.response.EmergencyEventResponse;
import com.cibertec.ApiSentinela.emergencyEvent.mapper.EmergencyEventMapper;
import com.cibertec.ApiSentinela.emergencyEvent.model.EmergencyEvent;
import com.cibertec.ApiSentinela.emergencyEvent.model.type.EmergencyEventStatus;
import com.cibertec.ApiSentinela.emergencyEvent.repository.EmergencyEventRepository;
import com.cibertec.ApiSentinela.user.model.User;
import com.cibertec.ApiSentinela.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmergencyEventService {

    private final EmergencyEventRepository emergencyEventRepository;
    private final EmergencyEventMapper emergencyEventMapper;
    private final UserRepository userRepository;

    @Transactional
    public EmergencyEventResponse createEvent(CreateEmergencyEventRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmergencyEvent event = emergencyEventMapper.toEntity(request);
        event.setUser(user);

        EmergencyEvent savedEvent = emergencyEventRepository.save(event);
        return emergencyEventMapper.toResponse(savedEvent);
    }

    @Transactional
    public EmergencyEventResponse getEventById(Long id) {
        EmergencyEvent event = emergencyEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return emergencyEventMapper.toResponse(event);
    }

    @Transactional
    public List<EmergencyEventResponse> getEventsByUser(Long userId) {
        return emergencyEventRepository.findByUserId(userId)
                .stream()
                .map(emergencyEventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<EmergencyEventResponse> getAllEvents() {
        return emergencyEventRepository.findAll()
                .stream()
                .map(emergencyEventMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmergencyEventResponse closeEvent(Long id) {
        EmergencyEvent event = emergencyEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        
        event.setClosedAt(java.time.LocalDateTime.now());
        event.setStatus(EmergencyEventStatus.CLOSED);

        EmergencyEvent updated = emergencyEventRepository.save(event);
        return emergencyEventMapper.toResponse(updated);
    }

@Transactional(readOnly = true)
public List<EmergencyEventResponse> getActiveEvents() {
    return emergencyEventRepository
            .findByStatus(EmergencyEventStatus.ACTIVE)
            .stream()
            .map(emergencyEventMapper::toResponse)
            .collect(Collectors.toList());
}

@Transactional(readOnly = true)
public List<EmergencyEventResponse> getResolvedEvents() {
    return emergencyEventRepository
            .findByStatus(EmergencyEventStatus.CLOSED)
            .stream()
            .map(emergencyEventMapper::toResponse)
            .collect(Collectors.toList());
}



}

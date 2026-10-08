package com.cibertec.ApiSentinela.emergencyEvent.controller;

import com.cibertec.ApiSentinela.emergencyEvent.dtos.request.CreateEmergencyEventRequest;
import com.cibertec.ApiSentinela.emergencyEvent.dtos.response.EmergencyEventResponse;
import com.cibertec.ApiSentinela.emergencyEvent.service.EmergencyEventService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-events")
@RequiredArgsConstructor
public class EmergencyEventController {

    private final EmergencyEventService emergencyEventService;

  
    @Operation(summary = "Create a new emergency event")
    @PostMapping
    public ResponseEntity<EmergencyEventResponse> createEvent(
            @RequestBody CreateEmergencyEventRequest request) {

        EmergencyEventResponse response = emergencyEventService.createEvent(request);
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------
    // Actualizar evento por ID
    // ------------------------------------------------------------
    /*
    @Operation(summary = "Update emergency event by ID")
    @PutMapping("/{id}")
    public ResponseEntity<EmergencyEventResponse> updateEvent(
            @PathVariable Long id,
            @RequestBody UpdateEmergencyEventRequest request) {

        EmergencyEventResponse response = emergencyEventService.updateEvent(id, request);
        return ResponseEntity.ok(response);
    }
     */

    @Operation(summary = "Get emergency event by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyEventResponse> getEventById(@PathVariable Long id) {

        EmergencyEventResponse response = emergencyEventService.getEventById(id);
        return ResponseEntity.ok(response);
    }

  
    @Operation(summary = "Get all emergency events")
    @GetMapping
    public ResponseEntity<List<EmergencyEventResponse>> getAllEvents() {

        List<EmergencyEventResponse> list = emergencyEventService.getAllEvents();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get emergency events by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EmergencyEventResponse>> getEventsByUser(
            @PathVariable Long userId) {

        List<EmergencyEventResponse> list = emergencyEventService.getEventsByUser(userId);
        return ResponseEntity.ok(list);
    }

 
    @Operation(summary = "Get active (unresolved) emergency events")
    @GetMapping("/active")
    public ResponseEntity<List<EmergencyEventResponse>> getActiveEvents() {

        List<EmergencyEventResponse> list = emergencyEventService.getActiveEvents();
        return ResponseEntity.ok(list);
    }


    @Operation(summary = "Get resolved emergency events")
    @GetMapping("/resolved")
    public ResponseEntity<List<EmergencyEventResponse>> getResolvedEvents() {

        List<EmergencyEventResponse> list = emergencyEventService.getResolvedEvents();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Mark emergency event as resolved")
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<EmergencyEventResponse> resolveEvent(@PathVariable Long id) {

        EmergencyEventResponse response = emergencyEventService.closeEvent(id);
        return ResponseEntity.ok(response);
    }
}

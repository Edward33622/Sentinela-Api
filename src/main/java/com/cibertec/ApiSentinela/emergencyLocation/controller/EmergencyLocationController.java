package com.cibertec.ApiSentinela.emergencyLocation.controller;

import com.cibertec.ApiSentinela.emergencyLocation.dtos.request.CreateEmergencyLocationRequest;
import com.cibertec.ApiSentinela.emergencyLocation.dtos.response.EmergencyLocationResponse;
import com.cibertec.ApiSentinela.emergencyLocation.service.EmergencyLocationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-locations")
@RequiredArgsConstructor
public class EmergencyLocationController {

    private final EmergencyLocationService emergencyLocationService;


    @Operation(summary = "Create a new emergency location")
    @PostMapping
        public ResponseEntity<EmergencyLocationResponse> createLocation(
            @Valid @RequestBody CreateEmergencyLocationRequest request) {

        EmergencyLocationResponse response = emergencyLocationService.createLocation(request);
        return ResponseEntity.ok(response);
    }

    // ------------------------------------------------------------
    // UPDATE
    // ------------------------------------------------------------
    /*
    @Operation(summary = "Update emergency location by ID")
    @PutMapping("/{id}")
    public ResponseEntity<EmergencyLocationResponse> updateLocation(
            @PathVariable Long id,
            @RequestBody UpdateEmergencyLocationRequest request) {

        EmergencyLocationResponse response = emergencyLocationService.updateLocation(id, request);
        return ResponseEntity.ok(response);
    }
         */

   
    @Operation(summary = "Get emergency location by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyLocationResponse> getLocationById(@PathVariable Long id) {
        EmergencyLocationResponse response = emergencyLocationService.getLocationById(id);
        return ResponseEntity.ok(response);
    }

  
    @Operation(summary = "Get all emergency locations")
    @GetMapping
    public ResponseEntity<List<EmergencyLocationResponse>> getAllLocations() {
        List<EmergencyLocationResponse> locations = emergencyLocationService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @Operation(summary = "Get locations by emergency event ID")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EmergencyLocationResponse>> getLocationsByEvent(
            @PathVariable Long eventId) {

        List<EmergencyLocationResponse> locations = emergencyLocationService.getLocationsByEvent(eventId);
        return ResponseEntity.ok(locations);
    }

   
    @Operation(summary = "Delete emergency location by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        emergencyLocationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}

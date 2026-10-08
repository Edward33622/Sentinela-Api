package com.cibertec.ApiSentinela.emergencyMedia.controller;

import com.cibertec.ApiSentinela.emergencyMedia.dtos.request.CreateEmergencyMediaRequest;
import com.cibertec.ApiSentinela.emergencyMedia.dtos.response.EmergencyMediaResponse;
import com.cibertec.ApiSentinela.emergencyMedia.service.EmergencyMediaService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency-media")
@RequiredArgsConstructor
public class EmergencyMediaController {

    private final EmergencyMediaService emergencyMediaService;


    @Operation(summary = "Create a new emergency media record")
    @PostMapping
        public ResponseEntity<EmergencyMediaResponse> createMedia(
            @Valid @RequestBody CreateEmergencyMediaRequest request) {

        EmergencyMediaResponse response = emergencyMediaService.createMedia(request);
        return ResponseEntity.ok(response);
    }

 
    @Operation(summary = "Get media by ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyMediaResponse> getMediaById(@PathVariable Long id) {
        EmergencyMediaResponse response = emergencyMediaService.getMediaById(id);
        return ResponseEntity.ok(response);
    }

   
    @Operation(summary = "Get all media related to an emergency event")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EmergencyMediaResponse>> getMediaByEvent(@PathVariable Long eventId) {
        List<EmergencyMediaResponse> response = emergencyMediaService.getMediaByEvent(eventId);
        return ResponseEntity.ok(response);
    }

 
    @Operation(summary = "Get media by type (PHOTO, VIDEO, AUDIO)")
    @GetMapping("/type/{mediaType}")
    public ResponseEntity<List<EmergencyMediaResponse>> getMediaByType(@PathVariable String mediaType) {
        List<EmergencyMediaResponse> response = emergencyMediaService.getMediaByType(mediaType);
        return ResponseEntity.ok(response);
    }

    // =============================================================
    // UPDATE
    // =============================================================
    /*
    @Operation(summary = "Update media by ID")
    @PutMapping("/{id}")
    public ResponseEntity<EmergencyMediaResponse> updateMedia(
            @PathVariable Long id,
            @RequestBody UpdateEmergencyMediaRequest request) {

        EmergencyMediaResponse response = emergencyMediaService.updateMedia(id, request);
        return ResponseEntity.ok(response);
    }
         */

 
    @Operation(summary = "Delete media by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Long id) {
        emergencyMediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }
}

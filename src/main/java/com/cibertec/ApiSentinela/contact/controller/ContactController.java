package com.cibertec.ApiSentinela.contact.controller;

import com.cibertec.ApiSentinela.contact.dtos.request.CreateContactRequest;
import com.cibertec.ApiSentinela.contact.dtos.response.ContactResponse;
import com.cibertec.ApiSentinela.contact.service.ContactService;
import com.cibertec.ApiSentinela.user.model.User;
import com.cibertec.ApiSentinela.user.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.cibertec.ApiSentinela.contact.dtos.response.EmergencyAlertResult;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final UserRepository userRepository;

    @Operation(summary = "Create a new contact")
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(@RequestBody CreateContactRequest request) {
        ContactResponse response = contactService.createContact(request);
        return ResponseEntity.ok(response);
    }

    /*
    @Operation(summary = "Update contact by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long id,
            @RequestBody UpdateContactRequest request) {

        ContactResponse response = contactService.updateContact(id, request);
        return ResponseEntity.ok(response);
    }
    */

    // inicio - sobre twilio

    @PostMapping("/emergency/alert")
    public ResponseEntity<EmergencyAlertResult> sendEmergencyAlert(@RequestParam String location, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmergencyAlertResult result = contactService.sendEmergencyWhatsApp(user, location);

        if (result.getTotalRecipients() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        if (result.getSent() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.ok(result);
    }

    // fin - sobre twilio

    @Operation(summary = "Get contact by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id) {
        ContactResponse response = contactService.getContactById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all contacts")
    @GetMapping
    public ResponseEntity<List<ContactResponse>> getAllContacts() {
        List<ContactResponse> list = contactService.getAllContacts();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get contacts by User ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ContactResponse>> getContactsByUser(@PathVariable Long userId) {
        List<ContactResponse> list = contactService.getContactsByUser(userId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Delete a contact")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}

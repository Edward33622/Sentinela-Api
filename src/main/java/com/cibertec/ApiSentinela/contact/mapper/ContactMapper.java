package com.cibertec.ApiSentinela.contact.mapper;

import org.springframework.stereotype.Component;

import com.cibertec.ApiSentinela.contact.dtos.request.CreateContactRequest;
import com.cibertec.ApiSentinela.contact.dtos.response.ContactResponse;
import com.cibertec.ApiSentinela.contact.model.Contact;

@Component
public class ContactMapper {

    // Convierte entidad a response
    public ContactResponse toResponse(Contact contact) {
        Long userId = contact.getUser() != null ? contact.getUser().getId() : null;

        return new ContactResponse(
            contact.getId(),
            userId,
            contact.getFullName(),
            contact.getPhoneNumber(),
            contact.getEmail(),
            contact.getEnableWhatsapp(),
            contact.getEmergencyContact()
        );
    }

    // Convierte request a entidad
    public Contact toEntity(CreateContactRequest request) {
        Contact contact = new Contact();
        contact.setFullName(request.fullName());
        contact.setPhoneNumber(request.phoneNumber());
        contact.setEmail(request.email());
        contact.setEnableWhatsapp(request.enableWhatsapp() != null ? request.enableWhatsapp() : true);
        // Marcar como contacto de emergencia por defecto para uso en alertas
        contact.setEmergencyContact(true);
        // user se asigna en el service
        return contact;
    }
}

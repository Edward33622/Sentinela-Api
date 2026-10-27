package com.cibertec.ApiSentinela.contact.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.cibertec.ApiSentinela.contact.dtos.response.EmergencyAlertResult;
import com.cibertec.ApiSentinela.contact.dtos.response.RecipientResult;
import com.cibertec.ApiSentinela.subscription.controller.SubscriptionController;

import com.cibertec.ApiSentinela.contact.dtos.request.CreateContactRequest;
import com.cibertec.ApiSentinela.contact.dtos.response.ContactResponse;
import com.cibertec.ApiSentinela.contact.mapper.ContactMapper;
import com.cibertec.ApiSentinela.contact.model.Contact;
import com.cibertec.ApiSentinela.contact.repository.ContactRepository;
import com.cibertec.ApiSentinela.user.model.User;
import com.cibertec.ApiSentinela.user.repository.UserRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final UserRepository userRepository;

    // inicio - sobre twilio
    @Value("${twilio.phoneNumber}")
    private String fromPhone;
    @Value("${twilio.defaultCountryCode:+51}")
    private String defaultCountryCode;

    
    public EmergencyAlertResult sendEmergencyWhatsApp(User user, String location) {

        // Obtener todos los contactos de emergencia del usuario
        List<Contact> contacts = contactRepository.findByUserAndEmergencyContactTrue(user);

        // Filtrar solo contactos con WhatsApp habilitado
        contacts = contacts.stream()
            .filter(c -> Boolean.TRUE.equals(c.getEnableWhatsapp()))
            .toList();

        EmergencyAlertResult result = new EmergencyAlertResult();
        result.setTotalRecipients(contacts.size());
        result.setAttempted(0);
        result.setSent(0);
        result.setFailed(0);
        if (contacts.isEmpty()) {
            return result;
        }

        String message = buildEmergencyMessage(user, location);

        boolean userHasSmsBackup = SubscriptionController.hasSmsBackup(user.getId());
        String smsFromPhone = fromPhone != null ? fromPhone.replace("whatsapp:", "").trim() : null;

        for (Contact contact : contacts) {
            if (contact.getPhoneNumber() != null) {
                String normalized = formatPhoneNumber(contact.getPhoneNumber());
                String to = "whatsapp:" + normalized;
                RecipientResult recipient = new RecipientResult(contact.getFullName(), contact.getPhoneNumber(), normalized);
                result.setAttempted(result.getAttempted() + 1);

                boolean whatsAppSent = false;
                System.out.println("Enviando mensaje WhatsApp a " + normalized + "...");
                try {
                    Message msg = Message.creator(
                            new PhoneNumber(to),
                            new PhoneNumber(fromPhone),
                            message
                    ).create();

                    whatsAppSent = true;
                    recipient.setSent(true);
                    recipient.setSid(msg.getSid());
                    System.out.println("Mensaje WhatsApp enviado a " + normalized + " SID: " + msg.getSid());
                } catch (Exception e) {
                    System.err.println("Error enviando WhatsApp a " + normalized + ": " + e.getMessage());
                    recipient.setError("WhatsApp: " + e.getMessage());
                }

                // Despacho de SMS de respaldo para planes PRO / FAMILIA o como fallback si WhatsApp falló
                if (userHasSmsBackup || !whatsAppSent) {
                    if (smsFromPhone != null && !smsFromPhone.isBlank()) {
                        try {
                            System.out.println("Enviando SMS de respaldo a " + normalized + "...");
                            Message smsMsg = Message.creator(
                                    new PhoneNumber(normalized),
                                    new PhoneNumber(smsFromPhone),
                                    message
                            ).create();
                            System.out.println("SMS de respaldo enviado a " + normalized + " SID: " + smsMsg.getSid());
                            if (!whatsAppSent) {
                                recipient.setSent(true);
                                recipient.setSid(smsMsg.getSid());
                                recipient.setError(null);
                            }
                        } catch (Exception smsEx) {
                            System.err.println("Aviso SMS respaldo (" + normalized + "): " + smsEx.getMessage());
                            if (!whatsAppSent) {
                                recipient.setError("WhatsApp falló y SMS: " + smsEx.getMessage());
                            }
                        }
                    }
                }

                if (recipient.isSent()) {
                    result.setSent(result.getSent() + 1);
                } else {
                    result.setFailed(result.getFailed() + 1);
                }
                result.addRecipient(recipient);
            }
        }

        return result;
    }

    private String buildEmergencyMessage(User user, String location) {
        return "🚨 ALERTA DE EMERGENCIA 🚨\n\n"
                + "Usuario: " + user.getFullName() + "\n"
                + "Email: " + user.getEmail() + "\n"
                + "Ubicación: " + location + "\n"
                + "Hora: " + java.time.LocalDateTime.now();
    }

    private String formatPhoneNumber(String number) {
        if (number == null || number.isBlank()) return number;
        String trimmed = number.trim();
        boolean hadPlus = trimmed.startsWith("+");
        // Mantener solo dígitos
        String digits = trimmed.replaceAll("[^0-9]", "");
        String code = defaultCountryCode != null ? defaultCountryCode : "+51";
        String codeDigits = code.replaceAll("[^0-9]", "");

        if (digits.isEmpty()) return code + digits;

        // Si ya tenía + o ya incluye el código de país al inicio, solo asegurar el +
        if (hadPlus || (!codeDigits.isEmpty() && digits.startsWith(codeDigits))) {
            return "+" + digits;
        }
        // Agregar código por defecto
        return "+" + codeDigits + digits;
    }
    // fin - twilio
    @Transactional
    public ContactResponse createContact(CreateContactRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Contact contact = contactMapper.toEntity(request);
        contact.setUser(user);

        Contact saved = contactRepository.save(contact);
        return contactMapper.toResponse(saved);
    }

    @Transactional
    public List<ContactResponse> getContactsByUser(Long userId) {
        return contactRepository.findByUserId(userId)
                .stream()
                .map(contactMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contactRepository.delete(contact);
    }

    @Transactional
public ContactResponse getContactById(Long contactId) {
    Contact contact = contactRepository.findById(contactId)
            .orElseThrow(() -> new RuntimeException("Contact not found"));
    return contactMapper.toResponse(contact);
}

@Transactional
public List<ContactResponse> getAllContacts() {
    return contactRepository.findAll()
            .stream()
            .map(contactMapper::toResponse)
            .collect(Collectors.toList());
}

}

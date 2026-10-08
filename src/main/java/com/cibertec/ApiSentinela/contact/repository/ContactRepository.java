package com.cibertec.ApiSentinela.contact.repository;

import com.cibertec.ApiSentinela.contact.model.Contact;
import com.cibertec.ApiSentinela.user.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    // Buscar contactos por usuario
    List<Contact> findByUserId(Long userId);

    // Opcional: buscar contactos por correo electrónico
    List<Contact> findByEmail(String email);

    // Trae todos los contactos marcados como emergencia de un usuario
    List<Contact> findByUserAndEmergencyContactTrue(User user);

    // Opcional: traer solo uno por email (para demo/testing)
    List<Contact> findByUserAndEmergencyContactTrueAndEmail(User user, String email);

}

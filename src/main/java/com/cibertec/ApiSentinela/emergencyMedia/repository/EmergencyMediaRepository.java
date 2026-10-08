package com.cibertec.ApiSentinela.emergencyMedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.ApiSentinela.emergencyMedia.model.EmergencyMedia;

import java.util.List;

@Repository
public interface EmergencyMediaRepository extends JpaRepository<EmergencyMedia, Long> {

    // Obtener todos los medios asociados a un evento de emergencia
    List<EmergencyMedia> findByEmergencyEventId(Long emergencyEventId);

    // Buscar por tipo de medio (AUDIO, VIDEO, PHOTO)
    List<EmergencyMedia> findByMediaType(String mediaType);
}

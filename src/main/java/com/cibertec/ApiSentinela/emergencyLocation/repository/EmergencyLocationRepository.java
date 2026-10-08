package com.cibertec.ApiSentinela.emergencyLocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.ApiSentinela.emergencyLocation.model.EmergencyLocation;

import java.util.List;

@Repository
public interface EmergencyLocationRepository extends JpaRepository<EmergencyLocation, Long> {

    // Obtener todas las ubicaciones asociadas a un evento de emergencia
    List<EmergencyLocation> findByEmergencyEventId(Long emergencyEventId);
}

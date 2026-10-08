package com.cibertec.ApiSentinela.emergencyEvent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.ApiSentinela.emergencyEvent.model.EmergencyEvent;
import com.cibertec.ApiSentinela.emergencyEvent.model.type.EmergencyEventStatus;

import java.util.List;

@Repository
public interface EmergencyEventRepository extends JpaRepository<EmergencyEvent, Long> {

    // Buscar eventos de emergencia por usuario
    List<EmergencyEvent> findByUserId(Long userId);

    List<EmergencyEvent> findByStatus(EmergencyEventStatus status);

}

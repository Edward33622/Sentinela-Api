package com.cibertec.ApiSentinela.emergencyEvent.model;

import com.cibertec.ApiSentinela.emergencyEvent.model.type.EmergencyEventStatus;
import com.cibertec.ApiSentinela.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "emergency_events")
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmergencyEventStatus status;

    @Column(name = "activated_at", nullable = false)
    private LocalDateTime activatedAt = LocalDateTime.now();

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

}

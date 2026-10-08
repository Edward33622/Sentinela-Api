package com.cibertec.ApiSentinela.contact.model;

import com.cibertec.ApiSentinela.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(name = "enable_whatsapp")
    private Boolean enableWhatsapp = true;

    @Column(name = "is_emergency_contact")
    private Boolean emergencyContact = false;

}

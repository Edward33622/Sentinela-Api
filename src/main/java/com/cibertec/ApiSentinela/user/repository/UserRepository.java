package com.cibertec.ApiSentinela.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cibertec.ApiSentinela.user.model.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar usuario por email (útil para login y autenticación)
    Optional<User> findByEmail(String email);

    // Verificar si un email ya existe
    boolean existsByEmail(String email);
}

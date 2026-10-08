package com.cibertec.ApiSentinela.user.mapper;

import org.springframework.stereotype.Component;

import com.cibertec.ApiSentinela.user.dtos.request.CreateUserRequest;
import com.cibertec.ApiSentinela.user.dtos.response.UserResponse;
import com.cibertec.ApiSentinela.user.model.User;
import com.cibertec.ApiSentinela.user.model.type.UserStatus;

@Component
public class UserMapper {

    // Entidad -> Response
    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            user.getStatus() != null ? user.getStatus() : UserStatus.INACTIVE // por seguridad
        );
    }

    // Request -> Entidad
    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        user.setStatus(UserStatus.ACTIVE); // asignamos activo por defecto
        return user;
    }
}

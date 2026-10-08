package com.cibertec.ApiSentinela.user.service;

import com.cibertec.ApiSentinela.user.dtos.request.CreateUserRequest;
import com.cibertec.ApiSentinela.user.dtos.request.LoginRequest;
import com.cibertec.ApiSentinela.user.dtos.request.UpdateUserRequest;
import com.cibertec.ApiSentinela.user.dtos.response.AuthResponse;
import com.cibertec.ApiSentinela.user.dtos.response.UserResponse;
import com.cibertec.ApiSentinela.user.mapper.UserMapper;
import com.cibertec.ApiSentinela.user.model.User;
import com.cibertec.ApiSentinela.user.model.type.UserStatus;
import com.cibertec.ApiSentinela.user.repository.UserRepository;
import com.cibertec.ApiSentinela.user.security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;

   @Transactional
public AuthResponse login(LoginRequest request) {
    User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
        throw new RuntimeException("Invalid credentials");
    }

    // Genera token pasando email y id del usuario
    String token = jwtUtil.generateToken(user.getEmail(), user.getId());

    return new AuthResponse(token, user.getId(), user.getEmail());
}


    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        // Hashear password antes de guardar
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPhone(request.phone());
        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    @Transactional
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.INACTIVE);

        User updatedUser = userRepository.save(user);
        return userMapper.toResponse(updatedUser);
    }

  
}

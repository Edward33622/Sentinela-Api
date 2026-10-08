package com.cibertec.ApiSentinela.user.controller;

import com.cibertec.ApiSentinela.user.dtos.request.CreateUserRequest;
import com.cibertec.ApiSentinela.user.dtos.request.UpdateUserRequest;
import com.cibertec.ApiSentinela.user.dtos.response.UserResponse;
import com.cibertec.ApiSentinela.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService usersService;
   
    @Operation(summary = "Create a new user")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        UserResponse response = usersService.createUser(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UserResponse response = usersService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = usersService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Deactivate user by ID")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Long id) {
        UserResponse response = usersService.deactivateUser(id);
        return ResponseEntity.ok(response);
    }
}

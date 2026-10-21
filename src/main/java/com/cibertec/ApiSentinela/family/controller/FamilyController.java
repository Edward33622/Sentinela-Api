package com.cibertec.ApiSentinela.family.controller;

import com.cibertec.ApiSentinela.family.dtos.*;
import com.cibertec.ApiSentinela.user.model.User;
import com.cibertec.ApiSentinela.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.ApiSentinela.subscription.controller.SubscriptionController;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
@Tag(name = "Círculo Familiar", description = "Endpoints para gestión de círculos familiares, ubicación en vivo y monitoreo de batería")
public class FamilyController {

    private final UserRepository userRepository;

    // Almacenamiento en memoria para gestión concurrente en tiempo real
    private final ConcurrentHashMap<String, FamilyGroupDto> groupsByCode = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, String> userToGroupCode = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, FamilyMemberDto> memberStates = new ConcurrentHashMap<>();

    @Operation(summary = "Crear un nuevo círculo familiar")
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateFamilyGroupRequest request) {
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId es requerido"));
        }

        // Validación estricta de monetización: Solo usuarios con Plan FAMILIA pueden crear un círculo
        if (!SubscriptionController.isUserFamily(request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", "PLAN_UPGRADE_REQUIRED",
                            "message", "Crear y administrar un Círculo Familiar requiere el Plan Sentinela Familia (S/. 24.90/mes). Por favor actualiza tu plan en la sección de Suscripciones."
                    ));
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);
        String userName = user != null ? user.getFullName() : "Usuario " + request.getUserId();
        String userPhone = user != null ? user.getPhone() : "";

        // Generar código único de 6 caracteres ej: FAM-8492
        String code;
        do {
            int randomNum = 1000 + new Random().nextInt(9000);
            code = "FAM-" + randomNum;
        } while (groupsByCode.containsKey(code));

        String groupName = (request.getGroupName() != null && !request.getGroupName().trim().isEmpty())
                ? request.getGroupName().trim()
                : "Familia de " + userName;

        FamilyMemberDto adminMember = memberStates.computeIfAbsent(request.getUserId(), id ->
                FamilyMemberDto.builder()
                        .userId(id)
                        .fullName(userName)
                        .phone(userPhone)
                        .batteryLevel(100)
                        .isCharging(false)
                        .isEmergencyActive(false)
                        .currentStatus("🟢 En Espera")
                        .lastPing(LocalDateTime.now())
                        .isAdmin(true)
                        .build()
        );
        adminMember.setIsAdmin(true);

        List<FamilyMemberDto> members = new ArrayList<>();
        members.add(adminMember);

        FamilyGroupDto group = FamilyGroupDto.builder()
                .groupCode(code)
                .groupName(groupName)
                .adminUserId(request.getUserId())
                .adminName(userName)
                .createdAt(LocalDateTime.now())
                .members(members)
                .build();

        groupsByCode.put(code, group);
        userToGroupCode.put(request.getUserId(), code);

        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @Operation(summary = "Unirse a un círculo familiar con código de invitación")
    @PostMapping("/join")
    public ResponseEntity<?> joinGroup(@RequestBody JoinFamilyGroupRequest request) {
        if (request.getUserId() == null || request.getGroupCode() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId y groupCode son requeridos"));
        }

        String normalizedCode = request.getGroupCode().trim().toUpperCase();
        FamilyGroupDto group = groupsByCode.get(normalizedCode);
        if (group == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Código de círculo familiar '" + normalizedCode + "' no existe. Verifica con el titular de la cuenta."));
        }

        // Validar límite de 5 miembros en el Plan Familia
        boolean yaEsMiembro = group.getMembers().stream().anyMatch(m -> m.getUserId().equals(request.getUserId()));
        if (!yaEsMiembro && group.getMembers().size() >= 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "El círculo familiar ya alcanzó el cupo máximo permitido (5 miembros)."));
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);
        String userName = user != null ? user.getFullName() : "Familiar " + request.getUserId();
        String userPhone = user != null ? user.getPhone() : "";

        if (!yaEsMiembro) {
            FamilyMemberDto newMember = memberStates.computeIfAbsent(request.getUserId(), id ->
                    FamilyMemberDto.builder()
                            .userId(id)
                            .fullName(userName)
                            .phone(userPhone)
                            .batteryLevel(100)
                            .isCharging(false)
                            .isEmergencyActive(false)
                            .currentStatus("🟢 Conectado")
                            .lastPing(LocalDateTime.now())
                            .isAdmin(false)
                            .build()
            );
            newMember.setIsAdmin(false);
            group.getMembers().add(newMember);
        }

        userToGroupCode.put(request.getUserId(), normalizedCode);
        return ResponseEntity.ok(group);
    }

    @Operation(summary = "Reportar ping en vivo de ubicación, batería y estado")
    @PostMapping("/ping")
    public ResponseEntity<?> sendPing(@RequestBody FamilyPingRequest request) {
        if (request.getUserId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "userId es requerido"));
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);
        String userName = user != null ? user.getFullName() : "Usuario " + request.getUserId();
        String userPhone = user != null ? user.getPhone() : "";

        FamilyMemberDto state = memberStates.computeIfAbsent(request.getUserId(), id ->
                FamilyMemberDto.builder()
                        .userId(id)
                        .fullName(userName)
                        .phone(userPhone)
                        .isAdmin(false)
                        .build()
        );

        if (request.getLatitude() != null) state.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) state.setLongitude(request.getLongitude());
        if (request.getBatteryLevel() != null) state.setBatteryLevel(request.getBatteryLevel());
        if (request.getIsCharging() != null) state.setIsCharging(request.getIsCharging());
        if (request.getIsEmergencyActive() != null) state.setIsEmergencyActive(request.getIsEmergencyActive());

        String status = (request.getStatusZone() != null && !request.getStatusZone().trim().isEmpty())
                ? request.getStatusZone()
                : (Boolean.TRUE.equals(request.getIsEmergencyActive()) ? "🚨 SOS ACTIVO" : "🟢 En Movimiento");
        state.setCurrentStatus(status);
        state.setLastPing(LocalDateTime.now());

        // Actualizar referencia en el grupo
        String code = userToGroupCode.get(request.getUserId());
        if (code != null) {
            FamilyGroupDto group = groupsByCode.get(code);
            if (group != null) {
                for (int i = 0; i < group.getMembers().size(); i++) {
                    if (group.getMembers().get(i).getUserId().equals(request.getUserId())) {
                        group.getMembers().set(i, state);
                        break;
                    }
                }
            }
        }

        return ResponseEntity.ok(state);
    }

    @Operation(summary = "Consultar el círculo familiar del usuario")
    @GetMapping("/my-group")
    public ResponseEntity<?> getMyGroup(@RequestParam Long userId) {
        String code = userToGroupCode.get(userId);
        if (code == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "El usuario no pertenece a ningún círculo familiar aún"));
        }

        FamilyGroupDto group = groupsByCode.get(code);
        if (group == null) {
            userToGroupCode.remove(userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "El grupo ya no existe"));
        }

        return ResponseEntity.ok(group);
    }

    @Operation(summary = "Obtener miembros del círculo familiar con ubicación y batería")
    @GetMapping("/members")
    public ResponseEntity<?> getMembers(@RequestParam Long userId) {
        String code = userToGroupCode.get(userId);
        if (code == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        FamilyGroupDto group = groupsByCode.get(code);
        if (group == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(group.getMembers());
    }

    @Operation(summary = "Salir del círculo familiar")
    @PostMapping("/leave")
    public ResponseEntity<?> leaveGroup(@RequestParam Long userId) {
        String code = userToGroupCode.remove(userId);
        if (code != null) {
            FamilyGroupDto group = groupsByCode.get(code);
            if (group != null) {
                group.getMembers().removeIf(m -> m.getUserId().equals(userId));
                if (group.getMembers().isEmpty()) {
                    groupsByCode.remove(code);
                }
            }
        }
        return ResponseEntity.ok(Map.of("message", "Has salido del círculo familiar"));
    }
}

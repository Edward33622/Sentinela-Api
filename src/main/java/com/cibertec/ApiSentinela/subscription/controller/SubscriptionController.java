package com.cibertec.ApiSentinela.subscription.controller;

import com.cibertec.ApiSentinela.subscription.dtos.SubscribeRequest;
import com.cibertec.ApiSentinela.subscription.dtos.SubscriptionPlanResponse;
import com.cibertec.ApiSentinela.subscription.dtos.SubscriptionStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Suscripciones", description = "Endpoints para planes de pago y suscripciones de Sentinela")
public class SubscriptionController {

    // Simulación en memoria por usuario para mantener estado si no hay tabla SQL modificada
    public static final ConcurrentHashMap<Long, SubscriptionStatusResponse> userSubscriptions = new ConcurrentHashMap<>();

    public static boolean isUserFamily(Long userId) {
        if (userId == null) return false;
        SubscriptionStatusResponse sub = userSubscriptions.get(userId);
        return sub != null && "FAMILIA".equalsIgnoreCase(sub.getPlanId());
    }

    public static boolean hasSmsBackup(Long userId) {
        if (userId == null) return false;
        SubscriptionStatusResponse sub = userSubscriptions.get(userId);
        return sub != null && (Boolean.TRUE.equals(sub.isHasSmsBackup()) || "PRO".equalsIgnoreCase(sub.getPlanId()) || "FAMILIA".equalsIgnoreCase(sub.getPlanId()));
    }

    @Operation(summary = "Obtener planes de suscripción disponibles")
    @GetMapping("/plans")
    public ResponseEntity<List<SubscriptionPlanResponse>> getAvailablePlans() {
        SubscriptionPlanResponse freePlan = SubscriptionPlanResponse.builder()
                .id("FREE")
                .name("FREE")
                .displayName("Sentinela Free")
                .monthlyPrice(0.0)
                .annualPrice(0.0)
                .currency("PEN")
                .badge("BÁSICO")
                .isPopular(false)
                .maxContacts(2)
                .aiQueriesPerMonth(120) // 4 consultas al día (~120 al mes)
                .hasSmsBackup(false)
                .hasSafeTripMode(false)
                .features(Arrays.asList(
                        "Botón SOS básico con sirena y modo discreto",
                        "Hasta 2 contactos de auxilio",
                        "4 análisis de ruta IA al día con Gemini",
                        "Alertas solo por WhatsApp"
                ))
                .build();

        SubscriptionPlanResponse proPlan = SubscriptionPlanResponse.builder()
                .id("PRO")
                .name("PRO")
                .displayName("Sentinela Pro")
                .monthlyPrice(9.90)
                .annualPrice(95.00) // Descuento anual
                .currency("PEN")
                .badge("MÁS POPULAR")
                .isPopular(true)
                .maxContacts(999)
                .aiQueriesPerMonth(9999)
                .hasSmsBackup(true)
                .hasSafeTripMode(true)
                .features(Arrays.asList(
                        "Todo lo de Free incluido",
                        "Contactos de auxilio ilimitados",
                        "Gemini AI ilimitado para rutas y riesgo",
                        "WhatsApp + SMS de respaldo con Twilio",
                        "Modo Acompañante Seguro con temporizador"
                ))
                .build();

        SubscriptionPlanResponse familiaPlan = SubscriptionPlanResponse.builder()
                .id("FAMILIA")
                .name("FAMILIA")
                .displayName("Sentinela Familia")
                .monthlyPrice(24.90)
                .annualPrice(239.00) // Descuento anual
                .currency("PEN")
                .badge("PROTECCIÓN TOTAL")
                .isPopular(false)
                .maxContacts(999)
                .aiQueriesPerMonth(9999)
                .hasSmsBackup(true)
                .hasSafeTripMode(true)
                .features(Arrays.asList(
                        "Todo lo de Pro incluido",
                        "Hasta 5 miembros protegidos",
                        "Panel familiar compartido en tiempo real",
                        "Geocercas automáticas (aviso de llegada)",
                        "Monitoreo de batería y estado de conexión"
                ))
                .build();

        return ResponseEntity.ok(Arrays.asList(freePlan, proPlan, familiaPlan));
    }

    @Operation(summary = "Obtener el estado de suscripción de un usuario")
    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionStatusResponse> getUserSubscription(@PathVariable Long userId) {
        SubscriptionStatusResponse status = userSubscriptions.computeIfAbsent(userId, id ->
                SubscriptionStatusResponse.builder()
                        .userId(id)
                        .planId("FREE")
                        .planDisplayName("Sentinela Free")
                        .status("ACTIVE")
                        .billingCycle("NONE")
                        .startedAt(LocalDateTime.now())
                        .expiresAt(null)
                        .maxContacts(2)
                        .aiQueriesPerMonth(3)
                        .aiQueriesUsed(0)
                        .hasSmsBackup(false)
                        .hasSafeTripMode(false)
                        .build()
        );
        return ResponseEntity.ok(status);
    }

    @Operation(summary = "Suscribirse o cambiar de plan")
    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionStatusResponse> subscribe(@RequestBody SubscribeRequest request) {
        String plan = request.getPlanId() != null ? request.getPlanId().toUpperCase() : "FREE";
        String cycle = request.getBillingCycle() != null ? request.getBillingCycle().toUpperCase() : "MONTHLY";

        String displayName = "Sentinela Free";
        int maxContacts = 2;
        int aiQueries = 3;
        boolean sms = false;
        boolean safeTrip = false;

        if ("PRO".equals(plan)) {
            displayName = "Sentinela Pro";
            maxContacts = 999;
            aiQueries = 9999;
            sms = true;
            safeTrip = true;
        } else if ("FAMILIA".equals(plan)) {
            displayName = "Sentinela Familia";
            maxContacts = 999;
            aiQueries = 9999;
            sms = true;
            safeTrip = true;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expires = "ANNUAL".equals(cycle) ? now.plusYears(1) : now.plusMonths(1);
        if ("FREE".equals(plan)) {
            expires = null;
        }

        SubscriptionStatusResponse updated = SubscriptionStatusResponse.builder()
                .userId(request.getUserId())
                .planId(plan)
                .planDisplayName(displayName)
                .status("ACTIVE")
                .billingCycle("FREE".equals(plan) ? "NONE" : cycle)
                .startedAt(now)
                .expiresAt(expires)
                .maxContacts(maxContacts)
                .aiQueriesPerMonth(aiQueries)
                .aiQueriesUsed(0)
                .hasSmsBackup(sms)
                .hasSafeTripMode(safeTrip)
                .build();

        userSubscriptions.put(request.getUserId(), updated);
        return ResponseEntity.ok(updated);
    }
}

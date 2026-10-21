package com.cibertec.ApiSentinela.subscription.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeRequest {
    private Long userId;
    private String planId; // "FREE", "PRO", "FAMILIA"
    private String billingCycle; // "MONTHLY" or "ANNUAL"
    private String paymentMethod; // "GOOGLE_PLAY", "YAPE_PLIN", "CARD"
}

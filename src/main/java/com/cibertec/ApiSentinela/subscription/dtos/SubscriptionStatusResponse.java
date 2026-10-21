package com.cibertec.ApiSentinela.subscription.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionStatusResponse {
    private Long userId;
    private String planId; // "FREE", "PRO", "FAMILIA"
    private String planDisplayName;
    private String status; // "ACTIVE"
    private String billingCycle; // "MONTHLY", "ANNUAL", "NONE"
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private Integer maxContacts;
    private Integer aiQueriesPerMonth;
    private Integer aiQueriesUsed;
    private boolean hasSmsBackup;
    private boolean hasSafeTripMode;
}

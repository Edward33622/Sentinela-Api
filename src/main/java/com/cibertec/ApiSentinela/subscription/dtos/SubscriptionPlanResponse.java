package com.cibertec.ApiSentinela.subscription.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponse {
    private String id;
    private String name;
    private String displayName;
    private Double monthlyPrice;
    private Double annualPrice;
    private String currency;
    private String badge;
    private boolean isPopular;
    private List<String> features;
    private Integer maxContacts; // null or 999 for unlimited
    private Integer aiQueriesPerMonth; // null or 999 for unlimited
    private boolean hasSmsBackup;
    private boolean hasSafeTripMode;
}

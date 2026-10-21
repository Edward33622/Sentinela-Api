package com.cibertec.ApiSentinela.ai.dtos;

public record AnalyzeRouteResponse(
    String riskLevel,
    int suggestedMinutes,
    String analysis,
    boolean poweredByGemini,
    Boolean isFeasible,
    String estimatedDistance
) {
    public AnalyzeRouteResponse(String riskLevel, int suggestedMinutes, String analysis, boolean poweredByGemini) {
        this(riskLevel, suggestedMinutes, analysis, poweredByGemini, true, null);
    }
}

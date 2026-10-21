package com.cibertec.ApiSentinela.ai.dtos;

public record AnalyzeRouteRequest(
    String origin,
    String destination,
    String transport,
    String hour
) {
    public AnalyzeRouteRequest(String destination, String transport, String hour) {
        this("Lima, Perú", destination, transport, hour);
    }
}

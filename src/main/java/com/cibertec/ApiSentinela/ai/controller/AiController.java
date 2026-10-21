package com.cibertec.ApiSentinela.ai.controller;

import com.cibertec.ApiSentinela.ai.dtos.AnalyzeRouteRequest;
import com.cibertec.ApiSentinela.ai.dtos.AnalyzeRouteResponse;
import com.cibertec.ApiSentinela.ai.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @Operation(summary = "Analiza el riesgo de un trayecto utilizando Google Gemini AI")
    @PostMapping("/analyze-route")
    public ResponseEntity<AnalyzeRouteResponse> analyzeRoute(@RequestBody AnalyzeRouteRequest request) {
        AnalyzeRouteResponse response = aiService.analyzeRoute(request);
        return ResponseEntity.ok(response);
    }
}

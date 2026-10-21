package com.cibertec.ApiSentinela.ai.service;

import com.cibertec.ApiSentinela.ai.dtos.AnalyzeRouteRequest;
import com.cibertec.ApiSentinela.ai.dtos.AnalyzeRouteResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnalyzeRouteResponse analyzeRoute(AnalyzeRouteRequest request) {
        String origin = (request.origin() != null && !request.origin().isBlank())
                ? request.origin() : "Lima, Perú";
        String destination = (request.destination() != null && !request.destination().isBlank()) 
                ? request.destination() : "Destino habitual";
        String transport = (request.transport() != null && !request.transport().isBlank()) 
                ? request.transport() : "A pie";
        String hour = (request.hour() != null && !request.hour().isBlank()) 
                ? request.hour() : "Horario actual";

        try {
            String prompt = String.format(
                "Eres 'Sentinela IA', el copiloto inteligente de seguridad y evaluación de rutas en el Perú.\n\n" +
                "ÁREA DE COBERTURA ACTIVA DE SENTINELA: Lima Metropolitana y Callao (Perú).\n\n" +
                "EVALÚA CON RIGOR GEOGRÁFICO REAL EL SIGUIENTE TRAYECTO:\n" +
                "- Punto de origen: %s\n" +
                "- Destino ingresado: %s\n" +
                "- Medio de transporte: %s\n" +
                "- Hora del trayecto: %s\n\n" +
                "REGLAS CRÍTICAS DE EVALUACIÓN GEOGRÁFICA Y SEGURIDAD:\n" +
                "1. COBERTURA METROPOLITANA Y COHERENCIA GEOGRÁFICA (OBLIGATORIO Y PRIORITARIO):\n" +
                "   - Sentinela está centrado en el monitoreo preventivo de distritos de Lima Metropolitana y Callao (ej. San Miguel, Miraflores, Los Olivos, Surco, Cercado, etc.).\n" +
                "   - Si el origen O el destino corresponden a OTRA PROVINCIA O REGIÓN del Perú (ej. Tumbes, Piura, Cusco, Arequipa, Trujillo, Chiclayo, Ica, Huancayo, etc.):\n" +
                "     * Debe señalar explícitamente al inicio: '⚠️ FUERA DE RANGO METROPOLITANO: Este trayecto se encuentra fuera de la cobertura urbana de Lima Metropolitana y Callao.'\n" +
                "     * Si el medio de transporte es 'A pie': Es TOTALMENTE INVIABLE, ABSURDO Y DE RIESGO EXTREMO. Señala obligatoriamente 'isFeasible': false, 'riskLevel': '🔴 RIESGO EXTREMO / INVIABLE'. Calcula la distancia real (ej. Tumbes ~1,270 km; Cusco ~1,100 km) y los minutos reales (más de 16,000 minutos / varias semanas de caminata forzada) y explica con total severidad que es humanamente imposible e ilegal/peligroso caminar esa distancia por autopista interprovincial y desierto.\n" +
                "     * Si el medio es 'Taxi por aplicativo' o vehículo urbano: Aclara que los taxis metropolitanos no realizan viajes interprovinciales de cientos o miles de kilómetros. Recomienda bus interprovincial formal en terminal terrestre o vuelo aéreo.\n" +
                "   - Si es un trayecto urbano coherente dentro de los distritos de Lima y Callao:\n" +
                "     * 'isFeasible': true, calcula la distancia urbana real (ej. 8.5 km) y el tiempo estimado considerando la congestión típica en Lima para la hora indicada.\n\n" +
                "2. EVALUACIÓN DE SEGURIDAD SEGÚN HORARIO Y ENTORNO:\n" +
                "   - Madrugada o noche profunda (23:00 a 05:30): Si va a pie en ciudad, aconseja enfáticamente NO caminar solo a esa hora.\n" +
                "   - Noche (18:00 a 22:59): Alerta de riesgo moderado a alto en paraderos y calles solitarias.\n" +
                "   - Día (06:00 a 17:59): Alerta de arrebatos de pertenencias en congestión y transporte público.\n\n" +
                "3. FORMATO DE RESPUESTA:\n" +
                "Debes responder OBLIGATORIAMENTE con un objeto JSON válido (sin texto adicional fuera del JSON) con la siguiente estructura exacta:\n" +
                "{\n" +
                "  \"riskLevel\": \"🔴 RIESGO EXTREMO / INVIABLE\" o \"🔴 RIESGO ALTO\" o \"🟡 RIESGO MODERADO\" o \"🟢 RIESGO BAJO\",\n" +
                "  \"isFeasible\": true o false,\n" +
                "  \"estimatedDistance\": \"ej. 1,270 km\" o \"ej. 8.5 km\",\n" +
                "  \"suggestedMinutes\": [número entero con los minutos calculados reales],\n" +
                "  \"analysis\": \"Texto explicativo completo con viñetas, alertas de fuera de rango si aplica, geografía, horario y recomendaciones\"\n" +
                "}",
                origin, destination, transport, hour
            );

            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(part));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", Collections.singletonList(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            List<String> candidateModels = Arrays.asList("gemini-flash-lite-latest", "gemini-3.5-flash-lite", "gemini-flash-latest");

            for (String modelName : candidateModels) {
                try {
                    String url = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key=" + geminiApiKey;
                    ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

                    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                        JsonNode root = objectMapper.readTree(response.getBody());
                        JsonNode candidatePart = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

                        if (!candidatePart.isMissingNode()) {
                            String rawText = candidatePart.asText().trim();
                            log.info("Gemini [{}] raw response: {}", modelName, rawText);

                            String jsonText = rawText;
                            if (jsonText.contains("```json")) {
                                int start = jsonText.indexOf("```json") + 7;
                                int end = jsonText.indexOf("```", start);
                                if (end > start) {
                                    jsonText = jsonText.substring(start, end).trim();
                                }
                            } else if (jsonText.contains("```")) {
                                int start = jsonText.indexOf("```") + 3;
                                int end = jsonText.indexOf("```", start);
                                if (end > start) {
                                    jsonText = jsonText.substring(start, end).trim();
                                }
                            }

                            try {
                                JsonNode parsed = objectMapper.readTree(jsonText);
                                String riskLevel = parsed.path("riskLevel").asText("🟡 RIESGO MODERADO");
                                int suggestedMin = parsed.path("suggestedMinutes").asInt(30);
                                boolean isFeasible = parsed.path("isFeasible").asBoolean(true);
                                String estimatedDistance = parsed.hasNonNull("estimatedDistance") ? parsed.path("estimatedDistance").asText() : null;
                                String analysis = parsed.path("analysis").asText(rawText);

                                return new AnalyzeRouteResponse(riskLevel, suggestedMin, analysis, true, isFeasible, estimatedDistance);
                            } catch (Exception parseEx) {
                                log.warn("Fallo al parsear JSON estructurado de Gemini [{}]: {}", modelName, parseEx.getMessage());
                                String upper = rawText.toUpperCase();
                                boolean isFeasible = !upper.contains("INVIABLE") && !upper.contains("INCOHEREN") && !upper.contains("EXTREMO");
                                String risk = (!isFeasible || upper.contains("EXTREMO")) ? "🔴 RIESGO EXTREMO / INVIABLE" :
                                              (upper.contains("ALTO") ? "🔴 RIESGO ALTO" :
                                              (upper.contains("MODERADO") ? "🟡 RIESGO MODERADO" : "🟢 RIESGO BAJO"));
                                int suggestedMin = isFeasible ? (transport.equalsIgnoreCase("A pie") ? 25 : 35) : 1300;

                                return new AnalyzeRouteResponse(risk, suggestedMin, rawText, true, isFeasible, null);
                            }
                        }
                    }
                } catch (Exception modelEx) {
                    log.warn("Modelo Gemini [{}] no disponible o límite alcanzado: {}. Probando siguiente modelo...", modelName, modelEx.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Fallo general al consultar Google Gemini: {}", e.getMessage());
        }

        // Fallback heurístico si no hay conexión a Google Gemini
        boolean esInterprovincial = esDestinoInterprovincial(destination);
        boolean aPie = transport.equalsIgnoreCase("A pie");

        if (esInterprovincial && aPie) {
            String reporteInviable = String.format(
                "⚠️ ALERTA GEOGRÁFICA Y DE SEGURIDAD (Modo Offline):\n" +
                "El destino '%s' se encuentra en otra provincia/departamento a cientos de kilómetros de %s.\n\n" +
                "🚫 TRAYECTO INVIABLE A PIE:\n" +
                "• No es físicamente posible realizar este desplazamiento caminando (requeriría semanas de trayecto por carretera y cordillera).\n" +
                "• Riesgo extremo de accidentes en carretera de alta velocidad y desprotección total.\n" +
                "• Se recomienda abordar un bus interprovincial en un terminal terrestre formal o un vuelo comercial.",
                destination, origin
            );
            return new AnalyzeRouteResponse("🔴 RIESGO EXTREMO / INVIABLE", 15000, reporteInviable, false, false, "+1,000 km");
        } else if (esInterprovincial) {
            String reporteInterprovincial = String.format(
                "🚌 DIAGNÓSTICO DE VIAJE INTERPROVINCIAL (Modo Offline):\n" +
                "Ruta: %s ➡️ %s en %s (%s).\n\n" +
                "💡 Recomendaciones para Viajes de Larga Distancia:\n" +
                "• Viaja únicamente en empresas de transporte interprovincial autorizadas (SUTRAN).\n" +
                "• Mantén tu celular cargado y comparte tu itinerario con tus contactos de emergencia.\n" +
                "• Tiempo de viaje terrestre aproximado: 21 a 24 horas (~1,300 minutos).",
                origin, destination, transport, hour
            );
            return new AnalyzeRouteResponse("🟡 RIESGO MODERADO", 1300, reporteInterprovincial, false, true, "+1,000 km");
        } else {
            int suggestedMin = aPie ? 25 : 35;
            String fallbackAnalysis = String.format(
                "Diagnóstico Urbano de Seguridad Sentinela:\n" +
                "Trayecto hacia %s en %s (%s).\n\n" +
                "💡 Recomendaciones de Seguridad:\n" +
                "• Comparte tu ubicación en vivo con tus contactos de emergencia.\n" +
                "• Evita el uso expuesto del teléfono en zonas desoladas o paraderos.\n" +
                "• Monitoreo preventivo calibrado para trayecto urbano: %d minutos.",
                destination, transport, hour, suggestedMin
            );
            return new AnalyzeRouteResponse("🟡 RIESGO MODERADO", suggestedMin, fallbackAnalysis, false, true, "5-10 km");
        }
    }

    private boolean esDestinoInterprovincial(String destino) {
        if (destino == null) return false;
        String d = destino.toUpperCase();
        return d.contains("CUSCO") || d.contains("CUZCO") || d.contains("AREQUIPA") ||
               d.contains("TRUJILLO") || d.contains("CHICLAYO") || d.contains("PIURA") ||
               d.contains("IQUITOS") || d.contains("PUNO") || d.contains("HUANCAYO") ||
               d.contains("TACNA") || d.contains("TARAPOTO") || d.contains("CAJAMARCA") ||
               d.contains("AYACUCHO") || d.contains("PUCALLPA") || d.contains("TUMBES");
    }
}

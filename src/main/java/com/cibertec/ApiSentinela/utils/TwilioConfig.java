package com.cibertec.ApiSentinela.utils;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    private static boolean configurado = false;

    /** true solo si se configuraron credenciales reales de Twilio en local.properties. */
    public static boolean isConfigurado() {
        return configurado;
    }

    @PostConstruct
    public void initTwilio() {
        boolean faltanCredenciales = accountSid == null || accountSid.isBlank() || accountSid.startsWith("dummy")
                || authToken == null || authToken.isBlank() || authToken.startsWith("dummy");
        if (faltanCredenciales) {
            System.err.println("[Twilio] Sin credenciales: las alertas por WhatsApp y SMS no se enviaran. "
                    + "Agrega TWILIO_ACCOUNT_SID y TWILIO_AUTH_TOKEN en local.properties.");
            return;
        }
        Twilio.init(accountSid, authToken);
        configurado = true;
        System.out.println("[Twilio] Credenciales cargadas: el envio de alertas esta habilitado.");
    }

}

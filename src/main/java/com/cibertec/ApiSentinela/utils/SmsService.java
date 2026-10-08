package com.cibertec.ApiSentinela.utils;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phoneNumber}")
    private String fromPhone;

    // Contacto de emergencia (puede venir de BD luego)
    private final String emergencyContact = "+519XXXXXXXX";

    public void sendEmergencyMessage(String messageBody) {

        Message message = Message.creator(
                new PhoneNumber(emergencyContact),
                new PhoneNumber(fromPhone),
                messageBody
        ).create();

        System.out.println("Mensaje enviado con SID: " + message.getSid());
    }
}

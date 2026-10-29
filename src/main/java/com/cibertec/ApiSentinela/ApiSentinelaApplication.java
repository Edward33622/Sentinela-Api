package com.cibertec.ApiSentinela;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiSentinelaApplication {

	public static void main(String[] args) {
		// En Windows se usan los certificados del sistema operativo. Así la conexión segura con
		// Twilio funciona aunque un antivirus (por ejemplo, Avast) inspeccione el tráfico HTTPS.
		if (System.getProperty("os.name", "").toLowerCase().contains("windows")
				&& System.getProperty("javax.net.ssl.trustStoreType") == null) {
			System.setProperty("javax.net.ssl.trustStoreType", "Windows-ROOT");
		}
		SpringApplication.run(ApiSentinelaApplication.class, args);
	}

}

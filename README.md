# Sentinela API

API REST de la aplicación Sentinela (Spring Boot 3.3, Java 17, MySQL).

## Puesta en marcha

1. Crear la base de datos ejecutando en MySQL `database/schema.sql` y luego `database/data.sql`. Se ejecutan **una sola vez**: `schema.sql` borra y vuelve a crear la base.
2. Copiar `local.properties.example` como `local.properties` (en la raíz del proyecto) y escribir la contraseña de MySQL en `DB_PASSWORD`. Ese archivo no se sube a Git.
3. Compilar y arrancar:

```bash
./mvnw -DskipTests package
java -jar target/ApiSentinela-0.0.1-SNAPSHOT.jar
```

La API queda en `http://localhost:8080`. En el emulador de Android se accede como `http://10.0.2.2:8080`.

## Alertas por WhatsApp (Twilio)

La API envía las alertas de emergencia con Twilio. Para activarlas:

1. Crear una cuenta en https://www.twilio.com y copiar el **Account SID** y el **Auth Token** de la consola.
2. Pegarlos en `local.properties` en `TWILIO_ACCOUNT_SID` y `TWILIO_AUTH_TOKEN`, y reiniciar la API. Al arrancar debe aparecer el mensaje `[Twilio] Credenciales cargadas`.
3. Con el **sandbox de WhatsApp** de Twilio (remitente `+14155238886`), cada contacto que vaya a recibir alertas debe enviar antes, desde su WhatsApp a ese número, el mensaje `join <código>` que muestra la consola de Twilio en *Messaging → Try it out → Send a WhatsApp message*.
4. Con una cuenta de prueba (trial), Twilio solo entrega mensajes a números verificados en la consola.

El **SMS de respaldo** es opcional: requiere un número de Twilio habilitado para SMS en `TWILIO_SMS_NUMBER`. Si se deja vacío, solo se envía WhatsApp.

Sin credenciales, la API funciona con normalidad, pero no envía las alertas e informa el motivo en la consola.

En Windows, la API usa automáticamente los certificados del sistema operativo, para que la conexión con Twilio funcione aunque un antivirus inspeccione el tráfico HTTPS.

-- 1️⃣ Crear base de datos
DROP DATABASE IF EXISTS sentinela_bd;
CREATE DATABASE sentinela_bd;
USE sentinela_bd;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name NVARCHAR(200) NOT NULL,
    email NVARCHAR(200) NOT NULL UNIQUE,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE'
);

CREATE TABLE contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name NVARCHAR(200) NOT NULL,
    phone_number VARCHAR(20),
    email NVARCHAR(200) NOT NULL,
    enable_whatsapp BOOL DEFAULT TRUE,
    is_emergency_contact BOOL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE emergency_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closed_at TIMESTAMP NULL,
    status ENUM('ACTIVE','CLOSED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE emergency_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emergency_event_id BIGINT NOT NULL,
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    captured_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (emergency_event_id) REFERENCES emergency_events(id)
);

CREATE TABLE emergency_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    emergency_event_id BIGINT NOT NULL,
    media_type ENUM('AUDIO','VIDEO','PHOTO') NOT NULL,
    storage_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (emergency_event_id) REFERENCES emergency_events(id) ON DELETE CASCADE
);

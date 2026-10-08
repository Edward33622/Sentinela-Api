USE sentinela_bd;

-- ===========================
-- 1️⃣ Tabla users
-- ===========================
INSERT INTO users (full_name, email, phone, password_hash, status) VALUES
('Juan Pérez', 'juan.perez@email.com', '987654321', 'hash123', 'ACTIVE'),
('María López', 'maria.lopez@email.com', '987654322', 'hash234', 'ACTIVE'),
('Carlos Ramírez', 'carlos.ramirez@email.com', '987654323', 'hash345', 'ACTIVE'),
('Ana Torres', 'ana.torres@email.com', '987654324', 'hash456', 'ACTIVE'),
('Luis García', 'luis.garcia@email.com', '987654325', 'hash567', 'ACTIVE');

-- ===========================
-- 2️⃣ Tabla contacts
-- ===========================
INSERT INTO contacts (user_id, full_name, phone_number, email, enable_whatsapp, is_emergency_contact) VALUES
(1, 'Laura Pérez', '987111111', 'laura.perez@email.com', TRUE, TRUE),
(2, 'Miguel López', '987222222', 'miguel.lopez@email.com', TRUE, FALSE),
(3, 'Sofía Ramírez', '987333333', 'sofia.ramirez@email.com', FALSE, TRUE),
(4, 'Andrés Torres', '987444444', 'andres.torres@email.com', TRUE, FALSE),
(5, 'Valeria García', '987555555', 'valeria.garcia@email.com', FALSE, TRUE);

-- ===========================
-- 3️⃣ Tabla emergency_events
-- ===========================
INSERT INTO emergency_events (user_id, activated_at, closed_at, status) VALUES
(1, NOW(), NULL, 'ACTIVE'),
(2, NOW() - INTERVAL 1 HOUR, NOW(), 'CLOSED'),
(3, NOW() - INTERVAL 2 HOUR, NULL, 'ACTIVE'),
(4, NOW() - INTERVAL 3 HOUR, NOW(), 'CLOSED'),
(5, NOW() - INTERVAL 4 HOUR, NULL, 'ACTIVE');

-- ===========================
-- 4️⃣ Tabla emergency_locations
-- ===========================
INSERT INTO emergency_locations (emergency_event_id, latitude, longitude, captured_at) VALUES
(1, -12.04318, -77.02824, NOW()),
(2, -12.04637, -77.04279, NOW() - INTERVAL 1 HOUR),
(3, -12.04845, -77.03199, NOW() - INTERVAL 2 HOUR),
(4, -12.05012, -77.03789, NOW() - INTERVAL 3 HOUR),
(5, -12.04200, -77.03300, NOW() - INTERVAL 4 HOUR);

-- ===========================
-- 5️⃣ Tabla emergency_media
-- ===========================
INSERT INTO emergency_media (emergency_event_id, media_type, storage_url) VALUES
(1, 'PHOTO', 'https://storage.example.com/media1.jpg'),
(2, 'VIDEO', 'https://storage.example.com/media2.mp4'),
(3, 'AUDIO', 'https://storage.example.com/media3.mp3'),
(4, 'PHOTO', 'https://storage.example.com/media4.jpg'),
(5, 'VIDEO', 'https://storage.example.com/media5.mp4');

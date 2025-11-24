-- ============================
--   BASE DE DATOS COMPLETA
-- ============================

-- TABLA: parada
CREATE TABLE parada (
                        parada_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        latitud DOUBLE NULL,
                        longitud DOUBLE NULL
);

-- TABLA: monopatin
CREATE TABLE monopatin (
                           monopatin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           estado ENUM('DISPONIBLE', 'MANTENIMIENTO', 'OCUPADO') NULL,
                           latitud DOUBLE NOT NULL,
                           longitud DOUBLE NOT NULL,
                           parada_id BIGINT NULL,
                           CONSTRAINT fk_monopatin_parada
                               FOREIGN KEY (parada_id) REFERENCES parada(parada_id)
);

-- TABLA: mantenimiento
CREATE TABLE mantenimiento (
                               mantenimiento_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               encargado_id BIGINT NULL,
                               fecha_fin DATETIME(6) NULL,
                               fecha_inicio DATETIME(6) NULL,
                               monopatin_id BIGINT NULL,
                               CONSTRAINT fk_mantenimiento_monopatin
                                   FOREIGN KEY (monopatin_id) REFERENCES monopatin(monopatin_id)
);

-- TABLA: account
CREATE TABLE account (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         fecha_alta DATETIME(6) NULL,
                         saldo FLOAT NULL,
                         tipo VARCHAR(255) NULL
);

-- TABLA: users
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       is_disabled TINYINT(1) DEFAULT 0 NULL,
                       mobile BIGINT NULL,
                       name VARCHAR(255) NULL,
                       rol TINYINT NULL,
                       surname VARCHAR(255) NULL,
                       tipo TINYINT NULL
);

-- TABLA INTERMEDIA: usuario_account
CREATE TABLE usuario_account (
                                 usuario_id BIGINT NOT NULL,
                                 account_id BIGINT NOT NULL,
                                 CONSTRAINT fk_usuario_account_account
                                     FOREIGN KEY (account_id) REFERENCES account(id),
                                 CONSTRAINT fk_usuario_account_usuario
                                     FOREIGN KEY (usuario_id) REFERENCES users(id)
);

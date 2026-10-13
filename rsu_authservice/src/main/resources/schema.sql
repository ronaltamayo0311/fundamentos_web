-- 1. Catálogo de Roles 
CREATE TABLE ROLE (
    id_role BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE, -- 'ESTUDIANTE', 'DOCENTE', 'EGRESADO'
    name VARCHAR(100) NOT NULL
);

-- 2. Identidad de Usuarios
CREATE TABLE USERS(
	id_user BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255), -- Para el login local
    name VARCHAR(100) NOT NULL,
    role VARCHAR(25) NOT NULL,
    state VARCHAR(20) DEFAULT 'ACTIVO'  
);

-- 3. Multicalidad: Relación Persona y sus Roles
CREATE TABLE USER_ROLE (
    id_user_role BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_user BIGINT NOT NULL,
    id_role BIGINT NOT NULL,
    FOREIGN KEY (id_user) REFERENCES USERS(id_user),
    FOREIGN KEY (id_role) REFERENCES ROLE(id_role)
);

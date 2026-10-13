-- 1. Insertar Roles

INSERT INTO ROLE (code, name) VALUES ('SUPER_ADMIN', 'Super administrador del Sistema');
INSERT INTO ROLE (code, name) VALUES ('ADMIN', 'Administrador normal del Sistema');

-- 2. Insertar Persona (Ejemplo: Administrador/Votante)
-- Se asume que los campos de la tabla PERSONA siguen el orden del CREATE TABLE proporcionado
INSERT INTO USERS (username, password, name, role, state)
           VALUES ('ronal', '$2a$10$AlXajvB5M4l99w3/OtMmg.EwhnB7dKY0DCXcz8jJlIZqGv.kL//9e', 'Ronal Tamayo', 'SUPER_ADMIN', 'ACTIVO');


-- Password: 123456 (hasheado)
-- Respuestas de seguridad: gato y azul (hasheadas)

-- 3. Asignar Rol a Persona (Persona 1 es Estudiante)
INSERT INTO USER_ROLE (id_user, id_role) VALUES (1, 1);


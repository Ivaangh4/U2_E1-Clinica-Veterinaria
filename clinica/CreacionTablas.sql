
CREATE DATABASE IF NOT EXISTS veterinarios;

USE veterinarios;


DROP TABLE IF EXISTS certificaciones;
DROP TABLE IF EXISTS detalles_contratos;
DROP TABLE IF EXISTS veterinarios;


CREATE TABLE veterinarios (
    num_licencia VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    fecha_contratacion DATE NOT NULL,

    PRIMARY KEY (num_licencia)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE detalles_contratos (
    id INT NOT NULL AUTO_INCREMENT,
    salario_base DECIMAL(10,2) NOT NULL,
    horario_semanal INT NOT NULL,
    veterinario_licencia VARCHAR(20) NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (veterinario_licencia)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE certificaciones (
    institucion_emisora VARCHAR(150) NOT NULL,
    nombre_especialidad VARCHAR(150) NOT NULL,
    veterinario_licencia VARCHAR(20) NOT NULL,
    id INT NOT NULL AUTO_INCREMENT,

    PRIMARY KEY (id)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE detalles_contratos
ADD CONSTRAINT detalles_contratos_veterinario_fk
FOREIGN KEY (veterinario_licencia)
REFERENCES veterinarios (num_licencia)
ON DELETE CASCADE
ON UPDATE CASCADE;


ALTER TABLE certificaciones
ADD CONSTRAINT certificaciones_veterinario_fk
FOREIGN KEY (veterinario_licencia)
REFERENCES veterinarios (num_licencia)
ON DELETE CASCADE
ON UPDATE CASCADE;


SHOW TABLES;

SHOW CREATE TABLE detalles_contratos;

SHOW CREATE TABLE certificaciones;

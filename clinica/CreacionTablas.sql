CREATE TABLE veterinarios.veterinarios (
    num_licencia VARCHAR(20) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    fecha_contratacion DATE NOT NULL,

    CONSTRAINT veterinarios_pk
        PRIMARY KEY (num_licencia)

)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;


CREATE TABLE veterinarios.detalles_contratos (
    id INT AUTO_INCREMENT NOT NULL,
    salario_base DECIMAL(10,2) NOT NULL,
    horario_semanal INT NOT NULL,
    veterinario_licencia VARCHAR(20) NOT NULL,

    CONSTRAINT detalles_contratos_pk
        PRIMARY KEY (id),

    CONSTRAINT detalles_contratos_unique
        UNIQUE (veterinario_licencia),

    CONSTRAINT detalles_contratos_veterinario_fk
        FOREIGN KEY (veterinario_licencia)
        REFERENCES veterinarios.veterinarios(num_licencia)
        ON DELETE CASCADE
        ON UPDATE CASCADE

)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;


CREATE TABLE veterinarios.certificaciones (
    institucion_emisora VARCHAR(150) NOT NULL,
    nombre_especialidad VARCHAR(150) NOT NULL,
    veterinario_licencia VARCHAR(20) NOT NULL,
    id INT AUTO_INCREMENT NOT NULL,

    CONSTRAINT certificaciones_pk
        PRIMARY KEY (id),

    CONSTRAINT certificaciones_veterinario_fk
        FOREIGN KEY (veterinario_licencia)
        REFERENCES veterinarios.veterinarios(num_licencia)
        ON DELETE CASCADE
        ON UPDATE CASCADE

)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;
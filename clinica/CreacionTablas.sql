CREATE TABLE veterinarios.veterinarios (
	num_licencia VARCHAR(20) NOT NULL,
	nombre VARCHAR(100) NOT NULL,
	apellidos VARCHAR(150) NOT NULL,
	fecha_contratacion DATE NOT NULL,
	CONSTRAINT veterinarios_pk PRIMARY KEY (num_licencia)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;




CREATE TABLE veterinarios.detalles_contratos (
	id INT auto_increment NOT NULL,
	salario_base DECIMAL(10,2) NOT NULL,
	horario_semanal INT NOT NULL,
	veterinario_licencia VARCHAR(20) NOT NULL,
	CONSTRAINT detalles_contratos_pk PRIMARY KEY (id),
	CONSTRAINT detalles_contratos_unique UNIQUE KEY (veterinario_licencia)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;




CREATE TABLE veterinarios.certificaciones (
	institucion_emisora VARCHAR(150) NOT NULL,
	nombre_especialidad VARCHAR(150) NOT NULL,
	veterinario_licencia VARCHAR(20) NOT NULL,
	id INT auto_increment NOT NULL,
	CONSTRAINT certificaciones_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4;
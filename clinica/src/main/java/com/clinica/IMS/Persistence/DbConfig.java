package com.clinica.IMS.Persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DbConfig {
	private static Properties properties = new Properties();

	private DbConfig() {
	}

	private static void cargarConfiguracion() throws SQLException {

		try (InputStream input = DbConfig.class.getClassLoader().getResourceAsStream("db.properties")) {

			if (input == null) {
				throw new IOException("No se encuentra el archivo db.properties");
			}

			properties.load(input);

		} catch (IOException e) {
			throw new SQLException("Error al cargar la configuración", e);
		}
	}

	public static Properties getProperties() throws SQLException {

		if (properties.isEmpty()) {
			cargarConfiguracion();
		}

		Properties copia = new Properties();
		copia.putAll(properties);

		return copia;
	}

	public static void reload() throws SQLException {
		properties.clear();
		cargarConfiguracion();
	}
}

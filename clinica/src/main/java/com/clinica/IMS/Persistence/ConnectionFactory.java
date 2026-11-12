package com.clinica.IMS.Persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionFactory {
	private static final HikariDataSource dataSource;

	static {

		try {

			Properties props = DbConfig.getProperties();

			HikariConfig config = new HikariConfig();

			config.setJdbcUrl(props.getProperty("db.url"));
			config.setDriverClassName(props.getProperty("db.driver"));
			config.setUsername(props.getProperty("db.user"));
			config.setPassword(props.getProperty("db.password"));

			config.setMaximumPoolSize(Integer.parseInt(props.getProperty("pool.maxSize", "10")));

			config.setMinimumIdle(Integer.parseInt(props.getProperty("pool.minIdle", "2")));

			config.setIdleTimeout(Long.parseLong(props.getProperty("pool.idleTimeout", "600000")));

			config.setConnectionTimeout(Long.parseLong(props.getProperty("pool.connectionTimeout", "30000")));

			config.setPoolName("ClinicaVeterinariaPool");

			dataSource = new HikariDataSource(config);

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private ConnectionFactory() {
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public static void closePool() {

		if (!dataSource.isClosed()) {
			dataSource.close();
		} //fSDF
	}
}

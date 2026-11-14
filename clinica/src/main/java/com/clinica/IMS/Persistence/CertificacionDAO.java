package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.Certificacion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel
 * Capa encargada de hacer todas las operaciones Crud
 */
public class CertificacionDAO {

	private static final Logger logger = LogManager.getLogger(CertificacionDAO.class);

	private static Connection connection;
	private static PreparedStatement psInsert;
	private static PreparedStatement psGetById;
	private static PreparedStatement psGetByLicence;
	private static PreparedStatement psUpdate;
	private static PreparedStatement psDelete;

	static {
		try {
			connection = ConnectionFactory.getConnection();
			psInsert = connection.prepareStatement("INSERT INTO certificaciones "
					+ "(institucion_emisora, nombre_especialidad, veterinario_licencia) " + "VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			psGetById = connection.prepareStatement("SELECT id, institucion_emisora, nombre_especialidad, "
					+ "veterinario_licencia FROM certificaciones WHERE id = ?");
			psGetByLicence = connection.prepareStatement("SELECT id, institucion_emisora, nombre_especialidad, "
					+ "veterinario_licencia FROM certificaciones WHERE veterinario_licencia = ?");
			psUpdate = connection.prepareStatement("UPDATE certificaciones SET institucion_emisora = ?, "
					+ "nombre_especialidad = ? WHERE id = ?");
			psDelete = connection.prepareStatement("DELETE FROM certificaciones WHERE id = ?");
		} catch (SQLException e) {
			logger.error("Error al obtener la conexión para las certificaciones", e);
		}
	}

	public CertificacionDAO() {
	}

	public boolean insertarCertificacion(Certificacion certificacion) {
		boolean insertado = false;
		try {
			connection.setAutoCommit(false);

			psInsert.setString(1, certificacion.getEntidadEmisora());
			psInsert.setString(2, certificacion.getNombreEsp());
			psInsert.setString(3, certificacion.getLicenciaVeterinario());

			if (psInsert.executeUpdate() > 0) {
				try (ResultSet rs = psInsert.getGeneratedKeys()) {
					if (rs.next()) {
						certificacion.setIdCertificacion(rs.getInt(1));
					}
				}
				connection.commit();
				logger.info("Certificación insertada correctamente");
				insertado = true;
			}

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en la inserción de certificación", ex);
			}
			logger.error("Error al insertar certificación", e);
		}
		return insertado;
	}

	public Certificacion obtenerCertificacionPorId(int id) {
		Certificacion certificacion = null;

		try {
			psGetById.setInt(1, id);

			try (ResultSet rs = psGetById.executeQuery()) {
				if (rs.next()) {
					certificacion = new Certificacion(
							rs.getInt("id"),
							rs.getString("institucion_emisora"),
							rs.getString("nombre_especialidad"),
							rs.getString("veterinario_licencia"));
					logger.info("Certificación encontrada");
				} else {
					logger.warn("Certificación no encontrada");
				}
			}
		} catch (SQLException e) {
			logger.error("Error al obtener certificación", e);
		}
		return certificacion;
	}

	public List<Certificacion> obtenerCertificacionesPorLicencia(String licencia) {
		List<Certificacion> certificaciones = new ArrayList<>();

		try {
			psGetByLicence.setString(1, licencia);

			try (ResultSet rs = psGetByLicence.executeQuery()) {
				while (rs.next()) {
					certificaciones.add(new Certificacion(
							rs.getInt("id"),
							rs.getString("institucion_emisora"),
							rs.getString("nombre_especialidad"),
							rs.getString("veterinario_licencia")));
				}
				logger.info("Certificaciones encontradas: {}", certificaciones.size());
			}
		} catch (SQLException e) {
			logger.error("Error al obtener las certificaciones del veterinario", e);
		}
		return certificaciones;
	}

	public boolean actualizarCertificacion(Certificacion certificacion) {
		boolean actualizado = false;

		try {
			connection.setAutoCommit(false);
			psUpdate.setString(1, certificacion.getEntidadEmisora());
			psUpdate.setString(2, certificacion.getNombreEsp());
			psUpdate.setInt(3, certificacion.getIdCertificacion());

			if (psUpdate.executeUpdate() > 0) {
				connection.commit();
				logger.info("Certificación actualizada correctamente");
				actualizado = true;
			} else {
				connection.rollback();
				logger.warn("No se encontró la certificación con id: {}", certificacion.getIdCertificacion());
			}

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en la actualización de certificación", ex);
			}
			logger.error("Error al actualizar certificación", e);
		}
		return actualizado;
	}

	public boolean eliminarCertificacion(int id) {
		boolean borrado = false;

		try {
			connection.setAutoCommit(false);
			psDelete.setInt(1, id);

			if (psDelete.executeUpdate() > 0) {
				connection.commit();
				logger.info("Certificación borrada correctamente");
				borrado = true;
			} else {
				connection.rollback();
				logger.warn("No se encontró la certificación con id: {}", id);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en el borrado de certificación", ex);
			}
			logger.error("Error al borrar certificación", e);
		}
		return borrado;
	}

}
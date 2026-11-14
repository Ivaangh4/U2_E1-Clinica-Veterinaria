package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.DetalleContrato;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Samuel
 * Capa encargada de hacer todas las operaciones Crud
 */
public class DetalleContratoDAO {

	private static final Logger logger = LogManager.getLogger(DetalleContratoDAO.class);

	private static Connection connection;
	private static PreparedStatement psInsert;
	private static PreparedStatement psGetById;
	private static PreparedStatement psGetByLicence;
	private static PreparedStatement psUpdate;
	private static PreparedStatement psUpdateSalario;
	private static PreparedStatement psDelete;

	static {
		try {
			connection = ConnectionFactory.getConnection();
			psInsert = connection.prepareStatement("INSERT INTO detalles_contratos "
					+ "(salario_base, horario_semanal, veterinario_licencia) " + "VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			psGetById = connection.prepareStatement("SELECT id, salario_base, horario_semanal, "
					+ "veterinario_licencia FROM detalles_contratos WHERE id = ?");
			psGetByLicence = connection.prepareStatement("SELECT id, salario_base, horario_semanal, "
					+ "veterinario_licencia FROM detalles_contratos WHERE veterinario_licencia = ?");
			psUpdate = connection.prepareStatement("UPDATE detalles_contratos SET salario_base = ?, "
					+ "horario_semanal = ? WHERE id = ?");
			psUpdateSalario = connection.prepareStatement("UPDATE detalles_contratos SET salario_base = ? "
					+ "WHERE veterinario_licencia = ?");
			psDelete = connection.prepareStatement("DELETE FROM detalles_contratos WHERE id = ?");
		} catch (SQLException e) {
			logger.error("Error al obtener la conexión para los detalles de contrato", e);
		}
	}

	public DetalleContratoDAO() {
	}

	public boolean insertarContrato(DetalleContrato contrato) {
		boolean insertado = false;
		try {
			connection.setAutoCommit(false);

			psInsert.setBigDecimal(1, contrato.getSalarioBase());
			psInsert.setInt(2, contrato.getHorarioSemanal());
			psInsert.setString(3, contrato.getVeterinarioLicencia());

			if (psInsert.executeUpdate() > 0) {
				try (ResultSet rs = psInsert.getGeneratedKeys()) {
					if (rs.next()) {
						contrato.setIdContrato(rs.getInt(1));
					}
				}
				connection.commit();
				logger.info("Contrato insertado correctamente");
				insertado = true;
			}

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en la inserción de contrato", ex);
			}
			logger.error("Error al insertar contrato", e);
		}
		return insertado;
	}

	public DetalleContrato obtenerContratoPorId(int id) {
		DetalleContrato contrato = null;

		try {
			psGetById.setInt(1, id);

			try (ResultSet rs = psGetById.executeQuery()) {
				if (rs.next()) {
					contrato = new DetalleContrato(
							rs.getInt("id"),
							rs.getBigDecimal("salario_base"),
							rs.getInt("horario_semanal"),
							rs.getString("veterinario_licencia"));
					logger.info("Contrato encontrado");
				} else {
					logger.warn("Contrato no encontrado");
				}
			}
		} catch (SQLException e) {
			logger.error("Error al obtener contrato", e);
		}
		return contrato;
	}

	public DetalleContrato obtenerContratoPorLicencia(String licencia) {
		DetalleContrato contrato = null;

		try {
			psGetByLicence.setString(1, licencia);

			try (ResultSet rs = psGetByLicence.executeQuery()) {
				if (rs.next()) {
					contrato = new DetalleContrato(
							rs.getInt("id"),
							rs.getBigDecimal("salario_base"),
							rs.getInt("horario_semanal"),
							rs.getString("veterinario_licencia"));
					logger.info("Contrato encontrado");
				} else {
					logger.warn("Contrato no encontrado para la licencia: {}", licencia);
				}
			}
		} catch (SQLException e) {
			logger.error("Error al obtener contrato por licencia", e);
		}
		return contrato;
	}

	public boolean actualizarContrato(DetalleContrato contrato) {
		boolean actualizado = false;

		try {
			connection.setAutoCommit(false);
			psUpdate.setBigDecimal(1, contrato.getSalarioBase());
			psUpdate.setInt(2, contrato.getHorarioSemanal());
			psUpdate.setInt(3, contrato.getIdContrato());

			if (psUpdate.executeUpdate() > 0) {
				connection.commit();
				logger.info("Contrato actualizado correctamente");
				actualizado = true;
			} else {
				connection.rollback();
				logger.warn("No se encontró el contrato con id: {}", contrato.getIdContrato());
			}

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en la actualización de contrato", ex);
			}
			logger.error("Error al actualizar contrato", e);
		}
		return actualizado;
	}

	public boolean actualizarSalario(String licencia, BigDecimal nuevoSalario) {
		boolean actualizado = false;

		try {
			connection.setAutoCommit(false);
			psUpdateSalario.setBigDecimal(1, nuevoSalario);
			psUpdateSalario.setString(2, licencia);

			if (psUpdateSalario.executeUpdate() > 0) {
				connection.commit();
				logger.info("Salario actualizado correctamente");
				actualizado = true;
			} else {
				connection.rollback();
				logger.warn("No se encontró contrato para la licencia: {}", licencia);
			}

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en la actualización de salario", ex);
			}
			logger.error("Error al actualizar salario", e);
		}
		return actualizado;
	}

	public boolean eliminarContrato(int id) {
		boolean borrado = false;

		try {
			connection.setAutoCommit(false);
			psDelete.setInt(1, id);

			if (psDelete.executeUpdate() > 0) {
				connection.commit();
				logger.info("Contrato borrado correctamente");
				borrado = true;
			} else {
				connection.rollback();
				logger.warn("No se encontró el contrato con id: {}", id);
			}
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				logger.error("Error al realizar rollback en el borrado de contrato", ex);
			}
			logger.error("Error al borrar contrato", e);
		}
		return borrado;
	}

}
package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.clinica.IMS.Entity.Certificacion;

public class VeterinarioDAO {

	private static final Logger logger = LogManager.getLogger(VeterinarioDAO.class);
	
	private static Connection connection;
	private static PreparedStatement psInsertVet;
	private static PreparedStatement psGetByLicence;
	private static PreparedStatement psDelete;
	private static PreparedStatement psUpdate;
	
	static {
		try {
			connection = ConnectionFactory.getConnection();
			psInsertVet = connection.prepareStatement("INSERT INTO veterinarios "
					+ "(num_licencia, nombre, apellidos, fecha_contratacion) " + "VALUES (?, ?, ?, ?)");
			psGetByLicence = connection.prepareStatement("SELECT num_licencia, nombre, apellidos, fecha_contratacion "
					+ "FROM veterinarios WHERE num_licencia = (?)");
			psDelete = connection.prepareStatement("DELETE FROM veterinarios WHERE num_licencia = (?)");
			psUpdate = connection.prepareStatement("UPDATE veterinarios SET nombre = ?, apellidos = ?,"
					+ " fecha_contratacion = ? WHERE num_licencia = ?");
		} catch (SQLException e) {
			logger.error("Error al obtener la conexión para insertar el veterinario",e);
		}
	}

	public VeterinarioDAO() {
		// TODO Auto-generated constructor stub
	}

	public boolean insertarVeterinarioConContrato(Veterinario vetOriginal, DetalleContrato contratoOriginal) {
		boolean insertado = false;
		try {
			connection.setAutoCommit(false);
			
			psInsertVet.setString(1, vetOriginal.getNumLicencia());
			psInsertVet.setString(2, vetOriginal.getNombre());
			psInsertVet.setString(3, vetOriginal.getApellidos());
			psInsertVet.setDate(4, vetOriginal.getFechaContratacion());
			
			if(psInsertVet.executeUpdate() > 0) {
				connection.commit();
				logger.info("Veterinario insertado correctamente");
				insertado =  true;
			}
			
		} catch (SQLException e) {
			try {
	            connection.rollback();
	        } catch (SQLException ex) {
	            logger.error("Error al realizar rollback", ex);
	        }
			logger.error("Error al insertar veterinario",e);
		}
		return insertado;
	}

	public Veterinario obtenerVeterinarioPorLicencia(String licencia) {
		// TODO Auto-generated method stub
		Veterinario veterinario = null;
		
		try {
			psGetByLicence.setString(1, licencia);
			
			try (ResultSet rs = psGetByLicence.executeQuery()) {
	            if (rs.next()) {
	                veterinario = new Veterinario(
	                    rs.getString("num_licencia"),
	                    rs.getString("nombre"),
	                    rs.getString("apellidos"),
	                    rs.getDate("fecha_contratacion")
	                );
	                logger.info("Veterinario encontrado");
	            } else {
	                logger.warn("Veterinario no encontrado");
	            }
	        }
		}catch(SQLException e) {
			logger.error("Error al obtener veterinario",e);
		}
		return veterinario;
	}

	public DetalleContrato obtenerContratoPorLicencia(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean registrarCertificaciones(String licencia, List<Certificacion> certificaciones) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean actualizarSalarioContrato(String licencia, BigDecimal nuevoSalario) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean eliminarVeterinario(String licencia) {
		boolean borrado = false;
		try {
			connection.setAutoCommit(false);
			psDelete.setString(1, licencia);
			
			if(psDelete.executeUpdate() > 0) {
				connection.commit();
				logger.info("Veterinario borrado correctamente");
				borrado =  true;
			}
		}catch(SQLException e) {
			try {
	            connection.rollback();
	        } catch (SQLException ex) {
	            logger.error("Error al realizar rollback en el borrado", ex);
	        }
			logger.error("Error al borrar veterinario",e);
		}
		return borrado;
	}
	
	public boolean actualizarVeterinario(Veterinario veterinario) {
		boolean actualizado = false;
		
		try {
			connection.setAutoCommit(false);
			psUpdate.setString(1, veterinario.getNombre());
			psUpdate.setString(2, veterinario.getApellidos());
			psUpdate.setDate(3, veterinario.getFechaContratacion());
			psUpdate.setString(4, veterinario.getNumLicencia());
			
			if(psUpdate.executeUpdate() > 0) {
				connection.commit();
				logger.info("Veterinario actualizado correctamente");
				actualizado = true;
			} else {

	            connection.rollback();

	            logger.warn("No se encontró el veterinario con licencia: {}",
	                    veterinario.getNumLicencia());
	        }
			
		}catch(SQLException e) {
			try {
	            connection.rollback();
	        } catch (SQLException ex) {
	            logger.error("Error al realizar rollback en el actualizado", ex);
	        }
			logger.error("Error al actualizar veterinario",e);
		}
		return actualizado;
		
	}
	
}

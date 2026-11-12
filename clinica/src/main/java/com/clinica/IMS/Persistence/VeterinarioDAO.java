package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.clinica.IMS.Entity.Certificacion;

public class VeterinarioDAO {
	
	private static Connection connection;
	private static PreparedStatement psInsertVet;
	private static PreparedStatement psGetByLicence;
	private static PreparedStatement psDelete;
	static {
		try {
			connection = ConnectionFactory.getConnection();
			psInsertVet = connection.prepareStatement("INSERT INTO veterinarios "
					+ "(num_licencia, nombre, apellidos, fecha_contratacion) "
					+ "VALUES (?, ?, ?, ?)");
			psGetByLicence = connection.prepareStatement("SELECT num_licencia, nombre, apellidos, fecha_contratacion "
					+ "FROM veterinarios WHERE num_licencia = (?)");
			psDelete = connection.prepareStatement("DELETE FROM veterinarios WHERE num_licencia = (?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public VeterinarioDAO() {
		// TODO Auto-generated constructor stub
	}

	public boolean insertarVeterinarioConContrato(Veterinario vetOriginal, DetalleContrato contratoOriginal) {
		// TODO Auto-generated method stub
		return false;
	}

	public Veterinario obtenerVeterinarioPorLicencia(String string) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return false;
	}
	

}

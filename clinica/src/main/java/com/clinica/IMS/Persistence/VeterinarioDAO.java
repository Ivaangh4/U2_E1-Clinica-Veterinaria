package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;

import java.math.BigDecimal;
import java.util.List;

import com.clinica.IMS.Entity.Certificacion;

public class VeterinarioDAO {

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
	
	//wevewvwe

}

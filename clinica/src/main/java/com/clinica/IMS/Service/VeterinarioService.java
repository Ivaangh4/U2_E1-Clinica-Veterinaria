package com.clinica.IMS.Service;

import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Persistence.VeterinarioDAO;

public class VeterinarioService {
	private final VeterinarioDAO veterinarioDAO;

	public VeterinarioService() {
		this.veterinarioDAO = new VeterinarioDAO();
	}

	public boolean insertarVeterinario(Veterinario veterinario) {

		if (veterinario == null) {
			return false;
		}

		if (veterinario.getNumLicencia() == null || veterinario.getNumLicencia().isBlank()) {
			return false;
		}

		if (veterinario.getNombre() == null || veterinario.getNombre().isBlank()) {
			return false;
		}

		if (veterinario.getApellidos() == null || veterinario.getApellidos().isBlank()) {
			return false;
		}

		if (veterinario.getFechaContratacion() == null) {
			return false;
		}

		Veterinario existente = veterinarioDAO.obtenerVeterinarioPorLicencia(veterinario.getNumLicencia());

		if (existente != null) {
			return false;
		}

		return veterinarioDAO.insertarVeterinario(veterinario);
	}

	
	
	public Veterinario obtenerVeterinarioPorLicencia(String licencia) {

		if (licencia == null || licencia.isBlank()) {
			return null;
		}

		return veterinarioDAO.obtenerVeterinarioPorLicencia(licencia);
	}

	
	
	public boolean actualizarVeterinario(Veterinario veterinario) {

		if (veterinario == null) {
			return false;
		}

		if (veterinario.getNumLicencia() == null || veterinario.getNumLicencia().isBlank()) {
			return false;
		}

		if (veterinario.getNombre() == null || veterinario.getNombre().isBlank()) {
			return false;
		}

		if (veterinario.getApellidos() == null || veterinario.getApellidos().isBlank()) {
			return false;
		}

		if (veterinario.getFechaContratacion() == null) {
			return false;
		}

		return veterinarioDAO.actualizarVeterinario(veterinario);
	}

	
	
	public boolean eliminarVeterinario(String licencia) {

		if (licencia == null || licencia.isBlank()) {
			return false;
		}

		return veterinarioDAO.eliminarVeterinario(licencia);
	}
}

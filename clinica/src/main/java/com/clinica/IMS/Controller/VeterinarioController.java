package com.clinica.IMS.Controller;

import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Service.VeterinarioService;
/**
 * @author Miguel Ángel
 * Implementación métodos VeteinarioService
 */
public class VeterinarioController {
	private final VeterinarioService veterinarioService;

	public VeterinarioController() {
		this.veterinarioService = new VeterinarioService();
	}

	public boolean insertarVeterinario(Veterinario veterinario) {
		return veterinarioService.insertarVeterinario(veterinario);
	}

	public Veterinario obtenerVeterinarioPorLicencia(String licencia) {
		return veterinarioService.obtenerVeterinarioPorLicencia(licencia);
	}

	public boolean actualizarVeterinario(Veterinario veterinario) {
		return veterinarioService.actualizarVeterinario(veterinario);
	}

	public boolean eliminarVeterinario(String licencia) {
		return veterinarioService.eliminarVeterinario(licencia);
	}
}

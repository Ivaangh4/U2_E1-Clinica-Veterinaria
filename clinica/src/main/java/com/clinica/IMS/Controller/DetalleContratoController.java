package com.clinica.IMS.Controller;

import java.math.BigDecimal;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Service.DetalleContratoService;

public class DetalleContratoController {
	private final DetalleContratoService detalleContratoService;

	public DetalleContratoController() {
		this.detalleContratoService = new DetalleContratoService();
	}

	public boolean insertarContrato(DetalleContrato contrato) {
		return detalleContratoService.insertarContrato(contrato);
	}

	public DetalleContrato obtenerContratoPorId(int id) {
		return detalleContratoService.obtenerContratoPorId(id);
	}

	public DetalleContrato obtenerContratoPorLicencia(String licencia) {
		return detalleContratoService.obtenerContratoPorLicencia(licencia);
	}

	public boolean actualizarContrato(DetalleContrato contrato) {
		return detalleContratoService.actualizarContrato(contrato);
	}

	public boolean actualizarSalario(String licencia, BigDecimal nuevoSalario) {
		return detalleContratoService.actualizarSalario(licencia, nuevoSalario);
	}

	public boolean eliminarContrato(int id) {
		return detalleContratoService.eliminarContrato(id);
	}
}

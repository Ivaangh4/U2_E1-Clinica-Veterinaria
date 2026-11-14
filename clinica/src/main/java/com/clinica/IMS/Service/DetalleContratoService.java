package com.clinica.IMS.Service;

import java.math.BigDecimal;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Persistence.DetalleContratoDAO;

/**
 * @author Samuel
 * Clase donde se implementan todos los metodos Crud de DetalleContrato
 */
public class DetalleContratoService {
	private final DetalleContratoDAO detalleContratoDAO;

	public DetalleContratoService() {
		this.detalleContratoDAO = new DetalleContratoDAO();
	}

	public boolean insertarContrato(DetalleContrato contrato) {

		if (contrato == null) {
			return false;
		}

		if (contrato.getVeterinarioLicencia() == null || contrato.getVeterinarioLicencia().isBlank()) {
			return false;
		}

		if (contrato.getSalarioBase() == null || contrato.getSalarioBase().compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}

		if (contrato.getHorarioSemanal() <= 0) {
			return false;
		}

		DetalleContrato existente = detalleContratoDAO.obtenerContratoPorLicencia(contrato.getVeterinarioLicencia());

		if (existente != null) {
			return false;
		}

		return detalleContratoDAO.insertarContrato(contrato);
	}

	public DetalleContrato obtenerContratoPorId(int id) {

		if (id <= 0) {
			return null;
		}

		return detalleContratoDAO.obtenerContratoPorId(id);
	}

	public DetalleContrato obtenerContratoPorLicencia(String licencia) {

		if (licencia == null || licencia.isBlank()) {
			return null;
		}

		return detalleContratoDAO.obtenerContratoPorLicencia(licencia);
	}

	public boolean actualizarContrato(DetalleContrato contrato) {

		if (contrato == null) {
			return false;
		}

		if (contrato.getIdContrato() <= 0) {
			return false;
		}

		if (contrato.getSalarioBase() == null || contrato.getSalarioBase().compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}

		if (contrato.getHorarioSemanal() <= 0) {
			return false;
		}

		return detalleContratoDAO.actualizarContrato(contrato);
	}

	public boolean actualizarSalario(String licencia, BigDecimal nuevoSalario) {

		if (licencia == null || licencia.isBlank()) {
			return false;
		}

		if (nuevoSalario == null || nuevoSalario.compareTo(BigDecimal.ZERO) <= 0) {
			return false;
		}

		return detalleContratoDAO.actualizarSalario(licencia, nuevoSalario);
	}

	public boolean eliminarContrato(int id) {

		if (id <= 0) {
			return false;
		}

		return detalleContratoDAO.eliminarContrato(id);
	}
}
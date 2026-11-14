package com.clinica.IMS.Controller;

import java.util.List;

import com.clinica.IMS.Entity.Certificacion;
import com.clinica.IMS.Service.CertificacionService;

/**
 * @author Samuel
 * Implementacion de los metodos de CertificacionService
 */

public class CertificacionController {
	private final CertificacionService certificacionService;

	public CertificacionController() {
		this.certificacionService = new CertificacionService();
	}

	public boolean insertarCertificacion(Certificacion certificacion) {
		return certificacionService.insertarCertificacion(certificacion);
	}

	public boolean insertarCertificaciones(String licencia, List<Certificacion> certificaciones) {
		return certificacionService.insertarCertificaciones(licencia, certificaciones);
	}

	public Certificacion obtenerCertificacionPorId(int id) {
		return certificacionService.obtenerCertificacionPorId(id);
	}

	public List<Certificacion> obtenerCertificacionesPorLicencia(String licencia) {
		return certificacionService.obtenerCertificacionesPorLicencia(licencia);
	}

	public boolean actualizarCertificacion(Certificacion certificacion) {
		return certificacionService.actualizarCertificacion(certificacion);
	}

	public boolean eliminarCertificacion(int id) {
		return certificacionService.eliminarCertificacion(id);
	}
}
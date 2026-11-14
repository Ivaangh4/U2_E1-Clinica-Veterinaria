package com.clinica.IMS.Service;

import java.util.ArrayList;
import java.util.List;

import com.clinica.IMS.Entity.Certificacion;
import com.clinica.IMS.Persistence.CertificacionDAO;

public class CertificacionService {
	private final CertificacionDAO certificacionDAO;

	public CertificacionService() {
		this.certificacionDAO = new CertificacionDAO();
	}

	public boolean insertarCertificacion(Certificacion certificacion) {

		if (certificacion == null) {
			return false;
		}

		if (certificacion.getEntidadEmisora() == null || certificacion.getEntidadEmisora().isBlank()) {
			return false;
		}

		if (certificacion.getNombreEsp() == null || certificacion.getNombreEsp().isBlank()) {
			return false;
		}

		if (certificacion.getLicenciaVeterinario() == null || certificacion.getLicenciaVeterinario().isBlank()) {
			return false;
		}

		return certificacionDAO.insertarCertificacion(certificacion);
	}

	public Certificacion obtenerCertificacionPorId(int id) {

		if (id <= 0) {
			return null;
		}

		return certificacionDAO.obtenerCertificacionPorId(id);
	}

	public List<Certificacion> obtenerCertificacionesPorLicencia(String licencia) {

		if (licencia == null || licencia.isBlank()) {
			return new ArrayList<>();
		}

		return certificacionDAO.obtenerCertificacionesPorLicencia(licencia);
	}

	public boolean actualizarCertificacion(Certificacion certificacion) {

		if (certificacion == null) {
			return false;
		}

		if (certificacion.getIdCertificacion() <= 0) {
			return false;
		}

		if (certificacion.getEntidadEmisora() == null || certificacion.getEntidadEmisora().isBlank()) {
			return false;
		}

		if (certificacion.getNombreEsp() == null || certificacion.getNombreEsp().isBlank()) {
			return false;
		}

		return certificacionDAO.actualizarCertificacion(certificacion);
	}

	public boolean eliminarCertificacion(int id) {

		if (id <= 0) {
			return false;
		}

		return certificacionDAO.eliminarCertificacion(id);
	}
}
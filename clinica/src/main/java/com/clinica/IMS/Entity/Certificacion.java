package com.clinica.IMS.Entity;

public class Certificacion {
	private int idCertificacion;
	private String entidadEmisora;
	private String nombreEsp;
	private String licenciaVeterinario;
	
	public Certificacion(int idCertificacion, String entidadEmisora, String nombreEsp, String licenciaVeterinario) {
		super();
		this.idCertificacion = idCertificacion;
		this.entidadEmisora = entidadEmisora;
		this.nombreEsp = nombreEsp;
		this.licenciaVeterinario = licenciaVeterinario;
	}

	public int getIdCertificacion() {
		return idCertificacion;
	}

	public void setIdCertificacion(int idCertificacion) {
		this.idCertificacion = idCertificacion;
	}

	public String getEntidadEmisora() {
		return entidadEmisora;
	}

	public void setEntidadEmisora(String entidadEmisora) {
		this.entidadEmisora = entidadEmisora;
	}

	public String getNombreEsp() {
		return nombreEsp;
	}

	public void setNombreEsp(String nombreEsp) {
		this.nombreEsp = nombreEsp;
	}

	public String getLicenciaVeterinario() {
		return licenciaVeterinario;
	}

	public void setLicenciaVeterinario(String licenciaVeterinario) {
		this.licenciaVeterinario = licenciaVeterinario;
	}
	
	
	
	
}

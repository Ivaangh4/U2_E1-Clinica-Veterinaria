package com.clinica.IMS.Entity;

import java.sql.Date;

public class Veterinario {
	private String numLicencia;
	private String nombre;
	private String apellidos;
	private Date fechaContratacion;
	
	public Veterinario(String numLicencia, String nombre, String apellidos, Date fechaContratacion) {
		super();
		this.numLicencia = numLicencia;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaContratacion = fechaContratacion;
	}

	public String getNumLicencia() {
		return numLicencia;
	}

	public void setNumLicencia(String numLicencia) {
		this.numLicencia = numLicencia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Date getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(Date fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}
	
	
	
	
}

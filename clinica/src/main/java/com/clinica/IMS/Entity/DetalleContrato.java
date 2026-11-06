package com.clinica.IMS.Entity;

import java.math.BigDecimal;

public class DetalleContrato {
	private int idContrato;
	private BigDecimal salarioBase;
	private int horarioSemanal;
	private String veterinarioLicencia;
	
	public DetalleContrato(int idContrato, BigDecimal salarioBase, int horarioSemanal, String veterinarioLicencia) {
		super();
		this.idContrato = idContrato;
		this.salarioBase = salarioBase;
		this.horarioSemanal = horarioSemanal;
		this.veterinarioLicencia = veterinarioLicencia;
	}
	
	public int getIdContrato() {
		return idContrato;
	}
	public void setIdContrato(int idContrato) {
		this.idContrato = idContrato;
	}
	public BigDecimal getSalarioBase() {
		return salarioBase;
	}
	public void setSalarioBase(BigDecimal salarioBase) {
		this.salarioBase = salarioBase;
	}
	public int getHorarioSemanal() {
		return horarioSemanal;
	}
	public void setHorarioSemanal(int horarioSemanal) {
		this.horarioSemanal = horarioSemanal;
	}
	public String getVeterinarioLicencia() {
		return veterinarioLicencia;
	}
	public void setVeterinarioLicencia(String veterinarioLicencia) {
		this.veterinarioLicencia = veterinarioLicencia;
	}
	
	
	
	
	
}

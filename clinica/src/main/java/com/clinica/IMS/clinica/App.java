package com.clinica.IMS.clinica;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.clinica.IMS.Controller.CertificacionController;
import com.clinica.IMS.Controller.DetalleContratoController;
import com.clinica.IMS.Controller.VeterinarioController;
import com.clinica.IMS.Entity.Certificacion;
import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Persistence.ConnectionFactory;
import com.clinica.IMS.Persistence.DetalleContratoDAO;
import com.clinica.IMS.Persistence.VeterinarioDAO;

/**
 * @author Miguel
 * @author Ivan
 * @author Samuel
 * 
 */
public class App {

	private static final Logger logger = LogManager.getLogger(App.class);

	private static final String LIC_1 = "VET-101";
	private static final String LIC_2 = "VET-102";
	private static final String LIC_3 = "VET-103";

	private static final VeterinarioController vetController = new VeterinarioController();
	private static final DetalleContratoController contratoController = new DetalleContratoController();
	private static final CertificacionController certController = new CertificacionController();

	public static void main(String[] args) {
		try {
			logger.info("===== 0. LIMPIEZA INICIAL (para poder repetir la demo) =====");
			eliminarVeterinarioCompleto(LIC_1);
			eliminarVeterinarioCompleto(LIC_2);
			eliminarVeterinarioCompleto(LIC_3);

			logger.info("===== 1. ALTA DE VETERINARIOS CON CONTRATO Y CERTIFICACIONES =====");
			altaVeterinario(new Veterinario(LIC_1, "Laura", "Gómez Pérez", Date.valueOf("2024-02-01")),
					new DetalleContrato(0, new BigDecimal("2500.00"), 40, LIC_1),
					List.of(new Certificacion(0, "UCM", "Cirugía Felina", LIC_1),
							new Certificacion(0, "UAB", "Dermatología Canina", LIC_1)));

			altaVeterinario(new Veterinario(LIC_2, "Carlos", "Ruiz Soto", Date.valueOf("2025-05-15")),
					new DetalleContrato(0, new BigDecimal("2200.50"), 35, LIC_2),
					List.of(new Certificacion(0, "Universidad de Zaragoza", "Animales Exóticos", LIC_2)));

			altaVeterinario(new Veterinario(LIC_3, "Marta", "León Díaz", Date.valueOf("2026-01-10")),
					new DetalleContrato(0, new BigDecimal("2800.00"), 38, LIC_3),
					new ArrayList<>());

			listarTodo("LISTADO TRAS LAS ALTAS");

			logger.info("===== 2. SIMULACIÓN DE FALLOS TRANSACCIONALES (rollback registrado en Log4j2) =====");
			simularFallos();
			listarTodo("LISTADO TRAS LOS FALLOS (no debe haber cambiado nada)");

			logger.info("===== 3. MODIFICACIONES =====");
			logger.info("Salario de {} actualizado: {}", LIC_2,
					contratoController.actualizarSalario(LIC_2, new BigDecimal("2450.75")));

			Veterinario vet3 = vetController.obtenerVeterinarioPorLicencia(LIC_3);
			vet3.setApellidos("León Díaz-Torres");
			logger.info("Veterinario {} actualizado: {}", LIC_3, vetController.actualizarVeterinario(vet3));

			DetalleContrato contrato1 = contratoController.obtenerContratoPorLicencia(LIC_1);
			contrato1.setHorarioSemanal(30);
			logger.info("Horario del contrato de {} actualizado: {}", LIC_1,
					contratoController.actualizarContrato(contrato1));

			Certificacion cert = certController.obtenerCertificacionesPorLicencia(LIC_1).get(0);
			cert.setNombreEsp("Cirugía Felina Avanzada");
			logger.info("Certificación {} actualizada: {}", cert.getIdCertificacion(),
					certController.actualizarCertificacion(cert));

			listarTodo("LISTADO TRAS LAS MODIFICACIONES");

			logger.info("===== 4. BORRADOS =====");
			Certificacion aBorrar = certController.obtenerCertificacionesPorLicencia(LIC_1).get(1);
			logger.info("Certificación {} borrada: {}", aBorrar.getIdCertificacion(),
					certController.eliminarCertificacion(aBorrar.getIdCertificacion()));

			logger.info("Veterinario {} borrado (certificaciones y contrato incluidos): {}", LIC_3,
					eliminarVeterinarioCompleto(LIC_3));

			listarTodo("LISTADO FINAL");

		} catch (Exception e) {
			logger.error("Error inesperado durante la demostración", e);
		} finally {
			ConnectionFactory.closePool();
			logger.info("Pool de conexiones cerrado. Fin de la demostración.");
		}
	}

	private static void altaVeterinario(Veterinario vet, DetalleContrato contrato, List<Certificacion> certs) {
		boolean vetOk = vetController.insertarVeterinario(vet);
		boolean contratoOk = vetOk && contratoController.insertarContrato(contrato);
		boolean certsOk = true;
		if (!certs.isEmpty()) {
			certsOk = certController.insertarCertificaciones(vet.getNumLicencia(), new ArrayList<>(certs));
		}
		logger.info("Alta de {}: veterinario={}, contrato={}, certificaciones={}", vet.getNumLicencia(), vetOk,
				contratoOk, certsOk);
	}

	/**
	 * Se usan los DAO directamente para saltarse las validaciones de la capa de
	 * servicio y provocar un error SQL real en la base de datos.
	 */
	private static void simularFallos() {
		DetalleContratoDAO contratoDAO = new DetalleContratoDAO();
		VeterinarioDAO vetDAO = new VeterinarioDAO();

		// Fallo 1: segundo contrato para el mismo veterinario -> viola el UNIQUE (1:1)
		DetalleContrato duplicado = new DetalleContrato(0, new BigDecimal("9999.99"), 10, LIC_1);
		logger.info("Intento de segundo contrato para {} (debe fallar): insertado={}", LIC_1,
				contratoDAO.insertarContrato(duplicado));

		// Fallo 2: salario nulo -> viola NOT NULL
		DetalleContrato invalido = new DetalleContrato(0, null, 40, LIC_2);
		logger.info("Intento de contrato con salario nulo (debe fallar): insertado={}",
				contratoDAO.insertarContrato(invalido));

		// Fallo 3: veterinario con clave primaria repetida
		Veterinario repetido = new Veterinario(LIC_1, "Intruso", "Repetido", Date.valueOf("2026-01-01"));
		logger.info("Intento de veterinario con licencia repetida (debe fallar): insertado={}",
				vetDAO.insertarVeterinario(repetido));
	}

	/** Borra certificaciones, contrato y veterinario (en este orden). */
	private static boolean eliminarVeterinarioCompleto(String licencia) {
		for (Certificacion c : certController.obtenerCertificacionesPorLicencia(licencia)) {
			certController.eliminarCertificacion(c.getIdCertificacion());
		}
		DetalleContrato contrato = contratoController.obtenerContratoPorLicencia(licencia);
		if (contrato != null) {
			contratoController.eliminarContrato(contrato.getIdContrato());
		}
		return vetController.eliminarVeterinario(licencia);
	}

	private static void listarTodo(String titulo) {
		logger.info("----- {} -----", titulo);
		for (String licencia : new String[] { LIC_1, LIC_2, LIC_3 }) {
			Veterinario v = vetController.obtenerVeterinarioPorLicencia(licencia);
			if (v == null) {
				logger.info("{}: no existe", licencia);
				continue;
			}
			logger.info("Veterinario: {} - {} {} (contratado el {})", v.getNumLicencia(), v.getNombre(),
					v.getApellidos(), v.getFechaContratacion());

			DetalleContrato c = contratoController.obtenerContratoPorLicencia(licencia);
			if (c != null) {
				logger.info("   Contrato {}: salario={} horas/semana={}", c.getIdContrato(), c.getSalarioBase(),
						c.getHorarioSemanal());
			}
			for (Certificacion cert : certController.obtenerCertificacionesPorLicencia(licencia)) {
				logger.info("   Certificación {}: {} ({})", cert.getIdCertificacion(), cert.getNombreEsp(),
						cert.getEntidadEmisora());
			}
		}
	}
}
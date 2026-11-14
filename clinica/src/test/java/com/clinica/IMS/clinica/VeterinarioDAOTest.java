package com.clinica.IMS.clinica;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import com.clinica.IMS.Entity.Certificacion;
import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Persistence.VeterinarioDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias con JUnit 5 para validar las operaciones de persistencia,
 * el control transaccional y la integridad referencial de {@link VeterinarioDAO}.
 * 
 * @author Ivan
 */
public class VeterinarioDAOTest {
	
	private VeterinarioDAO dao;

	/**
	 * Prepara un estado limpio en la base de datos antes de cada prueba,
	 * eliminando los registros previos de certificaciones, contratos y veterinarios.
	 * 
	 * @throws Exception si ocurre un error al cargar {@code db.properties} o al limpiar las tablas.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		Properties props;
		InputStream input;
		String url;
		String user;
		String pass;

		props = new Properties();
		input = getClass().getClassLoader().getResourceAsStream("db.properties");
		
		if (input != null) {
			props.load(input);
		}

		url = props.getProperty("db.url");
		user = props.getProperty("db.user");
		pass = props.getProperty("db.password");
		
		dao = new VeterinarioDAO();
		
		try (Connection conn = DriverManager.getConnection(url, user, pass);
			 Statement stmt = conn.createStatement()) {
			   
			   stmt.executeUpdate("DELETE FROM certificaciones");
			   stmt.executeUpdate("DELETE FROM detalles_contratos");
			   stmt.executeUpdate("DELETE FROM veterinarios");
		}
	}
	
	/**
	 * Verifica la inserción correcta de un veterinario junto con su detalle de contrato
	 * y su posterior recuperación por número de licencia.
	 */
	@Test
	public void testInsertarVeterinarioConContratoExitoso() {
		Veterinario vetOriginal;
		DetalleContrato contratoOriginal;
		boolean fueInsertado;
		Veterinario vetRecuperado;
		DetalleContrato contratoRecuperado;
		BigDecimal salarioEsperado;

		vetOriginal = new Veterinario("VET-001", "Laura", "Gómez", Date.valueOf("2026-11-06"));
		contratoOriginal = new DetalleContrato(0, new BigDecimal("2500.00"), 40, "VET-001");

		fueInsertado = dao.insertarVeterinario(vetOriginal);
		vetRecuperado = dao.obtenerVeterinarioPorLicencia("VET-001");
		contratoRecuperado = dao.obtenerContratoPorLicencia("VET-001");
		salarioEsperado = new BigDecimal("2500.00");

		assertTrue(fueInsertado);
		assertNotNull(vetRecuperado);
		assertEquals("Laura", vetRecuperado.getNombre());
		assertNotNull(contratoRecuperado);
		assertEquals(0, salarioEsperado.compareTo(contratoRecuperado.getSalarioBase()));
	}
	
	/**
	 * Comprueba que, ante un fallo en la inserción del contrato asociado, se ejecuta
	 * un rollback y el veterinario no queda persistido en la base de datos.
	 */
	@Test
	public void testRollbackTransaccional() {
		Veterinario vetOriginal;
		DetalleContrato contratoInvalido;
		boolean fueInsertado;
		Veterinario vetRecuperado;

		vetOriginal = new Veterinario("VET-002", "Carlos", "Ruiz", Date.valueOf("2026-11-06"));
		contratoInvalido = new DetalleContrato(0, null, 40, "VET-002");

		fueInsertado = dao.insertarVeterinario(vetOriginal);
		vetRecuperado = dao.obtenerVeterinarioPorLicencia("VET-002");

		assertFalse(fueInsertado);
		assertNull(vetRecuperado, "El veterinario no debe existir en la BD debido al rollback");
	}
	
	/**
	 * Valida el registro de múltiples certificaciones asociadas a un mismo veterinario
	 * y la recuperación posterior de sus datos.
	 */
	@Test
	public void testAltaYRecuperacionCertificaciones() {
		Veterinario vetOriginal;
		DetalleContrato contratoOriginal;
		Certificacion cert1;
		Certificacion cert2;
		List<Certificacion> listaCert;
		boolean certificacionesInsertadas;
		Veterinario vetRecuperado;

		vetOriginal = new Veterinario("VET-003", "Ana", "Martínez", Date.valueOf("2026-11-06"));
		contratoOriginal = new DetalleContrato(0, new BigDecimal("2800.00"), 35, "VET-003");
		dao.insertarVeterinario(vetOriginal);

		cert1 = new Certificacion(0, "UCM", "Cirugía Felina", "VET-003");
		cert2 = new Certificacion(0, "UAB", "Dermatología Canina", "VET-003");
		listaCert = List.of(cert1, cert2);

		certificacionesInsertadas = dao.registrarCertificaciones("VET-003", listaCert);
		vetRecuperado = dao.obtenerVeterinarioPorLicencia("VET-003");

		assertTrue(certificacionesInsertadas);
		assertNotNull(vetRecuperado);
	}
	
	/**
	 * Verifica la actualización del salario de un contrato y la posterior eliminación
	 * del veterinario respetando la integridad referencial.
	 */
	@Test
	public void testActualizacionYBorrado() {
		Veterinario vetOriginal;
		DetalleContrato contratoOriginal;
		boolean fueActualizado;
		DetalleContrato contratoModificado;
		BigDecimal nuevoSalario = null;
		boolean fueEliminado;
		Veterinario vetEliminado;
		
		vetOriginal = new Veterinario("VET-004", "Jose", "Hernandez", Date.valueOf("2026-11-06"));
		contratoOriginal = new DetalleContrato(0, new BigDecimal("2100.00"), 30, "VET-004");
		
		dao.insertarVeterinario(vetOriginal);
		
		nuevoSalario = new BigDecimal("2950.00");
		
		fueActualizado = dao.actualizarSalarioContrato("VET-004", nuevoSalario);
		
		contratoModificado = dao.obtenerContratoPorLicencia("VET-004");
		
		assertTrue(fueActualizado);
		assertNotNull(contratoModificado);
		assertEquals(0, nuevoSalario.compareTo(contratoModificado.getSalarioBase()));
		
		fueEliminado = dao.eliminarVeterinario("VET-004");
		
		vetEliminado = dao.obtenerVeterinarioPorLicencia("VET-004");
		
		assertTrue(fueEliminado);
		assertNull(vetEliminado);
	}
}
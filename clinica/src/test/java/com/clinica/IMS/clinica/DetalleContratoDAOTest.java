package com.clinica.IMS.clinica;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Persistence.DetalleContratoDAO;
import com.clinica.IMS.Persistence.VeterinarioDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class DetalleContratoDAOTest {

	private DetalleContratoDAO dao;
	private VeterinarioDAO vetDao;

	/**
	 * Rigorous Test :-)
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

		dao = new DetalleContratoDAO();
		vetDao = new VeterinarioDAO();

		try (Connection conn = DriverManager.getConnection(url, user, pass);
			 Statement stmt = conn.createStatement()) {

			stmt.executeUpdate("DELETE FROM certificaciones");
			stmt.executeUpdate("DELETE FROM detalles_contratos");
			stmt.executeUpdate("DELETE FROM veterinarios");
		}
	}

	@Test
	public void testInsertarContratoExitoso() {
		Veterinario vetOriginal;
		DetalleContrato contratoOriginal;
		boolean fueInsertado;
		DetalleContrato contratoPorLicencia;
		DetalleContrato contratoPorId;
		BigDecimal salarioEsperado;

		vetOriginal = new Veterinario("VET-001", "Laura", "Gómez", Date.valueOf("2026-11-06"));
		contratoOriginal = new DetalleContrato(0, new BigDecimal("2500.00"), 40, "VET-001");

		vetDao.insertarVeterinario(vetOriginal);
		fueInsertado = dao.insertarContrato(contratoOriginal);
		contratoPorLicencia = dao.obtenerContratoPorLicencia("VET-001");
		contratoPorId = dao.obtenerContratoPorId(contratoOriginal.getIdContrato());
		salarioEsperado = new BigDecimal("2500.00");

		assertTrue(fueInsertado);
		assertNotNull(contratoPorLicencia);
		assertNotNull(contratoPorId);
		assertEquals(0, salarioEsperado.compareTo(contratoPorLicencia.getSalarioBase()));
		assertEquals(40, contratoPorLicencia.getHorarioSemanal());
	}

	@Test
	public void testRollbackTransaccional() {
		Veterinario vetOriginal;
		DetalleContrato contratoInvalido;
		boolean fueInsertado;
		DetalleContrato contratoRecuperado;

		vetOriginal = new Veterinario("VET-002", "Carlos", "Ruiz", Date.valueOf("2026-11-06"));
		contratoInvalido = new DetalleContrato(0, null, 40, "VET-002");

		vetDao.insertarVeterinario(vetOriginal);
		fueInsertado = dao.insertarContrato(contratoInvalido);
		contratoRecuperado = dao.obtenerContratoPorLicencia("VET-002");

		assertFalse(fueInsertado);
		assertNull(contratoRecuperado, "El contrato no debe existir en la BD debido al rollback");
	}

	@Test
	public void testActualizarSalarioYRestriccionUnique() {
		Veterinario vetOriginal;
		DetalleContrato contratoOriginal;
		DetalleContrato contratoDuplicado;
		boolean duplicadoInsertado;
		BigDecimal nuevoSalario;
		boolean salarioActualizado;
		DetalleContrato contratoRecuperado;

		vetOriginal = new Veterinario("VET-003", "Ana", "Martínez", Date.valueOf("2026-11-06"));
		contratoOriginal = new DetalleContrato(0, new BigDecimal("2800.00"), 35, "VET-003");
		contratoDuplicado = new DetalleContrato(0, new BigDecimal("3000.00"), 40, "VET-003");

		vetDao.insertarVeterinario(vetOriginal);
		dao.insertarContrato(contratoOriginal);
		duplicadoInsertado = dao.insertarContrato(contratoDuplicado);

		nuevoSalario = new BigDecimal("3150.00");
		salarioActualizado = dao.actualizarSalario("VET-003", nuevoSalario);
		contratoRecuperado = dao.obtenerContratoPorLicencia("VET-003");

		assertFalse(duplicadoInsertado, "No debe permitir dos contratos para la misma licencia");
		assertTrue(salarioActualizado);
		assertNotNull(contratoRecuperado);
		assertEquals(0, nuevoSalario.compareTo(contratoRecuperado.getSalarioBase()));
	}

	@Test
	public void testActualizacionYBorrado() {
		Veterinario vetOriginal;
		DetalleContrato contratoOriginal;
		boolean fueActualizado;
		DetalleContrato contratoModificado;
		BigDecimal nuevoSalario;
		boolean fueEliminado;
		DetalleContrato contratoEliminado;

		vetOriginal = new Veterinario("VET-004", "Jose", "Hernandez", Date.valueOf("2026-11-06"));
		contratoOriginal = new DetalleContrato(0, new BigDecimal("2100.00"), 30, "VET-004");

		vetDao.insertarVeterinario(vetOriginal);
		dao.insertarContrato(contratoOriginal);

		nuevoSalario = new BigDecimal("2950.00");
		contratoOriginal.setSalarioBase(nuevoSalario);
		contratoOriginal.setHorarioSemanal(38);

		fueActualizado = dao.actualizarContrato(contratoOriginal);
		contratoModificado = dao.obtenerContratoPorLicencia("VET-004");

		assertTrue(fueActualizado);
		assertNotNull(contratoModificado);
		assertEquals(0, nuevoSalario.compareTo(contratoModificado.getSalarioBase()));
		assertEquals(38, contratoModificado.getHorarioSemanal());

		fueEliminado = dao.eliminarContrato(contratoOriginal.getIdContrato());
		contratoEliminado = dao.obtenerContratoPorLicencia("VET-004");

		assertTrue(fueEliminado);
		assertNull(contratoEliminado);
	}
}
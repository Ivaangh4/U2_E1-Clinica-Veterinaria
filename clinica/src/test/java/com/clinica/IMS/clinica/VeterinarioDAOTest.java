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
 * Unit test for simple App.
 */
public class VeterinarioDAOTest {

	private VeterinarioDAO dao;

	@BeforeEach
	public void setUp() throws Exception {

		Properties props = new Properties();

		try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {

			if (input == null) {
				throw new Exception("No se encuentra db.properties");
			}

			props.load(input);
		}

		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String pass = props.getProperty("db.password");

		try (Connection conn = DriverManager.getConnection(url, user, pass); Statement stmt = conn.createStatement()) {

			stmt.executeUpdate("DELETE FROM certificaciones");
			stmt.executeUpdate("DELETE FROM detalles_contratos");
			stmt.executeUpdate("DELETE FROM veterinarios");
		}

		dao = new VeterinarioDAO();
	}

	@Test
	public void testInsertarYObtenerVeterinario() {

		Veterinario veterinario = new Veterinario("VET-001", "Laura", "Gómez", Date.valueOf("2026-11-06"));

		boolean insertado = dao.insertarVeterinario(veterinario);

		Veterinario recuperado = dao.obtenerVeterinarioPorLicencia("VET-001");

		assertTrue(insertado);
		assertNotNull(recuperado);

		assertEquals("VET-001", recuperado.getNumLicencia());
		assertEquals("Laura", recuperado.getNombre());
		assertEquals("Gómez", recuperado.getApellidos());
		assertEquals(Date.valueOf("2026-11-06"), recuperado.getFechaContratacion());
	}

	@Test
	public void testObtenerVeterinarioInexistente() {

		Veterinario recuperado = dao.obtenerVeterinarioPorLicencia("VET-999");

		assertNull(recuperado);
	}

	@Test
	public void testActualizarVeterinario() {

		Veterinario veterinario = new Veterinario("VET-002", "Carlos", "Ruiz", Date.valueOf("2026-11-06"));

		dao.insertarVeterinario(veterinario);

		Veterinario veterinarioModificado = new Veterinario("VET-002", "Carlos", "Fernández",
				Date.valueOf("2026-11-10"));

		boolean actualizado = dao.actualizarVeterinario(veterinarioModificado);

		Veterinario recuperado = dao.obtenerVeterinarioPorLicencia("VET-002");

		assertTrue(actualizado);
		assertNotNull(recuperado);

		assertEquals("Carlos", recuperado.getNombre());
		assertEquals("Fernández", recuperado.getApellidos());
		assertEquals(Date.valueOf("2026-11-10"), recuperado.getFechaContratacion());
	}

	@Test
	public void testEliminarVeterinario() {

		Veterinario veterinario = new Veterinario("VET-003", "Ana", "Martínez", Date.valueOf("2026-11-06"));

		dao.insertarVeterinario(veterinario);

		boolean eliminado = dao.eliminarVeterinario("VET-003");

		Veterinario recuperado = dao.obtenerVeterinarioPorLicencia("VET-003");

		assertTrue(eliminado);
		assertNull(recuperado);
	}

	@Test
	public void testActualizarVeterinarioInexistente() {

		Veterinario veterinario = new Veterinario("VET-999", "Pedro", "López", Date.valueOf("2026-11-06"));

		boolean actualizado = dao.actualizarVeterinario(veterinario);

		assertFalse(actualizado);
	}

	@Test
	public void testEliminarVeterinarioInexistente() {

		boolean eliminado = dao.eliminarVeterinario("VET-999");

		assertFalse(eliminado);
	}

}

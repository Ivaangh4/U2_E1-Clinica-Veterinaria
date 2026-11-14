package com.clinica.IMS.clinica;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Persistence.VeterinarioDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias con JUnit 5 para validar las operaciones CRUD,
 * el control transaccional y las restricciones de integridad de {@link VeterinarioDAO}.
 * 
 * @author Ivan
 */
public class VeterinarioDAOTest {
	
	private VeterinarioDAO dao;

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
	

	@Test
	public void testInsertarVeterinarioExitoso() {
		Veterinario vetOriginal;
		boolean fueInsertado;
		Veterinario vetRecuperado;

		vetOriginal = new Veterinario("VET-001", "Laura", "Gómez", Date.valueOf("2026-11-06"));

		fueInsertado = dao.insertarVeterinario(vetOriginal);
		vetRecuperado = dao.obtenerVeterinarioPorLicencia("VET-001");

		assertTrue(fueInsertado);
		assertNotNull(vetRecuperado);
		assertEquals("Laura", vetRecuperado.getNombre());
		assertEquals("Gómez", vetRecuperado.getApellidos());
	}
	

	@Test
	public void testRollbackTransaccional() {
		Veterinario vetInvalido;
		boolean fueInsertado;
		Veterinario vetRecuperado;

		vetInvalido = new Veterinario("VET-002", null, "Ruiz", Date.valueOf("2026-11-06"));

		fueInsertado = dao.insertarVeterinario(vetInvalido);
		vetRecuperado = dao.obtenerVeterinarioPorLicencia("VET-002");

		assertFalse(fueInsertado);
		assertNull(vetRecuperado, "El veterinario no debe existir en la BD debido al rollback");
	}
	

	@Test
	public void testLicenciaDuplicadaFalla() {
		Veterinario vet1;
		Veterinario vet2;
		boolean insertado1;
		boolean insertado2;

		vet1 = new Veterinario("VET-003", "Ana", "Martínez", Date.valueOf("2026-11-06"));
		vet2 = new Veterinario("VET-003", "Pedro", "López", Date.valueOf("2026-11-06"));

		insertado1 = dao.insertarVeterinario(vet1);
		insertado2 = dao.insertarVeterinario(vet2);

		assertTrue(insertado1);
		assertFalse(insertado2, "No debe permitir insertar dos veterinarios con la misma licencia");
	}
	

	@Test
	public void testActualizacionYBorrado() {
		Veterinario vetOriginal;
		boolean fueActualizado;
		Veterinario vetModificado;
		boolean fueEliminado;
		Veterinario vetEliminado;
		
		vetOriginal = new Veterinario("VET-004", "Jose", "Hernandez", Date.valueOf("2026-11-06"));
		dao.insertarVeterinario(vetOriginal);
		
		vetOriginal.setNombre("José Antonio");
		vetOriginal.setApellidos("Hernández García");
		
		fueActualizado = dao.actualizarVeterinario(vetOriginal);
		vetModificado = dao.obtenerVeterinarioPorLicencia("VET-004");
		
		assertTrue(fueActualizado);
		assertNotNull(vetModificado);
		assertEquals("José Antonio", vetModificado.getNombre());
		assertEquals("Hernández García", vetModificado.getApellidos());
		
		fueEliminado = dao.eliminarVeterinario("VET-004");
		vetEliminado = dao.obtenerVeterinarioPorLicencia("VET-004");
		
		assertTrue(fueEliminado);
		assertNull(vetEliminado);
	}
}
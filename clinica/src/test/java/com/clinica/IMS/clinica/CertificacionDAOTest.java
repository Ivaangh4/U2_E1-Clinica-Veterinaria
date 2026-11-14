package com.clinica.IMS.clinica;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import com.clinica.IMS.Entity.Certificacion;
import com.clinica.IMS.Entity.Veterinario;
import com.clinica.IMS.Persistence.CertificacionDAO;
import com.clinica.IMS.Persistence.VeterinarioDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias con JUnit 5 para comprobar las operaciones CRUD,
 * la gestión de rollback ante datos inválidos y la recuperación de especialidades
 * por licencia en {@link CertificacionDAO}.
 * 
 * @author Ivan
 */
public class CertificacionDAOTest {

	private CertificacionDAO dao;
	private VeterinarioDAO vetDao;

	/**
	 * Restaura un estado limpio en la base de datos e instancia los DAOs
	 * antes de cada caso de prueba.
	 * 
	 * @throws Exception si ocurre algún problema al leer {@code db.properties} o ejecutar el borrado inicial.
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

		dao = new CertificacionDAO();
		vetDao = new VeterinarioDAO();

		try (Connection conn = DriverManager.getConnection(url, user, pass);
			 Statement stmt = conn.createStatement()) {

			stmt.executeUpdate("DELETE FROM certificaciones");
			stmt.executeUpdate("DELETE FROM detalles_contratos");
			stmt.executeUpdate("DELETE FROM veterinarios");
		}
	}

	/**
	 * Verifica la inserción de una certificación asociada a un veterinario
	 * y su recuperación exacta mediante el ID autogenerado.
	 */
	@Test
	public void testInsertarCertificacionExitoso() {
		Veterinario vetOriginal;
		Certificacion certOriginal;
		boolean fueInsertada;
		Certificacion certRecuperada;

		vetOriginal = new Veterinario("VET-001", "Laura", "Gómez", Date.valueOf("2026-11-06"));
		certOriginal = new Certificacion(0, "UCM", "Cirugía Felina", "VET-001");

		vetDao.insertarVeterinario(vetOriginal);
		fueInsertada = dao.insertarCertificacion(certOriginal);
		certRecuperada = dao.obtenerCertificacionPorId(certOriginal.getIdCertificacion());

		assertTrue(fueInsertada);
		assertNotNull(certRecuperada);
		assertEquals("UCM", certRecuperada.getEntidadEmisora());
		assertEquals("Cirugía Felina", certRecuperada.getNombreEsp());
	}

	/**
	 * Comprueba que al intentar insertar una certificación con especialidad nula,
	 * se ejecuta el rollback correspondiente y no se guarda ningún registro.
	 */
	@Test
	public void testRollbackTransaccional() {
		Veterinario vetOriginal;
		Certificacion certInvalida;
		boolean fueInsertada;
		List<Certificacion> listaRecuperada;

		vetOriginal = new Veterinario("VET-002", "Carlos", "Ruiz", Date.valueOf("2026-11-06"));
		certInvalida = new Certificacion(0, "UCM", null, "VET-002");

		vetDao.insertarVeterinario(vetOriginal);
		fueInsertada = dao.insertarCertificacion(certInvalida);
		listaRecuperada = dao.obtenerCertificacionesPorLicencia("VET-002");

		assertFalse(fueInsertada);
		assertTrue(listaRecuperada.isEmpty(), "La certificación no debe existir en la BD debido al rollback");
	}

	/**
	 * Valida el alta de varias certificaciones para un mismo veterinario
	 * y su recuperación conjunta filtrando por número de licencia.
	 */
	@Test
	public void testAltaYRecuperacionCertificaciones() {
		Veterinario vetOriginal;
		Certificacion cert1;
		Certificacion cert2;
		boolean insertada1;
		boolean insertada2;
		List<Certificacion> listaRecuperada;

		vetOriginal = new Veterinario("VET-003", "Ana", "Martínez", Date.valueOf("2026-11-06"));
		vetDao.insertarVeterinario(vetOriginal);

		cert1 = new Certificacion(0, "UCM", "Cirugía Felina", "VET-003");
		cert2 = new Certificacion(0, "UAB", "Dermatología Canina", "VET-003");

		insertada1 = dao.insertarCertificacion(cert1);
		insertada2 = dao.insertarCertificacion(cert2);
		listaRecuperada = dao.obtenerCertificacionesPorLicencia("VET-003");

		assertTrue(insertada1);
		assertTrue(insertada2);
		assertNotNull(listaRecuperada);
		assertEquals(2, listaRecuperada.size());
	}

	/**
	 * Verifica la modificación de la institución emisora y especialidad de una certificación,
	 * así como su eliminación definitiva por identificador.
	 */
	@Test
	public void testActualizacionYBorrado() {
		Veterinario vetOriginal;
		Certificacion certOriginal;
		boolean fueActualizada;
		Certificacion certModificada;
		boolean fueEliminada;
		Certificacion certEliminada;

		vetOriginal = new Veterinario("VET-004", "Jose", "Hernandez", Date.valueOf("2026-11-06"));
		certOriginal = new Certificacion(0, "UCM", "Traumatología", "VET-004");

		vetDao.insertarVeterinario(vetOriginal);
		dao.insertarCertificacion(certOriginal);

		certOriginal.setEntidadEmisora("Universidad Complutense");
		certOriginal.setNombreEsp("Traumatología Avanzada");

		fueActualizada = dao.actualizarCertificacion(certOriginal);
		certModificada = dao.obtenerCertificacionPorId(certOriginal.getIdCertificacion());

		assertTrue(fueActualizada);
		assertNotNull(certModificada);
		assertEquals("Universidad Complutense", certModificada.getEntidadEmisora());
		assertEquals("Traumatología Avanzada", certModificada.getNombreEsp());

		fueEliminada = dao.eliminarCertificacion(certOriginal.getIdCertificacion());
		certEliminada = dao.obtenerCertificacionPorId(certOriginal.getIdCertificacion());

		assertTrue(fueEliminada);
		assertNull(certEliminada);
	}
}
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
import com.clinica.IMS.Persistence.VeterinarioDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class VeterinarioDAOTest {
	
	private VeterinarioDAO dao;

    /**
     * Rigorous Test :-)
     */
	@BeforeEach
    public void setUp() throws Exception{
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
    public void testInsertarVeterinarioConContratoExitoso() {
        Veterinario vetOriginal;
        DetalleContrato contratoOriginal;
        boolean fueInsertado;
        Veterinario vetRecuperado;
        DetalleContrato contratoRecuperado;
        BigDecimal salarioEsperado;

        vetOriginal = new Veterinario("VET-001", "Laura", "Gómez", Date.valueOf("2026-11-06"));
        contratoOriginal = new DetalleContrato(0, new BigDecimal("2500.00"), 40, "VET-001");

        fueInsertado = dao.insertarVeterinarioConContrato(vetOriginal, contratoOriginal);
        vetRecuperado = dao.obtenerVeterinarioPorLicencia("VET-001");
        contratoRecuperado = dao.obtenerContratoPorLicencia("VET-001");
        salarioEsperado = new BigDecimal("2500.00");

        assertTrue(fueInsertado);
        assertNotNull(vetRecuperado);
        assertEquals("Laura", vetRecuperado.getNombre());
        assertNotNull(contratoRecuperado);
        assertEquals(0, salarioEsperado.compareTo(contratoRecuperado.getSalarioBase()));
    }
}

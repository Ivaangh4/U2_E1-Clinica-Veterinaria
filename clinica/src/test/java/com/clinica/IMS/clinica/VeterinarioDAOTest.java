package com.clinica.IMS.clinica;


import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Date;

import com.clinica.IMS.Entity.DetalleContrato;
import com.clinica.IMS.Entity.Veterinario;

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
    public void setUp() {
        dao = new VeterinarioDAO();
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

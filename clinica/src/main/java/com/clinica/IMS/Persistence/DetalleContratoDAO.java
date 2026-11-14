package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.DetalleContrato;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Samuel
 * Capa encargada de hacer todas las operaciones CRUD
 */
public class DetalleContratoDAO {

    private static final Logger logger =
            LogManager.getLogger(DetalleContratoDAO.class);

    private static final String SQL_INSERT =
            "INSERT INTO detalles_contratos "
            + "(salario_base, horario_semanal, veterinario_licencia) "
            + "VALUES (?, ?, ?)";

    private static final String SQL_GET_BY_ID =
            "SELECT id, salario_base, horario_semanal, "
            + "veterinario_licencia FROM detalles_contratos WHERE id = ?";

    private static final String SQL_GET_BY_LICENCE =
            "SELECT id, salario_base, horario_semanal, "
            + "veterinario_licencia FROM detalles_contratos "
            + "WHERE veterinario_licencia = ?";

    private static final String SQL_UPDATE =
            "UPDATE detalles_contratos SET salario_base = ?, "
            + "horario_semanal = ? WHERE id = ?";

    private static final String SQL_UPDATE_SALARIO =
            "UPDATE detalles_contratos SET salario_base = ? "
            + "WHERE veterinario_licencia = ?";

    private static final String SQL_DELETE =
            "DELETE FROM detalles_contratos WHERE id = ?";

    public DetalleContratoDAO() {
    }

    public boolean insertarContrato(DetalleContrato contrato) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setBigDecimal(1, contrato.getSalarioBase());
            ps.setInt(2, contrato.getHorarioSemanal());
            ps.setString(3, contrato.getVeterinarioLicencia());

            if (ps.executeUpdate() > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        contrato.setIdContrato(rs.getInt(1));
                    }
                }

                logger.info("Contrato insertado correctamente");
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al insertar contrato", e);
        }

        return false;
    }

    public DetalleContrato obtenerContratoPorId(int id) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    logger.info("Contrato encontrado");

                    return new DetalleContrato(
                            rs.getInt("id"),
                            rs.getBigDecimal("salario_base"),
                            rs.getInt("horario_semanal"),
                            rs.getString("veterinario_licencia")
                    );
                }
            }

            logger.warn("Contrato no encontrado");

        } catch (SQLException e) {
            logger.error("Error al obtener contrato", e);
        }

        return null;
    }

    public DetalleContrato obtenerContratoPorLicencia(String licencia) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_LICENCE)) {

            ps.setString(1, licencia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    logger.info("Contrato encontrado");

                    return new DetalleContrato(
                            rs.getInt("id"),
                            rs.getBigDecimal("salario_base"),
                            rs.getInt("horario_semanal"),
                            rs.getString("veterinario_licencia")
                    );
                }
            }

            logger.warn("Contrato no encontrado para la licencia: {}", licencia);

        } catch (SQLException e) {
            logger.error("Error al obtener contrato por licencia", e);
        }

        return null;
    }

    public boolean actualizarContrato(DetalleContrato contrato) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE)) {

            ps.setBigDecimal(1, contrato.getSalarioBase());
            ps.setInt(2, contrato.getHorarioSemanal());
            ps.setInt(3, contrato.getIdContrato());

            if (ps.executeUpdate() > 0) {

                logger.info("Contrato actualizado correctamente");
                return true;
            }

            logger.warn("No se encontró el contrato con id: {}",
                    contrato.getIdContrato());

        } catch (SQLException e) {
            logger.error("Error al actualizar contrato", e);
        }

        return false;
    }

    public boolean actualizarSalario(String licencia, BigDecimal nuevoSalario) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_SALARIO)) {

            ps.setBigDecimal(1, nuevoSalario);
            ps.setString(2, licencia);

            if (ps.executeUpdate() > 0) {

                logger.info("Salario actualizado correctamente");
                return true;
            }

            logger.warn("No se encontró contrato para la licencia: {}", licencia);

        } catch (SQLException e) {
            logger.error("Error al actualizar salario", e);
        }

        return false;
    }

    public boolean eliminarContrato(int id) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {

                logger.info("Contrato borrado correctamente");
                return true;
            }

            logger.warn("No se encontró el contrato con id: {}", id);

        } catch (SQLException e) {
            logger.error("Error al borrar contrato", e);
        }

        return false;
    }
}
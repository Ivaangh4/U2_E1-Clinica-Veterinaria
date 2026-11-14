package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.Veterinario;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Miguel Ángel
 * Capa encargada de hacer todas las operaciones CRUD
 */
public class VeterinarioDAO {

    private static final Logger logger =
            LogManager.getLogger(VeterinarioDAO.class);

    private static final String SQL_INSERT =
            "INSERT INTO veterinarios "
            + "(num_licencia, nombre, apellidos, fecha_contratacion) "
            + "VALUES (?, ?, ?, ?)";

    private static final String SQL_GET_BY_LICENCE =
            "SELECT num_licencia, nombre, apellidos, fecha_contratacion "
            + "FROM veterinarios WHERE num_licencia = ?";

    private static final String SQL_DELETE =
            "DELETE FROM veterinarios WHERE num_licencia = ?";

    private static final String SQL_UPDATE =
            "UPDATE veterinarios SET nombre = ?, apellidos = ?, "
            + "fecha_contratacion = ? WHERE num_licencia = ?";

    public VeterinarioDAO() {
    }

    public boolean insertarVeterinario(Veterinario vetOriginal) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {

            ps.setString(1, vetOriginal.getNumLicencia());
            ps.setString(2, vetOriginal.getNombre());
            ps.setString(3, vetOriginal.getApellidos());
            ps.setDate(4, vetOriginal.getFechaContratacion());

            if (ps.executeUpdate() > 0) {

                logger.info("Veterinario insertado correctamente");
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al insertar veterinario", e);
        }

        return false;
    }

    public Veterinario obtenerVeterinarioPorLicencia(String licencia) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_LICENCE)) {

            ps.setString(1, licencia);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    logger.info("Veterinario encontrado");

                    return new Veterinario(
                            rs.getString("num_licencia"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getDate("fecha_contratacion")
                    );
                }
            }

            logger.warn("Veterinario no encontrado: {}", licencia);

        } catch (SQLException e) {
            logger.error("Error al obtener veterinario", e);
        }

        return null;
    }

    public boolean eliminarVeterinario(String licencia) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {

            ps.setString(1, licencia);

            if (ps.executeUpdate() > 0) {

                logger.info("Veterinario borrado correctamente: {}", licencia);
                return true;
            }

            logger.warn("No se encontró el veterinario con licencia: {}", licencia);

        } catch (SQLException e) {
            logger.error("Error al borrar veterinario", e);
        }

        return false;
    }

    public boolean actualizarVeterinario(Veterinario veterinario) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, veterinario.getNombre());
            ps.setString(2, veterinario.getApellidos());
            ps.setDate(3, veterinario.getFechaContratacion());
            ps.setString(4, veterinario.getNumLicencia());

            if (ps.executeUpdate() > 0) {

                logger.info("Veterinario actualizado correctamente");
                return true;
            }

            logger.warn("No se encontró el veterinario con licencia: {}",
                    veterinario.getNumLicencia());

        } catch (SQLException e) {
            logger.error("Error al actualizar veterinario", e);
        }

        return false;
    }
}

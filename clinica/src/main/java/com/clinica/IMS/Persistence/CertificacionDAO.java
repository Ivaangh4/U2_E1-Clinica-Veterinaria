
package com.clinica.IMS.Persistence;

import com.clinica.IMS.Entity.Certificacion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel
 * Capa encargada de hacer todas las operaciones CRUD
 */
public class CertificacionDAO {

    private static final Logger logger =
            LogManager.getLogger(CertificacionDAO.class);

    private static final String SQL_INSERT =
            "INSERT INTO certificaciones "
            + "(institucion_emisora, nombre_especialidad, veterinario_licencia) "
            + "VALUES (?, ?, ?)";

    private static final String SQL_GET_BY_ID =
            "SELECT id, institucion_emisora, nombre_especialidad, "
            + "veterinario_licencia FROM certificaciones WHERE id = ?";

    private static final String SQL_GET_BY_LICENCE =
            "SELECT id, institucion_emisora, nombre_especialidad, "
            + "veterinario_licencia FROM certificaciones "
            + "WHERE veterinario_licencia = ?";

    private static final String SQL_UPDATE =
            "UPDATE certificaciones SET institucion_emisora = ?, "
            + "nombre_especialidad = ? WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM certificaciones WHERE id = ?";

    public CertificacionDAO() {
    }

    public boolean insertarCertificacion(Certificacion certificacion) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, certificacion.getEntidadEmisora());
            ps.setString(2, certificacion.getNombreEsp());
            ps.setString(3, certificacion.getLicenciaVeterinario());

            if (ps.executeUpdate() > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        certificacion.setIdCertificacion(rs.getInt(1));
                    }
                }

                logger.info("Certificación insertada correctamente");
                return true;
            }

        } catch (SQLException e) {
            logger.error("Error al insertar certificación", e);
        }

        return false;
    }

    public Certificacion obtenerCertificacionPorId(int id) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    logger.info("Certificación encontrada");

                    return new Certificacion(
                            rs.getInt("id"),
                            rs.getString("institucion_emisora"),
                            rs.getString("nombre_especialidad"),
                            rs.getString("veterinario_licencia")
                    );
                }
            }

            logger.warn("Certificación no encontrada");

        } catch (SQLException e) {
            logger.error("Error al obtener certificación", e);
        }

        return null;
    }

    public List<Certificacion> obtenerCertificacionesPorLicencia(String licencia) {

        List<Certificacion> certificaciones = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_GET_BY_LICENCE)) {

            ps.setString(1, licencia);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    certificaciones.add(new Certificacion(
                            rs.getInt("id"),
                            rs.getString("institucion_emisora"),
                            rs.getString("nombre_especialidad"),
                            rs.getString("veterinario_licencia")
                    ));
                }
            }

            logger.info("Certificaciones encontradas: {}", certificaciones.size());

        } catch (SQLException e) {
            logger.error("Error al obtener las certificaciones del veterinario", e);
        }

        return certificaciones;
    }

    public boolean actualizarCertificacion(Certificacion certificacion) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, certificacion.getEntidadEmisora());
            ps.setString(2, certificacion.getNombreEsp());
            ps.setInt(3, certificacion.getIdCertificacion());

            if (ps.executeUpdate() > 0) {

                logger.info("Certificación actualizada correctamente");
                return true;
            }

            logger.warn("No se encontró la certificación con id: {}",
                    certificacion.getIdCertificacion());

        } catch (SQLException e) {
            logger.error("Error al actualizar certificación", e);
        }

        return false;
    }

    public boolean eliminarCertificacion(int id) {

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {

                logger.info("Certificación borrada correctamente");
                return true;
            }

            logger.warn("No se encontró la certificación con id: {}", id);

        } catch (SQLException e) {
            logger.error("Error al borrar certificación", e);
        }

        return false;
    }
}

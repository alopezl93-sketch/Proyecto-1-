package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntrenamientoDAO {

    public boolean registrarEntrenamiento(int atletaId, Entrenamiento entrenamiento) {
        String sql = "INSERT INTO entrenamientos (atleta_id, fecha, tipo, marca, ubicacion, pais) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, atletaId);
            stmt.setString(2, entrenamiento.getFecha());
            stmt.setString(3, entrenamiento.getTipo());
            stmt.setDouble(4, entrenamiento.getMarca());
            stmt.setString(5, entrenamiento.getUbicacion());
            stmt.setString(6, entrenamiento.getPais());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al registrar entrenamiento: " + e.getMessage());
            return false;
        }
    }

    public List<Entrenamiento> obtenerEntrenamientosPorAtleta(int atletaId) {
        List<Entrenamiento> entrenamientos = new ArrayList<>();
        String sql = "SELECT * FROM entrenamientos WHERE atleta_id = ? ORDER BY fecha";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, atletaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fecha = rs.getDate("fecha").toString();
                String tipo = rs.getString("tipo");
                double marca = rs.getDouble("marca");
                String ubicacion = rs.getString("ubicacion");
                String pais = rs.getString("pais");
                entrenamientos.add(new Entrenamiento(fecha, tipo, marca, ubicacion, pais));
            }
        } catch (SQLException e) {
            System.err.println(" Error al obtener entrenamientos: " + e.getMessage());
        }
        return entrenamientos;
    }

    public boolean eliminarEntrenamientosPorAtleta(int atletaId) {
        String sql = "DELETE FROM entrenamientos WHERE atleta_id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, atletaId);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.err.println(" Error al eliminar entrenamientos: " + e.getMessage());
            return false;
        }
    }
}
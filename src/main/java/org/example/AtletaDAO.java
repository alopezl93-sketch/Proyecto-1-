package org.example;

import java.sql.*;
import java.time.LocalDate;

public class AtletaDAO {

    int crearAtleta(Atleta atleta) {
        String sql = "INSERT INTO atletas (nombre, edad, disciplina, departamento, nacionalidad, fecha_ingreso) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, atleta.getNombre());
            stmt.setInt(2, atleta.getEdad());
            stmt.setString(3, atleta.getDisciplina());
            stmt.setString(4, atleta.getDepartamento());
            stmt.setString(5, atleta.getNacionalidad());
            stmt.setDate(6, atleta.getFechaIngreso() != null ? Date.valueOf(atleta.getFechaIngreso()) : null);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        atleta.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(" Error al crear atleta: " + e.getMessage());
        }
        return 0;
    }

    public Atleta obtenerAtletaPorId(int id) {
        String sql = "SELECT * FROM atletas WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return crearAtletaDesdeResultSet(rs);
        } catch (SQLException e) {
            System.err.println(" Error al obtener atleta: " + e.getMessage());
        }
        return null;
    }

    public Atleta buscarAtletaPorNombre(String nombre) {
        String sql = "SELECT * FROM atletas WHERE nombre = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Atleta atleta = crearAtletaDesdeResultSet(rs);
                cargarEntrenamientos(atleta);
                return atleta;
            }
        } catch (SQLException e) {
            System.err.println(" Error al buscar atleta: " + e.getMessage());
        }
        return null;
    }

    public java.util.List<Atleta> obtenerTodosLosAtletas() {
        java.util.List<Atleta> atletas = new java.util.ArrayList<>();
        String sql = "SELECT * FROM atletas ORDER BY nombre";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Atleta atleta = crearAtletaDesdeResultSet(rs);
                cargarEntrenamientos(atleta);
                atletas.add(atleta);
            }
        } catch (SQLException e) {
            System.err.println(" Error al obtener atletas: " + e.getMessage());
        }
        return atletas;
    }

    public boolean actualizarAtleta(Atleta atleta) {
        String sql = "UPDATE atletas SET nombre = ?, edad = ?, disciplina = ?, departamento = ?, nacionalidad = ?, fecha_ingreso = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, atleta.getNombre());
            stmt.setInt(2, atleta.getEdad());
            stmt.setString(3, atleta.getDisciplina());
            stmt.setString(4, atleta.getDepartamento());
            stmt.setString(5, atleta.getNacionalidad());
            stmt.setDate(6, atleta.getFechaIngreso() != null ? Date.valueOf(atleta.getFechaIngreso()) : null);
            stmt.setInt(7, atleta.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al actualizar atleta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarAtleta(int id) {
        String sql = "DELETE FROM atletas WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(" Error al eliminar atleta: " + e.getMessage());
            return false;
        }
    }

    private void cargarEntrenamientos(Atleta atleta) {
        String sql = "SELECT * FROM entrenamientos WHERE atleta_id = ? ORDER BY fecha";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, atleta.getId());
            ResultSet rs = stmt.executeQuery();
            java.util.List<Entrenamiento> entrenamientos = new java.util.ArrayList<>();
            while (rs.next()) {
                String fecha = rs.getDate("fecha").toString();
                String tipo = rs.getString("tipo");
                double marca = rs.getDouble("marca");
                String ubicacion = rs.getString("ubicacion");
                String pais = rs.getString("pais");
                entrenamientos.add(new Entrenamiento(fecha, tipo, marca, ubicacion, pais));
            }
            atleta.setEntrenamientos(entrenamientos);
        } catch (SQLException e) {
            System.err.println(" Error al cargar entrenamientos: " + e.getMessage());
        }
    }

    private Atleta crearAtletaDesdeResultSet(ResultSet rs) throws SQLException {
        Date fi = rs.getDate("fecha_ingreso");
        LocalDate fechaIngreso = fi != null ? fi.toLocalDate() : null;
        return new Atleta(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getInt("edad"),
                rs.getString("disciplina"),
                rs.getString("departamento"),
                rs.getString("nacionalidad"),
                fechaIngreso
        );
    }
}
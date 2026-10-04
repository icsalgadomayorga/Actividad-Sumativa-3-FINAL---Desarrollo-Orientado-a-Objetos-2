package dao;

import model.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    public boolean create(Repartidor repartidor) {

        String sql = "INSERT INTO repartidor (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear repartidor: " + e.getMessage());
            return false;
        }
    }

    public List<Repartidor> readAll() {

        List<Repartidor> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidor ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Repartidor repartidor = new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );

                lista.add(repartidor);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar repartidores: "
                    + e.getMessage());
        }

        return lista;
    }

    public boolean update(Repartidor repartidor) {

        String sql =
                "UPDATE repartidor SET nombre = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombre());
            ps.setInt(2, repartidor.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar repartidor: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM repartidor WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar repartidor: "
                    + e.getMessage());
            return false;
        }
    }
}

package dao;

import model.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    public boolean create(Entrega entrega) {

        String sql = """
                INSERT INTO entrega
                (id_pedido, id_repartidor, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear entrega: "
                    + e.getMessage());
            return false;
        }
    }

    public List<Entrega> readAll() {

        List<Entrega> lista = new ArrayList<>();

        String sql = """
                SELECT id, id_pedido, id_repartidor, fecha, hora
                FROM entrega
                ORDER BY id
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Entrega entrega = new Entrega(
                        rs.getInt("id"),
                        rs.getInt("id_pedido"),
                        rs.getInt("id_repartidor"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime()
                );

                lista.add(entrega);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entregas: "
                    + e.getMessage());
        }

        return lista;
    }

    public boolean update(Entrega entrega) {

        String sql = """
                UPDATE entrega
                SET id_pedido = ?,
                    id_repartidor = ?,
                    fecha = ?,
                    hora = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));
            ps.setInt(5, entrega.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrega: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {

        String sql = "DELETE FROM entrega WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrega: "
                    + e.getMessage());
            return false;
        }
    }
}

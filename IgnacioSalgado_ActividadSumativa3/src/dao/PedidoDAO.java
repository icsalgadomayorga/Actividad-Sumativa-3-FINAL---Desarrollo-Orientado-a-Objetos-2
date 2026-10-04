package dao;

import model.EstadoPedido;
import model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean create(Pedido pedido) {

        String sql = """
                INSERT INTO pedido (direccion, tipo, estado)
                VALUES (?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());

            // El enum se guarda como texto en MySQL
            ps.setString(3, pedido.getEstado().name());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error al crear pedido: "
                    + e.getMessage());

            return false;
        }
    }


    public List<Pedido> readAll() {

        List<Pedido> lista = new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado " +
                        "FROM pedido ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(convertirPedido(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error al listar pedidos: "
                    + e.getMessage());
        }

        return lista;
    }


    public boolean update(Pedido pedido) {

        String sql = """
                UPDATE pedido
                SET direccion = ?, tipo = ?, estado = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());

            // Enum -> String para MySQL
            ps.setString(3, pedido.getEstado().name());

            ps.setInt(4, pedido.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error al actualizar pedido: "
                    + e.getMessage());

            return false;
        }
    }


    public boolean delete(int id) {

        String sql =
                "DELETE FROM pedido WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println("Error al eliminar pedido: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // FILTRO POR ESTADO
    // =========================
    public List<Pedido> buscarPorEstado(String estado) {

        List<Pedido> lista = new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado " +
                        "FROM pedido WHERE estado = ? ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(convertirPedido(rs));
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al filtrar pedidos por estado: "
                    + e.getMessage());
        }

        return lista;
    }

    // =========================
    // FILTRO POR TIPO
    // =========================
    public List<Pedido> buscarPorTipo(String tipo) {

        List<Pedido> lista = new ArrayList<>();

        String sql =
                "SELECT id, direccion, tipo, estado " +
                        "FROM pedido WHERE tipo = ? ORDER BY id";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, tipo);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(convertirPedido(rs));
                }
            }

        } catch (SQLException e) {

            System.out.println("Error al filtrar pedidos por tipo: "
                    + e.getMessage());
        }

        return lista;
    }

    // ==================================================
    // MÉTODOS COMPATIBLES CON ControladorPedidos
    // ==================================================

    /**
     * Guarda un pedido.
     * Compatible con ControladorPedidos.
     */
    public boolean guardar(Pedido pedido) {

        return create(pedido);
    }

    /**
     * Obtiene todos los pedidos.
     * Compatible con ControladorPedidos.
     */
    public List<Pedido> listarTodos() {

        return readAll();
    }

    /**
     * Comprueba si existe un pedido por ID.
     * Compatible con ControladorPedidos.
     */
    public boolean existe(int id) {

        String sql =
                "SELECT id FROM pedido WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {

            System.out.println("Error al comprobar pedido: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // CONVERTIR RESULTSET
    // =========================

    /**
     * Convierte un registro de MySQL
     * en un objeto Pedido.
     */
    private Pedido convertirPedido(ResultSet rs)
            throws SQLException {

        int id = rs.getInt("id");

        String direccion =
                rs.getString("direccion");

        String tipo =
                rs.getString("tipo");

        String estadoTexto =
                rs.getString("estado");

        EstadoPedido estado =
                EstadoPedido.valueOf(estadoTexto);

        return new Pedido(
                id,
                direccion,
                tipo,
                estado
        );
    }
}
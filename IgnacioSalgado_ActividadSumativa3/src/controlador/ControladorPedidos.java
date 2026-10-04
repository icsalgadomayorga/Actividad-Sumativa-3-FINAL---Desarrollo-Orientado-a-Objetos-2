package controlador;

import dao.PedidoDAO;
import model.Pedido;
import model.ZonaDeCarga;

import java.util.List;

/**
 * Controlador de pedidos.
 */
public class ControladorPedidos {

    private PedidoDAO pedidoDAO;
    private ZonaDeCarga zonaDeCarga;

    public ControladorPedidos() {

        pedidoDAO = new PedidoDAO();

        zonaDeCarga = new ZonaDeCarga();
    }

    /**
     * Guarda el pedido en la base de datos
     * y lo agrega a la zona de carga.
     */
    public synchronized boolean agregarPedido(
            Pedido pedido) {

        if (pedido == null) {
            return false;
        }

        boolean guardado =
                pedidoDAO.guardar(pedido);

        if (guardado) {

            zonaDeCarga.agregarPedido(pedido);
        }

        return guardado;
    }

    /**
     * Obtiene los pedidos desde MySQL.
     */
    public List<Pedido> obtenerPedidos() {

        return pedidoDAO.listarTodos();
    }

    /**
     * Busca un pedido.
     */
    public Pedido buscarPorId(int id) {

        for (Pedido pedido :
                pedidoDAO.listarTodos()) {

            if (pedido.getId() == id) {

                return pedido;
            }
        }

        return null;
    }

    /**
     * Comprueba si existe un pedido.
     */
    public boolean existePedido(int id) {

        return pedidoDAO.existe(id);
    }

    /**
     * Retorna la cantidad de pedidos.
     */
    public int cantidadPedidos() {

        return pedidoDAO.listarTodos().size();
    }

    /**
     * Retorna la zona de carga.
     */
    public ZonaDeCarga getZonaDeCarga() {

        return zonaDeCarga;
    }
}

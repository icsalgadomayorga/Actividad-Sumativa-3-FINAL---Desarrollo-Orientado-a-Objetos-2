package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Representa la zona de carga compartida
 * por los repartidores.
 */
public class ZonaDeCarga {

    private List<Pedido> pedidos;

    /**
     * Constructor de ZonaDeCarga.
     */
    public ZonaDeCarga() {

        pedidos = new ArrayList<>();
    }

    /**
     * Agrega un pedido a la zona de carga.
     */
    public synchronized void agregarPedido(Pedido pedido) {

        if (pedido != null) {

            pedido.setEstado(EstadoPedido.PENDIENTE);

            pedidos.add(pedido);

            System.out.println(
                    "[Zona de carga] Pedido #" +
                            pedido.getId() +
                            " agregado."
            );
        }
    }

    /**
     * Retira un pedido pendiente de la zona de carga.
     */
    public synchronized Pedido retirarPedido() {

        Iterator<Pedido> iterator = pedidos.iterator();

        while (iterator.hasNext()) {

            Pedido pedido = iterator.next();

            if (pedido.getEstado() == EstadoPedido.PENDIENTE) {

                pedido.setEstado(EstadoPedido.EN_REPARTO);

                iterator.remove();

                System.out.println(
                        "[Zona de carga] Pedido #" +
                                pedido.getId() +
                                " retirado."
                );

                return pedido;
            }
        }

        return null;
    }

    /**
     * Retorna la cantidad de pedidos pendientes.
     */
    public synchronized int cantidadPedidos() {

        return pedidos.size();
    }
}

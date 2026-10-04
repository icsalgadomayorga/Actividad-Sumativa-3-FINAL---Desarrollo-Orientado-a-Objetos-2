package vista;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaListaPedidos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> comboFiltroTipo;
    private JComboBox<String> comboFiltroEstado;

    private final PedidoDAO dao = new PedidoDAO();

    public VentanaListaPedidos() {

        setTitle("SpeedFast - Lista de Pedidos");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarTodos();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel filtros = new JPanel();

        filtros.add(new JLabel("Tipo:"));

        comboFiltroTipo = new JComboBox<>(
                new String[]{
                        "TODOS",
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                }
        );

        filtros.add(comboFiltroTipo);

        filtros.add(new JLabel("Estado:"));

        comboFiltroEstado = new JComboBox<>(
                new String[]{
                        "TODOS",
                        "PENDIENTE",
                        "EN_REPARTO",
                        "ENTREGADO"
                }
        );

        filtros.add(comboFiltroEstado);

        JButton btnFiltrar =
                new JButton("Filtrar");

        filtros.add(btnFiltrar);

        JButton btnActualizar =
                new JButton("Actualizar");

        filtros.add(btnActualizar);

        JButton btnRegistrar =
                new JButton("Registrar Pedido");

        filtros.add(btnRegistrar);

        JButton btnEditar =
                new JButton("Editar");

        filtros.add(btnEditar);

        JButton btnEliminar =
                new JButton("Eliminar");

        filtros.add(btnEliminar);

        add(
                filtros,
                BorderLayout.NORTH
        );

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Dirección",
                        "Tipo",
                        "Estado"
                },
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        btnFiltrar.addActionListener(
                e -> filtrar()
        );

        btnActualizar.addActionListener(
                e -> cargarTodos()
        );

        btnRegistrar.addActionListener(
                e -> abrirRegistro()
        );

        btnEditar.addActionListener(
                e -> editar()
        );

        btnEliminar.addActionListener(
                e -> eliminar()
        );
    }

    private void abrirRegistro() {

        VentanaRegistroPedido ventana =
                new VentanaRegistroPedido();

        ventana.setVisible(true);
    }

    private void editar() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido para editar."
            );

            return;
        }

        int id =
                Integer.parseInt(
                        tabla.getValueAt(
                                fila,
                                0
                        ).toString()
                );

        Pedido pedido = buscarPedidoPorId(id);

        if (pedido == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo encontrar el pedido."
            );

            return;
        }

        VentanaRegistroPedido ventana =
                new VentanaRegistroPedido();

        ventana.cargarPedido(pedido);

        ventana.setVisible(true);
    }

    private Pedido buscarPedidoPorId(int id) {

        List<Pedido> pedidos =
                dao.readAll();

        for (Pedido pedido : pedidos) {

            if (pedido.getId() == id) {
                return pedido;
            }
        }

        return null;
    }

    private void cargarTodos() {

        mostrarPedidos(
                dao.readAll()
        );
    }

    private void filtrar() {

        String tipo =
                comboFiltroTipo
                        .getSelectedItem()
                        .toString();

        String estadoTexto =
                comboFiltroEstado
                        .getSelectedItem()
                        .toString();

        List<Pedido> pedidos;

        if (tipo.equals("TODOS")
                && estadoTexto.equals("TODOS")) {

            pedidos = dao.readAll();

        } else if (!tipo.equals("TODOS")
                && estadoTexto.equals("TODOS")) {

            pedidos = dao.buscarPorTipo(tipo);

        } else if (tipo.equals("TODOS")
                && !estadoTexto.equals("TODOS")) {

            pedidos =
                    dao.buscarPorEstado(
                            estadoTexto
                    );

        } else {

            pedidos = dao.buscarPorTipo(tipo);

            EstadoPedido estado =
                    EstadoPedido.valueOf(
                            estadoTexto
                    );

            pedidos.removeIf(
                    pedido ->
                            pedido.getEstado()
                                    != estado
            );
        }

        mostrarPedidos(pedidos);
    }

    private void mostrarPedidos(
            List<Pedido> pedidos) {

        modeloTabla.setRowCount(0);

        for (Pedido pedido : pedidos) {

            modeloTabla.addRow(
                    new Object[]{
                            pedido.getId(),
                            pedido.getDireccion(),
                            pedido.getTipo(),
                            pedido.getEstado()
                    }
            );
        }
    }

    private void eliminar() {

        int fila =
                tabla.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un pedido."
            );

            return;
        }

        int id =
                Integer.parseInt(
                        tabla.getValueAt(
                                fila,
                                0
                        ).toString()
                );

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el pedido?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.delete(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido eliminado correctamente."
            );

            cargarTodos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se puede eliminar el pedido. " +
                            "Puede tener una entrega asociada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
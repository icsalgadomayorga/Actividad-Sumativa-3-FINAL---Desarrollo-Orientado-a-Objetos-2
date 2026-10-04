package vista;

import dao.EntregaDAO;
import dao.PedidoDAO;
import dao.RepartidorDAO;
import model.Entrega;
import model.Pedido;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VentanaEntregas extends JFrame {

    private JComboBox<Pedido> comboPedido;
    private JComboBox<Repartidor> comboRepartidor;

    private JTextField txtFecha;
    private JTextField txtHora;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private final EntregaDAO entregaDAO =
            new EntregaDAO();

    private final PedidoDAO pedidoDAO =
            new PedidoDAO();

    private final RepartidorDAO repartidorDAO =
            new RepartidorDAO();

    private int idSeleccionado = -1;

    public VentanaEntregas() {

        setTitle("SpeedFast - Gestión de Entregas");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();

        cargarCombos();
        cargarTabla();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout(10, 10));

        /*
         * PANEL SUPERIOR
         */
        JPanel panelSuperior = new JPanel(
                new BorderLayout(10, 10)
        );

        panelSuperior.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 20, 10, 20
                )
        );

        /*
         * PANEL DE DATOS
         */
        JPanel formulario = new JPanel(
                new GridLayout(2, 4, 10, 10)
        );

        formulario.add(
                new JLabel("Pedido:")
        );

        comboPedido = new JComboBox<>();

        formulario.add(comboPedido);

        formulario.add(
                new JLabel("Repartidor:")
        );

        comboRepartidor = new JComboBox<>();

        formulario.add(comboRepartidor);

        formulario.add(
                new JLabel("Fecha (AAAA-MM-DD):")
        );

        txtFecha = new JTextField(
                LocalDate.now().toString()
        );

        formulario.add(txtFecha);

        formulario.add(
                new JLabel("Hora (HH:MM):")
        );

        txtHora = new JTextField(
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0)
                        .toString()
        );

        formulario.add(txtHora);

        panelSuperior.add(
                formulario,
                BorderLayout.CENTER
        );

        /*
         * PANEL DE BOTONES
         */
        JPanel panelBotones = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER,
                        10,
                        5
                )
        );

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnEditar =
                new JButton("Editar");

        JButton btnEliminar =
                new JButton("Eliminar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        panelSuperior.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        add(
                panelSuperior,
                BorderLayout.NORTH
        );

        /*
         * TABLA
         */
        modeloTabla =
                new DefaultTableModel(
                        new Object[]{
                                "ID",
                                "Pedido",
                                "Repartidor",
                                "Fecha",
                                "Hora"
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

        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        add(
                new JScrollPane(tabla),
                BorderLayout.CENTER
        );

        /*
         * EVENTOS
         */
        btnRegistrar.addActionListener(
                e -> registrar()
        );

        btnEditar.addActionListener(
                e -> editar()
        );

        btnEliminar.addActionListener(
                e -> eliminar()
        );

        tabla.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int fila =
                                tabla.getSelectedRow();

                        if (fila >= 0) {

                            cargarEntregaSeleccionada(
                                    fila
                            );
                        }
                    }
                });
    }

    private void cargarCombos() {

        comboPedido.removeAllItems();

        List<Pedido> pedidos =
                pedidoDAO.readAll();

        for (Pedido pedido : pedidos) {

            comboPedido.addItem(pedido);
        }

        comboRepartidor.removeAllItems();

        List<Repartidor> repartidores =
                repartidorDAO.readAll();

        for (Repartidor repartidor : repartidores) {

            comboRepartidor.addItem(
                    repartidor
            );
        }
    }

    private void registrar() {

        Pedido pedido =
                (Pedido) comboPedido
                        .getSelectedItem();

        Repartidor repartidor =
                (Repartidor) comboRepartidor
                        .getSelectedItem();

        if (pedido == null || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido " +
                            "y un repartidor."
            );

            return;
        }

        LocalDate fecha;
        LocalTime hora;

        try {

            fecha =
                    LocalDate.parse(
                            txtFecha.getText().trim()
                    );

            hora =
                    LocalTime.parse(
                            txtHora.getText().trim()
                    );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n\n" +
                            "Fecha: AAAA-MM-DD\n" +
                            "Hora: HH:MM",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Entrega entrega =
                new Entrega(
                        pedido.getId(),
                        repartidor.getId(),
                        fecha,
                        hora
                );

        if (entregaDAO.create(entrega)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega registrada correctamente."
            );

            limpiarFormulario();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editar() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega de la tabla."
            );

            return;
        }

        Pedido pedido =
                (Pedido) comboPedido
                        .getSelectedItem();

        Repartidor repartidor =
                (Repartidor) comboRepartidor
                        .getSelectedItem();

        if (pedido == null || repartidor == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un pedido " +
                            "y un repartidor."
            );

            return;
        }

        try {

            LocalDate fecha =
                    LocalDate.parse(
                            txtFecha.getText().trim()
                    );

            LocalTime hora =
                    LocalTime.parse(
                            txtHora.getText().trim()
                    );

            Entrega entrega =
                    new Entrega(
                            idSeleccionado,
                            pedido.getId(),
                            repartidor.getId(),
                            fecha,
                            hora
                    );

            if (entregaDAO.update(entrega)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Entrega actualizada correctamente."
                );

                limpiarFormulario();
                cargarTabla();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No fue posible actualizar la entrega.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Fecha u hora inválida.\n\n" +
                            "Fecha: AAAA-MM-DD\n" +
                            "Hora: HH:MM",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminar() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una entrega."
            );

            return;
        }

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar esta entrega?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (entregaDAO.delete(idSeleccionado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entrega eliminada correctamente."
            );

            limpiarFormulario();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar la entrega.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Entrega> entregas =
                entregaDAO.readAll();

        for (Entrega entrega : entregas) {

            modeloTabla.addRow(
                    new Object[]{
                            entrega.getId(),
                            entrega.getIdPedido(),
                            entrega.getIdRepartidor(),
                            entrega.getFecha(),
                            entrega.getHora()
                    }
            );
        }
    }

    private void cargarEntregaSeleccionada(
            int fila) {

        idSeleccionado =
                Integer.parseInt(
                        tabla.getValueAt(
                                fila,
                                0
                        ).toString()
                );

        int idPedido =
                Integer.parseInt(
                        tabla.getValueAt(
                                fila,
                                1
                        ).toString()
                );

        int idRepartidor =
                Integer.parseInt(
                        tabla.getValueAt(
                                fila,
                                2
                        ).toString()
                );

        seleccionarPedido(idPedido);

        seleccionarRepartidor(
                idRepartidor
        );

        txtFecha.setText(
                tabla.getValueAt(
                        fila,
                        3
                ).toString()
        );

        txtHora.setText(
                tabla.getValueAt(
                        fila,
                        4
                ).toString()
        );
    }

    private void seleccionarPedido(
            int id) {

        for (int i = 0;
             i < comboPedido.getItemCount();
             i++) {

            Pedido pedido =
                    comboPedido.getItemAt(i);

            if (pedido.getId() == id) {

                comboPedido.setSelectedIndex(i);

                break;
            }
        }
    }

    private void seleccionarRepartidor(
            int id) {

        for (int i = 0;
             i < comboRepartidor.getItemCount();
             i++) {

            Repartidor repartidor =
                    comboRepartidor.getItemAt(i);

            if (repartidor.getId() == id) {

                comboRepartidor.setSelectedIndex(i);

                break;
            }
        }
    }

    private void limpiarFormulario() {

        idSeleccionado = -1;

        if (comboPedido.getItemCount() > 0) {

            comboPedido.setSelectedIndex(0);
        }

        if (comboRepartidor.getItemCount() > 0) {

            comboRepartidor.setSelectedIndex(0);
        }

        txtFecha.setText(
                LocalDate.now().toString()
        );

        txtHora.setText(
                LocalTime.now()
                        .withSecond(0)
                        .withNano(0)
                        .toString()
        );

        tabla.clearSelection();
    }
}
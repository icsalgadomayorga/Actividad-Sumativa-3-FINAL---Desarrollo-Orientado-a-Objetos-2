package vista;

import dao.PedidoDAO;
import model.EstadoPedido;
import model.Pedido;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroPedido extends JFrame {

    private JTextField txtDireccion;
    private JComboBox<String> comboTipo;
    private JComboBox<EstadoPedido> comboEstado;

    private final PedidoDAO dao = new PedidoDAO();

    private int idSeleccionado = -1;

    private JButton btnGuardar;

    public VentanaRegistroPedido() {

        setTitle("SpeedFast - Registrar Pedido");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel panel = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        panel.add(new JLabel("Dirección:"));

        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        panel.add(new JLabel("Tipo:"));

        comboTipo = new JComboBox<>(
                new String[]{
                        "COMIDA",
                        "ENCOMIENDA",
                        "EXPRESS"
                }
        );

        panel.add(comboTipo);

        panel.add(new JLabel("Estado:"));

        comboEstado = new JComboBox<>(
                EstadoPedido.values()
        );

        panel.add(comboEstado);

        btnGuardar = new JButton("Registrar");

        JButton btnCancelar = new JButton("Cancelar");

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        add(panel, BorderLayout.CENTER);

        btnGuardar.addActionListener(
                e -> guardar()
        );

        btnCancelar.addActionListener(
                e -> dispose()
        );
    }

    private void guardar() {

        String direccion =
                txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección es obligatoria."
            );

            return;
        }

        String tipo =
                comboTipo.getSelectedItem().toString();

        EstadoPedido estado =
                (EstadoPedido)
                        comboEstado.getSelectedItem();

        Pedido pedido =
                new Pedido(
                        direccion,
                        tipo,
                        estado
                );

        if (dao.create(pedido)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido registrado correctamente."
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Carga un pedido para editarlo.
     */
    public void cargarPedido(Pedido pedido) {

        if (pedido == null) {
            return;
        }

        idSeleccionado =
                pedido.getId();

        txtDireccion.setText(
                pedido.getDireccion()
        );

        comboTipo.setSelectedItem(
                pedido.getTipo()
        );

        comboEstado.setSelectedItem(
                pedido.getEstado()
        );

        setTitle("SpeedFast - Editar Pedido");

        btnGuardar.setText("Guardar cambios");

        btnGuardar.removeActionListener(
                btnGuardar.getActionListeners()[0]
        );

        btnGuardar.addActionListener(
                e -> actualizar()
        );
    }

    /**
     * Actualiza un pedido existente.
     */
    private void actualizar() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay ningún pedido seleccionado."
            );

            return;
        }

        String direccion =
                txtDireccion.getText().trim();

        if (direccion.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "La dirección es obligatoria."
            );

            return;
        }

        String tipo =
                comboTipo.getSelectedItem().toString();

        EstadoPedido estado =
                (EstadoPedido)
                        comboEstado.getSelectedItem();

        Pedido pedido =
                new Pedido(
                        idSeleccionado,
                        direccion,
                        tipo,
                        estado
                );

        if (dao.update(pedido)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Pedido actualizado correctamente."
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al actualizar el pedido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
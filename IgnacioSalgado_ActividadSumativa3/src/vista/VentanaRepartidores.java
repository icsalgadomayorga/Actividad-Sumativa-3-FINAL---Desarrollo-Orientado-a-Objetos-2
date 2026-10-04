package vista;

import dao.RepartidorDAO;
import model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaRepartidores extends JFrame {

    private JTextField txtNombre;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private final RepartidorDAO dao = new RepartidorDAO();

    private int idSeleccionado = -1;

    public VentanaRepartidores() {

        setTitle("SpeedFast - Gestión de Repartidores");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarTabla();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());

        panelSuperior.add(new JLabel("Nombre:"));

        txtNombre = new JTextField(20);
        panelSuperior.add(txtNombre);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelSuperior.add(btnRegistrar);
        panelSuperior.add(btnEditar);
        panelSuperior.add(btnEliminar);
        panelSuperior.add(btnLimpiar);

        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int fila = tabla.getSelectedRow();

                if (fila >= 0) {

                    idSeleccionado =
                            Integer.parseInt(
                                    tabla.getValueAt(fila, 0).toString());

                    txtNombre.setText(
                            tabla.getValueAt(fila, 1).toString());
                }
            }
        });
    }

    private void registrar() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar el nombre del repartidor."
            );
            return;
        }

        Repartidor repartidor = new Repartidor(nombre);

        if (dao.create(repartidor)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor registrado correctamente."
            );

            limpiar();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void editar() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor de la tabla."
            );

            return;
        }

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El nombre es obligatorio."
            );

            return;
        }

        Repartidor repartidor =
                new Repartidor(idSeleccionado, nombre);

        if (dao.update(repartidor)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor actualizado correctamente."
            );

            limpiar();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el repartidor.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminar() {

        if (idSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un repartidor."
            );

            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar este repartidor?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.delete(idSeleccionado)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Repartidor eliminado correctamente."
            );

            limpiar();
            cargarTabla();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se puede eliminar el repartidor. " +
                            "Puede tener entregas asociadas.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        for (Repartidor r : dao.readAll()) {

            modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getNombre()
            });
        }
    }

    private void limpiar() {

        txtNombre.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}

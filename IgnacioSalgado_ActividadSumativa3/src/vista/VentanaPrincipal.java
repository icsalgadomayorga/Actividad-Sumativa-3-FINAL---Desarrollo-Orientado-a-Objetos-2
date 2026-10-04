package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {

        setTitle("SpeedFast - Sistema de Gestión");
        setSize(600, 400);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(
                new BorderLayout()
        );

        JLabel titulo =
                new JLabel(
                        "SPEEDFAST",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        add(
                titulo,
                BorderLayout.NORTH
        );

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                3,
                                1,
                                15,
                                15
                        )
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        80,
                        30,
                        80
                )
        );

        JButton btnPedidos =
                new JButton(
                        "Gestión de Pedidos"
                );

        JButton btnRepartidores =
                new JButton(
                        "Gestión de Repartidores"
                );

        JButton btnEntregas =
                new JButton(
                        "Gestión de Entregas"
                );

        panel.add(btnPedidos);
        panel.add(btnRepartidores);
        panel.add(btnEntregas);

        add(
                panel,
                BorderLayout.CENTER
        );

        btnPedidos.addActionListener(e -> {

            VentanaListaPedidos ventana =
                    new VentanaListaPedidos();

            ventana.setVisible(true);
        });

        btnRepartidores.addActionListener(e -> {

            VentanaRepartidores ventana =
                    new VentanaRepartidores();

            ventana.setVisible(true);
        });

        btnEntregas.addActionListener(e -> {

            VentanaEntregas ventana =
                    new VentanaEntregas();

            ventana.setVisible(true);
        });
    }
}
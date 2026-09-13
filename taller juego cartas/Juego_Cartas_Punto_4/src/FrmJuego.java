import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class FrmJuego extends JFrame {

    private JPanel panelJugadorUno, panelJugadorDos;
    private JTabbedPane pestanasJugadores;
    private JTextField campoCantidadBarajas;

    private Jugador jugadorUno = new Jugador();
    private Jugador jugadorDos = new Jugador();

    public FrmJuego() {
        setSize(560, 330);
        setTitle("Juego de Cartas - Punto 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        JButton botonRepartir = new JButton("Repartir");
        botonRepartir.setBounds(10, 10, 100, 25);
        add(botonRepartir);

        JButton botonVerificar = new JButton("Verificar");
        botonVerificar.setBounds(120, 10, 100, 25);
        add(botonVerificar);

        JLabel etiquetaCantidadBarajas = new JLabel("Barajas:");
        etiquetaCantidadBarajas.setBounds(250, 10, 60, 25);
        add(etiquetaCantidadBarajas);

        campoCantidadBarajas = new JTextField("1");
        campoCantidadBarajas.setBounds(310, 10, 50, 25);
        add(campoCantidadBarajas);

        pestanasJugadores = new JTabbedPane();
        pestanasJugadores.setBounds(10, 45, 530, 235);
        add(pestanasJugadores);

        panelJugadorUno = new JPanel();
        panelJugadorUno.setBackground(new Color(50, 205, 50));
        pestanasJugadores.add("Jugador 1", panelJugadorUno);

        panelJugadorDos = new JPanel();
        panelJugadorDos.setBackground(new Color(100, 220, 130));
        pestanasJugadores.add("Jugador 2", panelJugadorDos);

        // Expresiones lambda: forma sencilla de usar programacion funcional en los eventos
        botonRepartir.addActionListener(evento -> {
            repartir();
        });

        botonVerificar.addActionListener(evento -> {
            verificar();
        });
    }

    private void repartir() {
        int cantidadBarajas;

        try {
            cantidadBarajas = Integer.parseInt(campoCantidadBarajas.getText());

            if (cantidadBarajas <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad de barajas debe ser mayor que 0");
                return;
            }

        } catch (Exception error) {
            JOptionPane.showMessageDialog(null, "Digite una cantidad numerica de barajas");
            campoCantidadBarajas.setText("1");
            campoCantidadBarajas.requestFocus();
            return;
        }

        // Cada posicion representa una de las 52 cartas.
        // El valor guarda cuantas veces se ha utilizado esa carta en este reparto.
        int[] cantidadUsadaPorCarta = new int[52];

        jugadorUno.repartir(cantidadUsadaPorCarta, cantidadBarajas);
        jugadorDos.repartir(cantidadUsadaPorCarta, cantidadBarajas);

        jugadorUno.mostrar(panelJugadorUno);
        jugadorDos.mostrar(panelJugadorDos);
    }

    private void verificar() {
        String mensaje = "";
        int jugadorSeleccionado = pestanasJugadores.getSelectedIndex();

        if (jugadorSeleccionado == 0) {
            mensaje = jugadorUno.obtenerGrupos();
        } else if (jugadorSeleccionado == 1) {
            mensaje = jugadorDos.obtenerGrupos();
        }

        if (mensaje.length() > 0) {
            JOptionPane.showMessageDialog(null, mensaje);
        }
    }
}

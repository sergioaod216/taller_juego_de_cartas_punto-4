import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private int cantidadCartasPorJugador = 10;
    private int margenCartas = 10;
    private int distanciaEntreCartas = 40;

    private Carta[] cartas = new Carta[cantidadCartasPorJugador];
    private Random generadorAleatorio = new Random();

    private String[] nombresCartas = {
        "As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"
    };

    private String[] nombresPintas = {
        "Trebol", "Pica", "Corazon", "Diamante"
    };

    private String[] nombresGrupos = {
        "", "", "Par", "Terna", "Cuarta", "Quinta",
        "Sexta", "Septima", "Octava", "Novena", "Decima"
    };

    public void repartir(int[] cantidadUsadaPorCarta, int cantidadBarajas) {
        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            cartas[indiceCarta] = new Carta(generadorAleatorio, cantidadUsadaPorCarta, cantidadBarajas);
        }
    }

    public void mostrar(JPanel panelJugador) {
        panelJugador.removeAll(); // Limpiar las cartas anteriores.
        panelJugador.setLayout(null); // Permitir posiciones manuales

        for (int indiceCarta = cantidadCartasPorJugador - 1; indiceCarta >= 0; indiceCarta--) {
            int posicionHorizontal = margenCartas + indiceCarta * distanciaEntreCartas;
            cartas[indiceCarta].mostrar(panelJugador, posicionHorizontal, margenCartas);
        }

        panelJugador.revalidate(); // Actualizar el panel
        panelJugador.repaint(); // Redibujar las cartas
    }

    public String obtenerGrupos() {
        if (cartas[0] == null) {
            return "Primero debe repartir las cartas";
        }

        boolean[] cartaUsada = new boolean[cantidadCartasPorJugador];
        String resultado = "";

        String gruposMismoNombre = buscarGruposMismoNombre(cartaUsada);
        String escaleras = buscarEscaleras(cartaUsada);

        if (gruposMismoNombre.length() > 0) {
            resultado += gruposMismoNombre;
        }

        if (escaleras.length() > 0) {
            if (resultado.length() > 0) {
                resultado += "\n";
            }
            resultado += escaleras;
        }

        if (resultado.length() == 0) {
            resultado = "No se encontraron grupos\n";
        }

        resultado += "\n" + calcularSobrantes(cartaUsada);
        return resultado;
    }

    private String buscarGruposMismoNombre(boolean[] cartaUsada) {
        int[] cantidadPorNombre = new int[13];
        String resultado = "";

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            int posicionNombre = cartas[indiceCarta].obtenerNombre().ordinal();
            cantidadPorNombre[posicionNombre]++;
        }

        for (int posicionNombre = 0; posicionNombre < cantidadPorNombre.length; posicionNombre++) {
            if (cantidadPorNombre[posicionNombre] >= 2) {
                int cantidadGrupo = cantidadPorNombre[posicionNombre];
                resultado += nombresGrupos[cantidadGrupo] + " de "
                        + nombresCartas[posicionNombre] + "\n";

                for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
                    int nombreCarta = cartas[indiceCarta].obtenerNombre().ordinal();
                    if (nombreCarta == posicionNombre) {
                        cartaUsada[indiceCarta] = true;
                    }
                }
            }
        }

        return resultado;
    }

    private String buscarEscaleras(boolean[] cartaUsada) {
        int[][] cantidadPorPintaYNombre = new int[4][13];
        String resultado = "";

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            int posicionPinta = cartas[indiceCarta].obtenerPinta().ordinal();
            int posicionNombre = cartas[indiceCarta].obtenerNombre().ordinal();
            cantidadPorPintaYNombre[posicionPinta][posicionNombre]++;
        }

        for (int posicionPinta = 0; posicionPinta < 4; posicionPinta++) {
            int posicionNombre = 0;

            while (posicionNombre < 13) {
                if (cantidadPorPintaYNombre[posicionPinta][posicionNombre] > 0) {
                    int inicioEscalera = posicionNombre;
                    int finEscalera = posicionNombre;

                    while (finEscalera + 1 < 13
                            && cantidadPorPintaYNombre[posicionPinta][finEscalera + 1] > 0) {
                        finEscalera++;
                    }

                    int cantidadEscalera = finEscalera - inicioEscalera + 1;

                    if (cantidadEscalera >= 2) {
                        resultado += nombresGrupos[cantidadEscalera] + " de "
                                + nombresPintas[posicionPinta] + " de "
                                + nombresCartas[inicioEscalera] + " a "
                                + nombresCartas[finEscalera] + "\n";

                        marcarCartasEscalera(cartaUsada, posicionPinta, inicioEscalera, finEscalera);
                    }

                    posicionNombre = finEscalera + 1;
                } else {
                    posicionNombre++;
                }
            }
        }

        return resultado;
    }

    private void marcarCartasEscalera(boolean[] cartaUsada, int posicionPinta,
            int inicioEscalera, int finEscalera) {

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            int pintaCarta = cartas[indiceCarta].obtenerPinta().ordinal();
            int nombreCarta = cartas[indiceCarta].obtenerNombre().ordinal();

            if (pintaCarta == posicionPinta
                    && nombreCarta >= inicioEscalera
                    && nombreCarta <= finEscalera) {
                cartaUsada[indiceCarta] = true;
            }
        }
    }

    private String calcularSobrantes(boolean[] cartaUsada) {
        String resultado = "Sobran:\n";
        int puntos = 0;
        boolean hayCartasSobrantes = false;

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            if (cartaUsada[indiceCarta] == false) {
                int posicionNombre = cartas[indiceCarta].obtenerNombre().ordinal();
                int posicionPinta = cartas[indiceCarta].obtenerPinta().ordinal();

                resultado += nombresCartas[posicionNombre] + " de "
                        + nombresPintas[posicionPinta] + "\n";

                puntos += cartas[indiceCarta].obtenerValor();
                hayCartasSobrantes = true;
            }
        }

        if (hayCartasSobrantes == false) {
            resultado += "Ninguna\n";
        }

        resultado += "\nPuntos:\n" + puntos;
        return resultado;
    }
}

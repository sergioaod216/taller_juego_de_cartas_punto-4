import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private int cantidadCartasPorJugador = 10;
    private int margenCartas = 10;
    private int distanciaEntreCartas = 40;

    private Carta[] cartas = new Carta[cantidadCartasPorJugador];
    private Random generadorAleatorio = new Random();

    private String[] nombresCartas = {"As", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
    private String[] nombresPintas = {"Trebol", "Pica", "Corazon", "Diamante"};
    private String[] nombresGrupos = {"", "", "Par", "Terna", "Cuarta", "Quinta", "Sexta", "Septima", "Octava", "Novena", "Decima"};

    public void repartir(int[] cantidadUsadaPorCarta, int cantidadBarajas) {
        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            cartas[indiceCarta] = new Carta(generadorAleatorio, cantidadUsadaPorCarta, cantidadBarajas);
        }
    }

    public void mostrar(JPanel panelJugador) {
        panelJugador.removeAll(); // Limpiar las cartas anteriores
        panelJugador.setLayout(null); // Permitir posiciones manuales

        // Recorrer desde la ultima carta hasta la primera para que se superpongan bien
        for (int indiceCarta = cantidadCartasPorJugador - 1; indiceCarta >= 0; indiceCarta--) {
            int posicionHorizontal = margenCartas + indiceCarta * distanciaEntreCartas; // Posicion de la carta
            cartas[indiceCarta].mostrar(panelJugador, posicionHorizontal, margenCartas); // Mostrar carta
        }

        panelJugador.revalidate(); // Actualizar componentes del panel
        panelJugador.repaint(); // Redibujar el panel
    }

    public String obtenerGrupos() {
        if (cartas[0] == null) {
            return "Primero debe repartir las cartas";
        }

        boolean[] cartaPerteneceAGrupo = new boolean[cantidadCartasPorJugador];
        String resultado = "";
        boolean seEncontroGrupo = false;

        // 1. Buscar grupos del mismo nombre: par, terna, cuarta, etc.
        int[] cantidadPorNombre = new int[13];

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            int posicionNombre = cartas[indiceCarta].obtenerNombre().ordinal();
            cantidadPorNombre[posicionNombre]++;
        }

        for (int posicionNombre = 0; posicionNombre < cantidadPorNombre.length; posicionNombre++) {
            if (cantidadPorNombre[posicionNombre] >= 2) {
                resultado += nombresGrupos[cantidadPorNombre[posicionNombre]] + " de "
                        + nombresCartas[posicionNombre] + "\n";
                seEncontroGrupo = true;

                for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
                    if (cartas[indiceCarta].obtenerNombre().ordinal() == posicionNombre) {
                        cartaPerteneceAGrupo[indiceCarta] = true;
                    }
                }
            }
        }

        if (seEncontroGrupo == true) {
            resultado += "\n";
        }

        // 2. Buscar escaleras de la misma pinta
        // Filas: pintas. Columnas: As, 2, 3 ... K
        int[][] cantidadPorPintaYNombre = new int[4][13];

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
            int posicionPinta = cartas[indiceCarta].obtenerPinta().ordinal();
            int posicionNombre = cartas[indiceCarta].obtenerNombre().ordinal();
            cantidadPorPintaYNombre[posicionPinta][posicionNombre]++;
        }

        boolean seEncontroEscalera = false;

        for (int posicionPinta = 0; posicionPinta < 4; posicionPinta++) {
            int posicionNombre = 0;

            while (posicionNombre < 13) {

                if (cantidadPorPintaYNombre[posicionPinta][posicionNombre] > 0) {
                    int posicionInicioEscalera = posicionNombre;
                    int posicionFinEscalera = posicionNombre;

                    // Avanzar mientras la siguiente carta tambien exista en la misma pinta
                    while (posicionFinEscalera + 1 < 13
                            && cantidadPorPintaYNombre[posicionPinta][posicionFinEscalera + 1] > 0) {
                        posicionFinEscalera++;
                    }

                    int cantidadCartasEscalera = posicionFinEscalera - posicionInicioEscalera + 1;

                    if (cantidadCartasEscalera >= 2) {
                        resultado += nombresGrupos[cantidadCartasEscalera] + " de "
                                + nombresPintas[posicionPinta] + " de "
                                + nombresCartas[posicionInicioEscalera] + " a "
                                + nombresCartas[posicionFinEscalera] + "\n";

                        seEncontroEscalera = true;
                        seEncontroGrupo = true;

                        // Marcar las cartas que forman esta escalera
                        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {
                            int pintaDeLaCarta = cartas[indiceCarta].obtenerPinta().ordinal();
                            int nombreDeLaCarta = cartas[indiceCarta].obtenerNombre().ordinal();

                            if (pintaDeLaCarta == posicionPinta
                                    && nombreDeLaCarta >= posicionInicioEscalera
                                    && nombreDeLaCarta <= posicionFinEscalera) {
                                cartaPerteneceAGrupo[indiceCarta] = true;
                            }
                        }
                    }

                    // Continuar despues de la escalera que acabamos de revisar
                    posicionNombre = posicionFinEscalera + 1;

                } else {
                    posicionNombre++;
                }
            }
        }

        if (seEncontroGrupo == false) {
            resultado = "No se encontraron grupos\n";
        }

        if (seEncontroEscalera == true) {
            resultado += "\n";
        }

        // 3. Mostrar cartas sobrantes y calcular puntos
        resultado += "Sobran:\n";
        int puntaje = 0;
        boolean hayCartasSobrantes = false;

        for (int indiceCarta = 0; indiceCarta < cantidadCartasPorJugador; indiceCarta++) {

            if (cartaPerteneceAGrupo[indiceCarta] == false) {
                int posicionNombre = cartas[indiceCarta].obtenerNombre().ordinal();
                int posicionPinta = cartas[indiceCarta].obtenerPinta().ordinal();

                resultado += nombresCartas[posicionNombre] + " de " + nombresPintas[posicionPinta] + "\n";
                puntaje += cartas[indiceCarta].obtenerValor();
                hayCartasSobrantes = true;
            }
        }

        if (hayCartasSobrantes == false) {
            resultado += "Ninguna\n";
        }

        resultado += "\nPuntos:\n" + puntaje;

        return resultado;
    }
}

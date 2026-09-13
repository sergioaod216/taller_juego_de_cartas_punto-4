import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Carta {

    private int indice; // Numero que identifica la carta entre 1 y 52

    public Carta(Random generadorAleatorio, int[] cantidadUsadaPorCarta, int cantidadBarajas) {
        boolean cartaDisponible = false;

        // Seguir buscando hasta encontrar una carta que aun pueda salir
        while (cartaDisponible == false) {
            int indiceGenerado = generadorAleatorio.nextInt(52) + 1;

            // La misma carta solo puede aparecer tantas veces como barajas existan
            if (cantidadUsadaPorCarta[indiceGenerado - 1] < cantidadBarajas) {
                indice = indiceGenerado;
                cantidadUsadaPorCarta[indiceGenerado - 1]++;
                cartaDisponible = true;
            }
        }
    }

    public void mostrar(JPanel panelJugador, int posicionHorizontal, int posicionVertical) {
        String rutaImagen = "/imagenes/CARTA" + indice + ".JPG";
        ImageIcon imagenCarta = new ImageIcon(getClass().getResource(rutaImagen));

        JLabel etiquetaCarta = new JLabel(imagenCarta);
        etiquetaCarta.setBounds(posicionHorizontal, posicionVertical,
                imagenCarta.getIconWidth(), imagenCarta.getIconHeight());
        panelJugador.add(etiquetaCarta);
    }

    public Pinta obtenerPinta() {
        if (indice <= 13) {
            return Pinta.TREBOL;
        } else if (indice <= 26) {
            return Pinta.PICA;
        } else if (indice <= 39) {
            return Pinta.CORAZON;
        } else {
            return Pinta.DIAMANTE;
        }
    }

    public NombreCarta obtenerNombre() {
        int numeroDentroDePinta = indice % 13;

        if (numeroDentroDePinta == 0) {
            numeroDentroDePinta = 13;
        }

        return NombreCarta.values()[numeroDentroDePinta - 1];
    }

    public int obtenerValor() {
        int posicionNombre = obtenerNombre().ordinal();

        // As, 10, Jack, Queen y King valen 10
        if (posicionNombre == 0 || posicionNombre >= 9) {
            return 10;
        }

        // Las demas cartas valen su numero
        return posicionNombre + 1;
    }
}

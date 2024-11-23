package edu.pruebas.prc2_alfonsorincon;

public class Partida {
    public int puntos;
    private Tablero tablero;

    // Constructor
    public Partida(int puntos) {
        this.puntos = puntos;
        this.tablero=new Tablero();
    }
    public Partida() {
        this.tablero=new Tablero();
    }

    // Método para definir la dificultad del juego. Por defecto, se inicia en Fácil
    public void comenzar(int x, int y, int minas, JuegoActivity juego) {
        int[][] array=tablero.establecerMinas(x,y,minas);
        juego.crearTablero(array, x);
    }
}

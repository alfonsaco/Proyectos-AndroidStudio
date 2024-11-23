package edu.pruebas.prc2_alfonsorincon;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class Tablero {
    public int x;
    public int y;
    public int minas;
    private GridLayout gridLayout;
    private Context context;

    // Constructor
    // Constructor
    public Tablero(Context context, GridLayout gridLayout, int x, int y, int minas) {
        this.context = context;
        this.gridLayout = gridLayout; // Guardamos el GridLayout directamente
        this.x = x;
        this.y = y;
        this.minas = minas;
    }
    public Tablero() {

    }

    // Método para situar las minas de forma aleatoria a lo largo del Grid, además de poner los respectivos números en cada casilla
    public int[][] establecerMinas(int anchura, int altura, int numMinas) {
        int[][] tablero=new int[anchura][altura];

        // Método para insertar minas en el tablero de forma aleatoria
        int contMinas=0;
        int random;
        do {
            for (int i=0; i<tablero.length; i++) {
                for (int e=0; e<tablero[i].length; e++) {
                    random=(int) (Math.random()*15);
                    // Verificar que en la posición en cuestión, no haya una mina ya. Si el random es
                    // 5, se colocará una mina.
                    if(random==5 && tablero[i][e]!=-1 && contMinas<numMinas) {
                        contMinas++;
                        tablero[i][e]=-1;
                    }
                }
            }
        } while(contMinas < numMinas);


        // Situar los números
        for (int i=0; i<tablero.length; i++) {
            for (int e=0; e<tablero[i].length; e++) {
                if(tablero[i][e]!=-1) {
                    // Verificar mina de arriba
                    if(i>0 && tablero[i-1][e]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar mina de abajo
                    if(i<tablero.length-1 && tablero[i+1][e]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar mina de la izquierda
                    if(e>0 && tablero[i][e-1]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar mina de la derecha
                    if(e<tablero[i].length-1 && tablero[i][e+1]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar arriba izquierda
                    if(i>0 && e>0 && tablero[i-1][e-1]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar arriba derecha
                    if(i>0 && e<tablero[i].length-1 && tablero[i-1][e+1]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar abajo izquierda
                    if(i<tablero.length-1 && e>0 && tablero[i+1][e-1]==-1) {
                        tablero[i][e]++;
                    }
                    // Verificar abajo derecha
                    if(i<tablero.length-1 && e<tablero[i].length-1 && tablero[i+1][e+1]==-1) {
                        tablero[i][e]++;
                    }
                }
            }
        }

        return tablero;
    }
}

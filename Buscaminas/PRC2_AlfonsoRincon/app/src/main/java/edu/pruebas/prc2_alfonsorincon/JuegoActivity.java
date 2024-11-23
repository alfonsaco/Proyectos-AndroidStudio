package edu.pruebas.prc2_alfonsorincon;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class JuegoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Partida partida;
    private Spinner dialogSpinner;
    private int personajeSeleccionado=R.drawable.submarina;
    // Array de los botones. Como algunos son ImageButtons, y otro Buttons, no se pueden
    // almacenar en un array de Buttons, por ejemplo, por lo que los almaceno en un Array de
    // View, ya que ambos heredan de esta clase.
    private View[][] botones;

    // Valores para resetear la dificultad, en caso de que se cambie
    int anchura=8;
    int altura=8;
    int minas=10;

    // Colores
    int fondoBoton;
    int uno;
    int dos;
    int tres;
    int cuatro;
    int cinco;

    // Contador minas
    int contMina=minas;
    TextView tvContador;

    // Variable para el sonido
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tablero);

        // Colores
        fondoBoton=ContextCompat.getColor(this, R.color.fondo);
        uno=ContextCompat.getColor(this, R.color.primero);
        dos=ContextCompat.getColor(this, R.color.dos);
        tres=ContextCompat.getColor(this, R.color.tres);
        cuatro=ContextCompat.getColor(this, R.color.cuatro);
        cinco=ContextCompat.getColor(this, R.color.cinco);

        tvContador=findViewById(R.id.tvContador);


        // Crear la ToolBar
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Línea de código para poder cambiar el color de los 3 puntitos de la ToolBar
        if(toolbar.getOverflowIcon() != null) {
            toolbar.getOverflowIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        // Partida
        partida=new Partida();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Comenzar la partida al iniciar la actividad
        partida.comenzar(8, 8, 10, this);

        tvContador.setText(String.valueOf(contMina));
    }

    // Funciones del ActionbBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item=menu.findItem(R.id.itemPersonaje);
        item.setIcon(personajeSeleccionado);
        return true;
    }

    // Opciones del ToolBar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        LayoutInflater inflater=getLayoutInflater();
        View dialogInflater=null;

        // Crear el Spinner, en caso de que se haya seleccionado la opción de personajes en el ToolBar
        if(id == R.id.itemPersonaje) {
            dialogInflater = inflater.inflate(R.layout.personaje, null);

            // Spinner de diálogo
            dialogSpinner = dialogInflater.findViewById(R.id.spinner);

            // Agregamos todos los elementos al Spinner
            ArrayList<Items> elementosSpinner = new ArrayList<>();
            elementosSpinner.add(new Items("Bomba clásica", R.drawable.clasica));
            elementosSpinner.add(new Items("TNT", R.drawable.tnt));
            elementosSpinner.add(new Items("Granada", R.drawable.granada));
            elementosSpinner.add(new Items("Mina marina", R.drawable.submarina));
            elementosSpinner.add(new Items("Bomba Mario Bros", R.drawable.mariobros));

            // Creación del adaptador y asignación al Spinner del diálogo
            Adapter adapter = new Adapter(this, elementosSpinner);
            dialogSpinner.setAdapter(adapter);
            dialogSpinner.setOnItemSelectedListener(this);

        } else if(id == R.id.itemReiniciar) {
            partida.comenzar(anchura, altura, minas, JuegoActivity.this);
            contMina=minas;
            tvContador.setText(String.valueOf(contMina));

        } else if(id == R.id.itemConfiguracion) {
            dialogInflater=inflater.inflate(R.layout.configuracion, null);
        } else if(id == R.id.itemInstrucciones) {
            dialogInflater=inflater.inflate(R.layout.intrucciones, null);
        }

        if(dialogInflater != null) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setView(dialogInflater);

            builder.setCancelable(true);

            AlertDialog dialog = builder.create();
            dialog.show();

            // Funciones de la opción de las intrucciones
            if(id == R.id.itemInstrucciones) {
                Button btnOK=dialogInflater.findViewById(R.id.btnOKpersonaje);

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                // Funciones de la opción de la configuración
            } else if(id == R.id.itemConfiguracion) {
                final View finalDialogInflater=dialogInflater;
                Button btnVolver=finalDialogInflater.findViewById(R.id.btnVolver);

                btnVolver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton rdPrincipiante=finalDialogInflater.findViewById(R.id.rdPrincipiante);
                        RadioButton rdAmateur=finalDialogInflater.findViewById(R.id.rdAmateur);
                        RadioButton rdAvanzado=finalDialogInflater.findViewById(R.id.rdAvanzado);

                        // Condicionales para ver que dificultad se ha elegido, y así poder crear una nueva partida
                        // CASO PRINCIPANTE
                        if(rdPrincipiante != null && rdPrincipiante.isChecked()) {
                            partida.comenzar(8, 8, 10, JuegoActivity.this);
                            anchura=8;
                            altura=8;
                            minas=10;
                            contMina=minas;
                            tvContador.setText(String.valueOf(contMina));
                        // CASO AMATEUR
                        } else if(rdAmateur != null && rdAmateur.isChecked()) {
                            partida.comenzar(12, 12, 30, JuegoActivity.this);
                            anchura=12;
                            altura=12;
                            minas=30;
                            contMina=minas;
                            tvContador.setText(String.valueOf(contMina));
                        // CASO AVANZADO
                        } else if(rdAvanzado != null && rdAvanzado.isChecked()) {
                            partida.comenzar(16, 16, 60, JuegoActivity.this);
                            anchura=16;
                            altura=16;
                            minas=60;
                            contMina=minas;
                            tvContador.setText(String.valueOf(contMina));
                        }

                        dialog.dismiss();
                    }
                });

                // Funciones de la opción de elegir personaje
            } else if(id == R.id.itemPersonaje) {
                Button btnOKpersonaje=dialogInflater.findViewById(R.id.btnOKpersonaje);

                btnOKpersonaje.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Usamos invalidateOptionsMenu(), para recrear el ToolBar, y así, poner la nueva imagen que hemos seleccionado.
                        // Gracias a esto, el icono se actualizará.
                        invalidateOptionsMenu();
                        dialog.dismiss();
                    }
                });
            }
        }

        return true;
    }

    // Métodos del Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Items items=(Items)adapterView.getSelectedItem();
        // Una vez seleccionado el nuevo personaje, será este. Lo podremos utilizar más tarde
        personajeSeleccionado=items.getImagen();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Método para crear el grid mediante el array de números
    public void crearTablero(int[][] tablero, int tamañoGrid) {
        android.widget.GridLayout gridLayout=findViewById(R.id.gridLayout);

        // Línea de código, para evitar que cada vez que cambio de dificultad, se superpongan los botones, ya que se sobrecarga
        // la aplicación, y acaba petando. Cone sto, se borran todos los botones, y se crea desde cero el nuevo grid.
        gridLayout.removeAllViews();

        gridLayout.setRowCount(tamañoGrid);
        gridLayout.setColumnCount(tamañoGrid);

        // Definir el tamaño del botón
        //      getDisplayMetrics(): contiene información sobre la pantalla del dispositivo
        //      widthPixels: devuelve el ancho en píxeles
        int dimensionBoton=getResources().getDisplayMetrics().widthPixels / tamañoGrid;
        botones=new View[tamañoGrid][tamañoGrid];

        for (int i=0; i<tablero.length; i++) {
            for (int e=0; e<tablero[i].length; e++) {
                // Caso mina
                if(tablero[i][e] == -1) {
                    ImageButton boton=new ImageButton(this);

                    // Tamaños del botón
                    boton.setLayoutParams(new android.widget.GridLayout.LayoutParams());
                    boton.getLayoutParams().width = dimensionBoton;
                    boton.getLayoutParams().height = dimensionBoton;

                    // Agregar los estilos creados en el XML a los botones
                    boton.setBackgroundResource(R.drawable.estilos_boton);
                    // Línea de código para poder hacer que la imagen se ajuste al tamaño del botón
                    boton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

                    // Guardar el botón en el array de View
                    botones[i][e] = boton;

                    // Configurar comportamiento al hacer click
                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Ponemos la imagen de la mina
                            boton.setImageResource(personajeSeleccionado);
                            boton.setBackgroundColor(fondoBoton);

                            boton.setEnabled(false);

                            reproducirAudio(R.raw.explosion);
                            hasPerdido(tablero);
                        }
                    });

                    boton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            boton.setBackgroundResource(R.drawable.bandera);
                            reproducirAudio(R.raw.click);
                            boton.setEnabled(false);

                            contMina--;
                            tvContador.setText(String.valueOf(contMina));
                            if(contMina == 0) {
                                hasGanado(tablero);
                            }

                            // Ponemos true, para evitar que tras el longClick, se haga un Click
                            return true;
                        }
                    });

                    // Añadir el botón al GridLayout
                    gridLayout.addView(boton);

                } else {
                    // Caso número
                    Button boton=new Button(this);

                    // Tamaños del botón
                    boton.setLayoutParams(new android.widget.GridLayout.LayoutParams());
                    boton.getLayoutParams().width = dimensionBoton;
                    boton.getLayoutParams().height = dimensionBoton;

                    // Agregar los estilos creados en el XML a los botones
                    boton.setBackgroundResource(R.drawable.estilos_boton);

                    // Guardar el botón en el array de View
                    botones[i][e] = boton;

                    // Configurar comportamiento al hacer click
                    final int i1=i;
                    final int e1=e;
                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Si la casilla es un 0, iniciar el algoritmo recursivo
                            if (tablero[i1][e1] == 0) {
                                destaparCeros(i1, e1, tablero);
                            }

                            // Reproducimos Click y mostramos el número
                            reproducirAudio(R.raw.click);
                            mostrarNumero(boton, i1, e1, tablero);
                        }
                    });

                    // En caso de que se ponga un banderín donde no hay minas, se acabará el juego
                    boton.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            hasPerdido(tablero);
                            reproducirAudio(R.raw.lose);
                            boton.setBackgroundResource(R.drawable.falsa_bandera);
                            boton.setText("");

                            return true;
                        }
                    });

                    // Añadir el botón al GridLayout
                    gridLayout.addView(boton);
                }
            }
        }
    }

    private void hasGanado(int[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int e = 0; e < tablero[i].length; e++) {
                View view = botones[i][e];

                // Mostrar todos los botones que sean minas
                if (tablero[i][e] == -1 && view instanceof ImageButton) {
                    ImageButton mina = (ImageButton) view;
                    mina.setImageResource(personajeSeleccionado);
                    mina.setBackgroundColor(fondoBoton);
                    mina.setEnabled(false);

                    // Mostrar todos los botones que no sean minas
                } else if (view instanceof Button) {
                    Button boton = (Button) view;
                    mostrarNumero(boton, i, e, tablero);
                }
            }
        }
        // Reproducimos el sonido de victoria
        reproducirAudio(R.raw.win);

        // Tras 3 segundos, se mostrará un diálogo que permitirá reiniciar el juego
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inflater para poder coger el id del diálogo
                LayoutInflater inflater=getLayoutInflater();
                View dialogInflater=inflater.inflate(R.layout.victoria, null);

                // Builder de la alerta
                AlertDialog.Builder builder=new AlertDialog.Builder(JuegoActivity.this);
                builder.setView(dialogInflater);

                AlertDialog alerta=builder.create();
                alerta.getWindow().setBackgroundDrawableResource(R.color.transparente);

                Button btnReiniciar=dialogInflater.findViewById(R.id.btnVictoriaReiniciar);
                btnReiniciar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        partida.comenzar(anchura, altura, minas, JuegoActivity.this);
                        contMina=minas;
                        tvContador.setText(String.valueOf(contMina));

                        mp.stop();
                        alerta.dismiss();
                    }
                });

                alerta.show();
            }
        }, 2000);
    }

    public void destaparCeros(int fila, int columna, int[][] tablero) {
        // Comprobar que estamos dentro de los límites
        if ((fila >= 0 && fila < tablero.length) && (columna >= 0 && columna < tablero[0].length)) {
            View boton = botones[fila][columna];

            // Comprobamos que el botón no esté deshabilitado, ya que en este caso no se debe destapar
            if (boton.isEnabled()) {
                // Se verifica que no sea mayor de 0. Si lo es, se muestra la casilla y se termina la función
                if(tablero[fila][columna] != 0) {
                    if (boton instanceof Button) {
                        mostrarNumero((Button) boton, fila, columna, tablero);
                    }
                    boton.setEnabled(false);

                // Como es cero, se seguirá ejecutando el código de forma recursiva
                } else {
                    if (boton instanceof Button) {
                        mostrarNumero((Button) boton, fila, columna, tablero);
                    }
                    boton.setEnabled(false);

                    // For para llegar a la celdas adyacentes
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            // Con esto, evitamos que haga nada con la celda actual. El continue nos da la orden de seguir en el bucle con el siguiente valor
                            if (i == 0 && j == 0) continue;

                            // Llamada recursiva para las celdas adyacentes
                            destaparCeros(fila + i, columna + j, tablero);
                        }
                    }
                }
            }
        }
    }

    // Método para mostrar el número con su color, al destapar un botón sin mina
    public void mostrarNumero(Button boton, int i, int e, int[][] tablero) {
        // Cambiar el color y fondo de los botones
        boton.setBackgroundColor(fondoBoton);

        if(tablero[i][e] == 1) {
            boton.setText("1");
            boton.setTextColor(uno);
        } else if(tablero[i][e] == 2) {
            boton.setText("2");
            boton.setTextColor(dos);
        } else if(tablero[i][e] == 3) {
            boton.setText("3");
            boton.setTextColor(tres);
        } else if(tablero[i][e] == 4) {
            boton.setText("4");
            boton.setTextColor(cuatro);
        } else if(tablero[i][e] == 5) {
            boton.setText("5");
            boton.setTextColor(cinco);
        } else if(tablero[i][e] == 6) {
            boton.setText("6");
        }

        // Deshabilitar el botón
        boton.setEnabled(false);
    }

    // Método que se ejecuta cuando tocas una bomba. Se mostrará todo el tablero, y se deshabilitarán
    // todos los botones
    public void hasPerdido(int[][] tablero) {
        for (int i = 0; i < tablero.length; i++) {
            for (int e = 0; e < tablero[i].length; e++) {
                View view = botones[i][e];

                // Mostrar todos los botones que sean minas
                if (tablero[i][e] == -1 && view instanceof ImageButton) {
                    ImageButton mina = (ImageButton) view;
                    mina.setImageResource(personajeSeleccionado);
                    mina.setBackgroundColor(fondoBoton);
                    mina.setEnabled(false);

                    // Mostrar todos los botones que no sean minas
                } else if (view instanceof Button) {
                    Button boton = (Button) view;
                    mostrarNumero(boton, i, e, tablero);
                }
            }
        }
        //Reproducimos el sonido de derrota
        reproducirAudio(R.raw.lose);

        // Tras 2 segundos, se mostrará un diálogo que permitirá reiniciar el juego
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Inflater para poder coger el id del diálogo
                LayoutInflater inflater=getLayoutInflater();
                View dialogInflater=inflater.inflate(R.layout.derrota, null);

                // Builder de la alerta
                AlertDialog.Builder builder=new AlertDialog.Builder(JuegoActivity.this);
                builder.setView(dialogInflater);

                AlertDialog alerta=builder.create();
                // Quitar el color de fondo que tendrá la alerta, según el sistema
                alerta.getWindow().setBackgroundDrawableResource(R.color.transparente);

                Button btnReiniciar=dialogInflater.findViewById(R.id.btnDerrotaReiniciar);
                btnReiniciar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        partida.comenzar(anchura, altura, minas, JuegoActivity.this);
                        contMina=minas;
                        tvContador.setText(String.valueOf(contMina));

                        mp.stop();
                        alerta.dismiss();
                    }
                });

                alerta.show();
            }
        }, 2000);
    }

    //Función para reproducir los audios
    public void reproducirAudio(int ruta) {
        mp=MediaPlayer.create(this, ruta);
        mp.start();
    }

}
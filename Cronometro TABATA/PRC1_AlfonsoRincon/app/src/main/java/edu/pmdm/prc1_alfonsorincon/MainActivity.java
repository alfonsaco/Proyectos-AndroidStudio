package edu.pmdm.prc1_alfonsorincon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Declaramos los elementos
    ImageButton btnPlay;
    EditText etxtSets;
    EditText etxtWork;
    EditText etxtRest;
    CountDownTimer timer;
    TextView txtSecondsLeft;
    ConstraintLayout constraintLayout;
    TextView txtSeriesLeft;
    TextView txtState;
    TextView txtSeriesLeftMessage;
    boolean state=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Variables asociadas a los elementos
        txtSecondsLeft=findViewById(R.id.txtSecondsLeft);
        btnPlay=findViewById(R.id.btnPlay);
        etxtSets=findViewById(R.id.etxtSets);
        etxtWork=findViewById(R.id.etxtWork);
        etxtRest=findViewById(R.id.etxtRest);
        constraintLayout=findViewById(R.id.constraintLayout);
        txtSeriesLeft=findViewById(R.id.txtSeriesLeft);
        txtState=findViewById(R.id.txtState);
        txtSeriesLeftMessage=findViewById(R.id.txtSeriesLeftMessage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Comienzo del programa al pulsar en el botón
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Try catch, para evitar que se puedan introduir valores muy grandes (Ej: 111111111111111111). Esto sucede
                // ya que el valor maximo de int es 2,147,483,647, y a partir de este, Java ono puede convertir más ese
                // String a int. Por eso, se saca esta excepción.
                try {
                    if(!etxtRest.getText().toString().isEmpty() && !etxtSets.getText().toString().isEmpty() && !etxtWork.getText().toString().isEmpty() && state==true) {
                        if(Integer.parseInt(etxtRest.getText().toString())>0 && Integer.parseInt(etxtSets.getText().toString())>0 && Integer.parseInt(etxtWork.getText().toString())>0) {
                            if(Integer.parseInt(etxtRest.getText().toString())>1000 || Integer.parseInt(etxtSets.getText().toString())>100 || Integer.parseInt(etxtWork.getText().toString())>1000) {
                                mostrarAlerta(R.string.dialog_title_highValues, R.string.dialog_text_highValues);
                            } else {
                                state=false;

                                int sets=Integer.parseInt(etxtSets.getText().toString());
                                int work=Integer.parseInt(etxtWork.getText().toString());
                                int rest=Integer.parseInt(etxtRest.getText().toString());

                                exerciseCounter(work, rest, sets);
                            }
                        } else {
                            mostrarAlerta(R.string.dialog_title_invalidValues, R.string.dialog_text_invalidValues);
                        }

                    } else {
                        if(state==false) {
                            mostrarAlerta(R.string.dialog_title_wait, R.string.dialog_text_wait);
                        } else {
                            mostrarAlerta(R.string.dialog_title_empty,R.string.dialog_text_empty);
                        }
                    }
                } catch(NumberFormatException e) {
                    mostrarAlerta(R.string.dialog_title_highValues, R.string.dialog_text_highValues);
                }
            }

        });

    }

    // Función para mostrar una alerta. Le pasaremos los valores de titulo y su descripción
    public void mostrarAlerta(int title, int text) {
        AlertDialog alerta=new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // Función para el contador de descanso
    private void restCounter(int work, int rest, int sets) {
        // Contador para determinar las series restantes
        int cont=sets;
        cont--;
        int finalCont = cont;

        if(finalCont > 0) {
            cambiarColor(R.color.background_red, R.color.text_red, "red");

            timer=new CountDownTimer(rest*1000, 1000) {
                @Override
                public void onTick(long l) {
                    txtSecondsLeft.setText(String.valueOf((l/1000)+1));
                    txtState.setText("REST");
                }

                @Override
                public void onFinish() {
                    playSound(R.raw.beep);
                    exerciseCounter(work, rest, finalCont);
                }
            }.start();
        } else {
            // Restaurar colores iniciales
            cambiarColor(R.color.background_grey, R.color.text_grey, "grey");
            // Cambiar el texto de los TextView
            txtSeriesLeft.setText(String.valueOf(finalCont));
            txtSecondsLeft.setText(String.valueOf(finalCont));
            txtState.setText("FINISHED");
            // Ponemos el boolean a true, para que podamos volver a ejecutar la función
            state=true;
            playSound(R.raw.gong);
        }
    }

    // Función para ejecutar el contador de ejercicio, y cambiar el fondo a verde.
    // Se pondrá fuera del onCreate, ya que no se pueden definir métodos dentro de otro
    private void exerciseCounter(int work, int rest, int sets) {
        cambiarColor(R.color.background_green, R.color.text_green, "green");

        // Tiempo en milisegundos
        int totalFinal=work*1000;
        timer=new CountDownTimer(totalFinal, 1000) {
            @Override
            public void onTick(long l) {
                txtSecondsLeft.setText(String.valueOf((l/1000)+1));
                txtSeriesLeft.setText(String.valueOf(sets-1));
                txtState.setText("WORK");
            }

            @Override
            public void onFinish() {
                // Reproducir el sonido beep, siempre que no se esté en la última serie
                if((sets-1) != 0) {
                    playSound(R.raw.beep);
                }
                restCounter(work, rest, sets);
            }
        }.start();
    }

    private void cambiarColor(int bakgroundColor, int textColor, String value) {
        constraintLayout.setBackgroundColor(ContextCompat.getColor(this, bakgroundColor));

        // Obtenemos el valor del color para usarlo en los textos
        int color=ContextCompat.getColor(this, textColor);
        txtSecondsLeft.setTextColor(color);
        txtState.setTextColor(color);
        txtSeriesLeft.setTextColor(color);
        txtSeriesLeftMessage.setTextColor(color);
        etxtRest.setTextColor(color);
        etxtSets.setTextColor(color);
        etxtWork.setTextColor(color);

        // Variables para el botón. Aunque la función no sea muy recomendada, es la única manera de poder utilizar las variables del método
        // y cambiar los colores del .xml
        int btnStroke=getResources().getIdentifier("button_"+value,"color",getPackageName());
        int btnBackground=getResources().getIdentifier("btnBackground_"+value, "color", getPackageName());

        GradientDrawable btnColours=(GradientDrawable)btnPlay.getBackground();
        btnColours.setColor(ContextCompat.getColor(this, btnBackground));
        // El 10 es equivalente al grosor del borde del botón
        btnColours.setStroke(10,ContextCompat.getColor(this, btnStroke));
    }

    // Función para reproducir el sonido
    public void playSound(int soundID) {
        MediaPlayer sound=MediaPlayer.create(
                getApplicationContext(), soundID);
        sound.start();
    }
}
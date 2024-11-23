package edu.pruebas.prc2_alfonsorincon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btnPlay;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnPlay=findViewById(R.id.btnPlay);
        reproducirAudio(R.raw.cancion_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Iniciar el juego al pulsar en el botón
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), JuegoActivity.class);
                // Paro la música de fondo, y reproduzco el sonido de play
                mp.stop();
                reproducirAudio(R.raw.play);
                startActivity(i);
            }
        });
    }

    public void reproducirAudio(int ruta) {
        mp =MediaPlayer.create(this, ruta);
        mp.setVolume(1.0f,1.0f);
        mp.start();
    }

}
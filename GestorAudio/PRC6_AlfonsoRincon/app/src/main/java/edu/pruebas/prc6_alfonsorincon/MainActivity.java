package edu.pruebas.prc6_alfonsorincon;

import android.media.MediaPlayer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.MediaController;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaController.MediaPlayerControl {

    private RecyclerView recyclerCanciones;
    private CancionAdapter adapter;
    private List<Cancion> listaCanciones;
    private List<Cancion> listaCancionesSinFiltros;

    private boolean[] checkboxesSeleccionados;

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;

    private int posicionCancion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Toolbar de opciones
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerCanciones=findViewById(R.id.recyclerCanciones);
        recyclerCanciones.setLayoutManager(new LinearLayoutManager(this));

        checkboxesSeleccionados = new boolean[]{false, false, false};

        String jsonString=leerJSON("recursosList.json");
        // Obtener los datos del JSON
        if(jsonString!=null){
            Gson gson=new Gson();
            RecursosWrapper wrapper=gson.fromJson(jsonString, RecursosWrapper.class);
            listaCanciones=wrapper.getListaRecursos();

            listaCancionesSinFiltros=new ArrayList<>(listaCanciones);

            adapter=new CancionAdapter(this, listaCanciones);
            recyclerCanciones.setAdapter(adapter);

        }else{
            listaCanciones=new ArrayList<>();
            listaCancionesSinFiltros=new ArrayList<>();
        }

        mediaController=new MediaController(this);
        mediaController.setAnchorView(findViewById(R.id.main));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para leer el JSON
    private String leerJSON(String archivo) {
        String json = null;

        try {
            InputStream is=getAssets().open(archivo);
            int tamaño=is.available();
            byte[] buffer=new byte[tamaño];
            is.read(buffer);
            is.close();
            json=new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    public void reproducirCancion(int cancionID) {
        // Para controlar que no haya ya nada reproduciendo
        if (mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }

        mediaPlayer=MediaPlayer.create(this, cancionID);

        // Reproducir canción y mostrar MediaControlelr
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaController.setMediaPlayer(this);
            mediaController.setEnabled(true);
            // Se pone el "0" para evitar que desaparezca el MediaController a los pocos segundos
            mediaController.show(40000);
        }
    }

    public void setListaCancionesSinFiltros(List<Cancion> listaCancionesSinFiltros) {
        this.listaCancionesSinFiltros = listaCancionesSinFiltros;
    }

    @Override
    public void start() {
        if(mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public void seekTo(int pos) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(pos);
        }
    }

    @Override
    public boolean isPlaying() {
        return (mediaPlayer != null) && mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        if (mediaPlayer != null) {
            return mediaPlayer.getAudioSessionId();
        }
        return 0;
    }

    // Inflar las opciones del ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemSeleccionado=item.getItemId();

        if(itemSeleccionado == R.id.opcionFiltrar) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);

            View dialogView=getLayoutInflater().inflate(R.layout.dialog_filtrar, null);

            AlertDialog dialog=builder.setView(dialogView).create();

            CheckBox checkVideo=dialogView.findViewById(R.id.checkVideo);
            CheckBox checkAudio=dialogView.findViewById(R.id.checkAudio);
            CheckBox checkStream=dialogView.findViewById(R.id.checkStream);

            // Dejar la casilla seleccionada por el usuario al desplegar el Dialog
            if (checkboxesSeleccionados[0]) {
                checkAudio.setChecked(true);
            }
            if (checkboxesSeleccionados[1]) {
                checkVideo.setChecked(true);
            }
            if (checkboxesSeleccionados[2]) {
                checkStream.setChecked(true);
            }

            Button btnFiltrar=dialogView.findViewById(R.id.btnFiltrar);
            btnFiltrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkboxesSeleccionados[0] = checkAudio.isChecked();
                    checkboxesSeleccionados[1] = checkVideo.isChecked();
                    checkboxesSeleccionados[2] = checkStream.isChecked();

                    List<Cancion> listaFiltrada = new ArrayList<>();

                    if(!checkVideo.isChecked() && !checkAudio.isChecked() && !checkStream.isChecked()) {
                        listaFiltrada=new ArrayList<>(listaCancionesSinFiltros);

                    } else {
                        for(Cancion c : listaCancionesSinFiltros) {
                            boolean añadir=false;
                            if(c.getTipo().equals("0") && checkAudio.isChecked()) {
                                añadir=true;
                            }
                            if(c.getTipo().equals("1") && checkVideo.isChecked()) {
                                añadir=true;
                            }
                            if(c.getTipo().equals("2") && checkStream.isChecked()) {
                                añadir=true;
                            }
                            if(añadir) {
                                listaFiltrada.add(c);
                            }
                        }
                    }

                    listaCanciones.clear();
                    listaCanciones.addAll(listaFiltrada);
                    adapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    // Parar la música al cambiar a la actividad video o al salir
    @Override
    protected void onStop() {
        super.onStop();

        if(mediaPlayer != null) {
            if(mediaPlayer.isPlaying()) {
                posicionCancion=mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mediaPlayer != null) {
            mediaPlayer.seekTo(posicionCancion);
            mediaPlayer.start();
            mediaController.show();
        }
    }
}

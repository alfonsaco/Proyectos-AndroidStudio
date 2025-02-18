package edu.pruebas.prc6_alfonsorincon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        videoView=findViewById(R.id.videoView);
        mediaController=new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        Intent intent=getIntent();
        String nombreVideo=intent.getStringExtra("uri");
        int tipo=intent.getIntExtra("tipo", 0);

        // Abrir video local
        if(tipo == 1) {
            if(nombreVideo != null) {
                int videoID=getResources().getIdentifier(nombreVideo, "raw", getPackageName());
                if(videoID != 0) {
                    Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoID);
                    videoView.setVideoURI(videoUri);
                    videoView.start();
                }
            } else {
                Toast.makeText(this, "Video no disponible", Toast.LENGTH_SHORT);
            }

        // Abrir video Streaming
        } else if(tipo ==2 ) {
            if(nombreVideo != null && !nombreVideo.isEmpty()) {
                Uri videoUri=Uri.parse(nombreVideo);
                videoView.setVideoURI(videoUri);
                // Caso de error
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        Toast.makeText(VideoActivity.this, "Video no disponible", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                videoView.start();
            } else {
                Toast.makeText(this, "Video no disponible", Toast.LENGTH_SHORT);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
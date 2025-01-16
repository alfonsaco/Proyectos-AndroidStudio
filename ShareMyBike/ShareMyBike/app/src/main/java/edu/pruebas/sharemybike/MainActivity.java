package edu.pruebas.sharemybike;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageButton btnUbi;
    Button btnLogin;
    TextView txtCodPostal;
    EditText etxtEmail;
    // Para obtener la ubicación
    private FusedLocationProviderClient fusedLocationClient;

    // Con este boolean se verifica si ya se ha agregado una ubicación
    private boolean login=true;

    private double ultimaLatitud=0;
    private double ultimaLongitud=0;

    private ActivityResultLauncher<String[]> pedirPermisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos los componentes
        btnUbi=findViewById(R.id.btnUbi);
        btnLogin=findViewById(R.id.btnLogin);
        txtCodPostal=findViewById(R.id.txtCodPostal);
        etxtEmail=findViewById(R.id.etxtEmail);

        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this);

        // Lanzador de permiso. Se verá si el usuario los acepta o no
        // Es un Launcher, se lanzará en el método pedirPermisosDeUbicacion()
        pedirPermisos=registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    // Para acceder a la ubicación mediante GPS
                    Boolean fineLocation=result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    // Para acceder a la ubicación mediante Internet
                    Boolean coarseLocation=result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                    // Si se ha aceptado, recibiremos la ubicación
                    if (fineLocation != null && fineLocation) {
                        obtenerUbicacion();
                    // Para el caso en que no esté disponible el fineLocation
                    } else if (coarseLocation != null && coarseLocation) {
                        obtenerUbicacion();
                    // Cuando se rechace el permiso
                    } else {
                        Toast.makeText(this, R.string.ubi_denegada, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // En el caso de que se pulse este botón, se lanza el método que dispara el Launcher
        btnUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedirPermisosDeUbicacion();
            }
        });

        // Al pulsar en login, se verifica que se hayan aportado los datos necesarios
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=etxtEmail.getText().toString();

                // Verificamos que se ha agregado la ubicación
                if(login == false) {
                    Toast.makeText(MainActivity.this,  R.string.obtener_ubicacion, Toast.LENGTH_SHORT).show();
                } else {
                    // Verificamos que se haya introducido un email
                    if(email.isEmpty() ||email == null) {
                        Toast.makeText(MainActivity.this, R.string.agregar_email, Toast.LENGTH_SHORT).show();
                    } else {
                        // Verificamos que el email sea realmente un email
                        if(esEmail(email)) {
                            Intent intent=new Intent(MainActivity.this, BikeActivity.class);
                            startActivity(intent);
                            // Transición para la pantalla
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        } else {
                            Toast.makeText(MainActivity.this, R.string.email_no_valido, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    // Método para disparar el Launcher
    private void pedirPermisosDeUbicacion() {
        pedirPermisos.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    // Método para obtener la ubicación
    private void obtenerUbicacion() {
        // Se verifica si el permiso de ubicación ha sido acpetado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(ubicacion -> {
                        if (ubicacion != null) {
                            ultimaLatitud=ubicacion.getLatitude();
                            ultimaLongitud=ubicacion.getLongitude();

                            // Abrir Google Maps con la ubicación actual
                            abrirMaps(ultimaLatitud, ultimaLongitud);

                            // Tras haber hecho esto, por fin podremos hacer login, ya que se ha obtenido la ubicación
                            login = true;

                        } else {
                            Toast.makeText(this, R.string.ubi_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, R.string.ubi_denegada, Toast.LENGTH_SHORT).show();
        }
    }

    // Método para que al abrir el Google Maps, se vaya a las coordenadas obtenidas
    private void abrirMaps(double latitud, double longitud) {
        // String con las coordenadas para utilizarlas en el Intent
        String geoUri="geo:0,0?q=" + latitud + "," + longitud;
        Intent intent=new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(geoUri));

        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    // Método para obtener la dirección postal al volver a la aplicaicón. Esta se pone en el txtCodPostal
    private void obtenerDireccionPostal(double latitud, double longitud) {
        // Con el Geocoder convertiremos estas coordenadas en una Dirección Postal
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            // Se utiliza List porque geocoder puede devolver varios resultados
            List<Address> direcciones=geocoder.getFromLocation(latitud, longitud, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                String dirPostal=direcciones.get(0).getAddressLine(0);
                txtCodPostal.setText(dirPostal);

            } else {
                txtCodPostal.setText(R.string.direccion_postal_noDisp);
            }

        } catch (IOException e) {
            e.printStackTrace();
            txtCodPostal.setText(R.string.direccion_postal_error);
        }
    }

    // Al volver a la app, se pondrá la nueva Dirección Postal
    @Override
    protected void onResume() {
        super.onResume();

        if (ultimaLatitud != 0 && ultimaLongitud != 0) {
            obtenerDireccionPostal(ultimaLatitud, ultimaLongitud);
        }
    }

    // Método para verificar si el correo obtenido es un email o no
    private boolean esEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

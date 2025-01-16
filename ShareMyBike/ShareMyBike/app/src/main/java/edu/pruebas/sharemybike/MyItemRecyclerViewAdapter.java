package edu.pruebas.sharemybike;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.pruebas.sharemybike.bikes.BikesContent;

// Adapter para conectar los datos (bikeList) con el RecyclerView
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<BikesContent.Bike> bikeList;
    // Contexto de la aplicación para acceder a recursos
    private final Context context;

    // Constructor
    public MyItemRecyclerViewAdapter(Context context, List<BikesContent.Bike> items) {
        this.context = context;
        this.bikeList = items;
    }

    // Método que crea las vistas individuales de cada fila
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items_recycler, parent, false);
        return new ViewHolder(view);
    }

    // Método que vincula los datos de una bicicleta con su vista correspondiente
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        BikesContent.Bike bike=bikeList.get(position);

        // Se asignan los valores a cada elemento del RecyclerView
        holder.txtAutor.setText(bike.getOwner());
        holder.txtDescripcion.setText(bike.getDescription());
        holder.imagenBici.setImageBitmap(bike.getPhoto());
        holder.txtCiudad.setText(bike.getCity());
        holder.txtDireccion.setText(bike.getLocation());

        // Configura el botón para enviar un Email
        holder.btnEmailAutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Contenido que inserta el email, el asunto y el mensaje
                String contenidoEnviar="mailto:" + Uri.encode(bike.getEmail()) +
                        "?subject=" + Uri.encode("Bike Request") +
                        "&body=" + Uri.encode("Dear " + bike.getOwner() + ",\n\n" +
                        "I'd like to use your bike at " + bike.getLocation() + " (" + bike.getCity() + ").\n\n" +
                        "Can you confirm its availability?\n\nKindest regards!");
                Uri uri=Uri.parse(contenidoEnviar);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                context.startActivity(emailIntent);
            }
        });

    }

    // Devuelve la cantidad de elementos en la lista
    @Override
    public int getItemCount() {
        return bikeList.size();
    }


    // Clase que representa cada fila
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenBici;
        TextView txtAutor;
        TextView txtDescripcion;
        ImageButton btnEmailAutor;
        TextView txtDireccion;
        TextView txtCiudad;

        public ViewHolder(View view) {
            super(view);

            // Obtenemos cada componente
            imagenBici=view.findViewById(R.id.imagenBici);
            txtAutor=view.findViewById(R.id.txtAutor);
            txtDescripcion=view.findViewById(R.id.txtDescripcion);
            btnEmailAutor=view.findViewById(R.id.btnEmailAutor);
            txtDireccion=view.findViewById(R.id.txtDireccion);
            txtCiudad=view.findViewById(R.id.txtCiudad);
        }
    }
}

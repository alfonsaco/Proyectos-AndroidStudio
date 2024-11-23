package edu.pruebas.prc2_alfonsorincon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
/*
    CLASE PARA CREAR EL SPINNER, CON TODOS SUS ITEMS
 */
public class Adapter extends ArrayAdapter<Items> {

    // Constructor
    public Adapter(@NonNull Context context, ArrayList<Items> listaItems) {
        super(context, 0, listaItems);
    }

    // Método para mostrar la vista de cada elemento que se mostrará en la parte principal del Spinner
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return vistaCreada(position, convertView, parent);
    }

    // Método para mostrar la vista de cada elemento en la lista desplegable del Spinner
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return vistaCreada(position, convertView, parent);
    }

    // Método para crear las listas que se mostrarán en los otros dos métodos
    public View vistaCreada(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout, parent, false);
        }

        Items items=getItem(position);

        // Se obtienen la imagen y el texto
        ImageView imagen=convertView.findViewById(R.id.ivCustomSpinner);
        TextView texto=convertView.findViewById(R.id.tvCustomSpinner);
        if(items != null) {
            imagen.setImageResource(items.getImagen());
            texto.setText(items.getTexto());
        }

        return convertView;
    }
}

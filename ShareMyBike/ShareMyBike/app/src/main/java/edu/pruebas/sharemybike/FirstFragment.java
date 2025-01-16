package edu.pruebas.sharemybike;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;

import edu.pruebas.sharemybike.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    CalendarView calendarView;
    TextView txtFecha;
    boolean cambiarFragment=false;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding=FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.calendarView);
        txtFecha = view.findViewById(R.id.txtFecha);

        // SharedPreferences para guardar la fecha
        sharedPreferences = getActivity().getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        String fecha=sharedPreferences.getString("fecha","Select the date to list the available bikes");
        txtFecha.setText(fecha);

        // Botón para pasar al siguiente Fragment
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Evitar cambiar de fragment si no se ha seleccionado fecha
                if (cambiarFragment == false) {
                    Toast.makeText(getContext(), "Select a date", Toast.LENGTH_SHORT).show();

                } else {
                    // Transición entre Fragments
                    NavOptions animacionNav=new NavOptions.Builder().setExitAnim(R.anim.left_out).setEnterAnim(R.anim.right_in).build();
                    NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, null, animacionNav);

                }
            }
        });

        // Cambio de selección del día
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int año, int mes, int dia) {
                // Aquí puedes manejar el cambio de fecha
                String fechaSeleccionada=dia + "/" + (mes + 1) + "/" + año;

                // Guardar la nueva fecha en SharedPreferences
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("fecha", fechaSeleccionada);
                editor.apply();

                txtFecha.setText(fechaSeleccionada);

                // Ya podemos cambiar de fragment
                cambiarFragment=true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
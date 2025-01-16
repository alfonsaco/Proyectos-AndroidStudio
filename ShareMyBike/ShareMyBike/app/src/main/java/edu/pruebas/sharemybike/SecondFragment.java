package edu.pruebas.sharemybike;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.pruebas.sharemybike.bikes.BikesContent;
import edu.pruebas.sharemybike.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    RecyclerView recicler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second, container, false);

        recicler=view.findViewById(R.id.recicler);
        recicler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Placeholder para cuando cargen los elementos. Primero estará visible el progressBar, y el resto invisible
        ProgressBar placeholder=view.findViewById(R.id.progressBar);
        placeholder.setVisibility(View.VISIBLE);
        recicler.setVisibility(View.GONE);

        // Cargar los datos del JSON
        BikesContent.loadBikesFromJSON(requireContext());

        // Tras 1 segundo, aparecen los resultados. 
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                placeholder.setVisibility(View.GONE);
                recicler.setVisibility(View.VISIBLE);
                recicler.setAdapter(new MyItemRecyclerViewAdapter(requireContext(), BikesContent.ITEMS));
            }
        }, 1000);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
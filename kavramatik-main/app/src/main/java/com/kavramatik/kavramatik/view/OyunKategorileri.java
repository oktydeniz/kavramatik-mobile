package com.kavramatik.kavramatik.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.kavramatik.kavramatik.databinding.FragmentOyunKategorileriBinding;

public class OyunKategorileri extends Fragment {

    private FragmentOyunKategorileriBinding binding;

    public OyunKategorileri() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOyunKategorileriBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actions();
    }

    private void actions() {
        binding.colors.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarRenk();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.shapes.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarSekil();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.dimensions.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarBoyut();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.directions.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarYon();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.numbers.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarSayi();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.emotions.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarDuygu();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.quantities.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarMiktar();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.senses.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarDuyu();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.opposites.setOnClickListener(v -> {
            NavDirections directions = OyunKategorileriDirections.actionOyunKategorileriToOyunlarZit();
            Navigation.findNavController(v).navigate(directions);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
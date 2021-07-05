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
import com.kavramatik.kavramatik.databinding.FragmentOyunlarZitBinding;

public class OyunlarZıt extends Fragment {

    private FragmentOyunlarZitBinding binding;

    public OyunlarZıt() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOyunlarZitBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actions();
    }

    private void actions() {
        binding.eslestirme.setOnClickListener(v -> {
            NavDirections directions = OyunlarZıtDirections.actionOyunlarZitToZitOyunFragment();
            Navigation.findNavController(v).navigate(directions);
        });
        binding.hafizaKartlari.setOnClickListener(v -> {
            NavDirections directions = OyunlarZıtDirections.actionOyunlarZitToHafizaKartiZit();
            Navigation.findNavController(v).navigate(directions);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
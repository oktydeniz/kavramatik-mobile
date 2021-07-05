package com.kavramatik.kavramatik.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentChoiceBinding;

public class ChoiceFragment extends Fragment {

    private FragmentChoiceBinding binding;

    public ChoiceFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChoiceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actions();
    }

    private void actions() {
        binding.educationImageView.setOnClickListener(v -> {
            NavDirections directions = ChoiceFragmentDirections.actionChoiceFragmentToEducationCategories();
            Navigation.findNavController(v).navigate(directions);
        });
        //binding.gameImageView.setOnClickListener(v -> Toast.makeText(getContext(), getResources().getString(R.string.not_ready_yet), Toast.LENGTH_LONG).show());
        binding.gameImageView.setOnClickListener(v -> {
            NavDirections directions = ChoiceFragmentDirections.actionChoiceFragmentToOyunKategorileri();
            Navigation.findNavController(v).navigate(directions);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.userSettings) {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_choiceFragment_to_profileFragment);
        }
        return super.onOptionsItemSelected(item);
    }
}
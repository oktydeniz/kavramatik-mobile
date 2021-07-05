package com.kavramatik.kavramatik.view.education;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.adapter.ColorRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentColorBinding;
import com.kavramatik.kavramatik.model.ColorModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.ColorViewModel;

import java.util.List;

public class ColorFragment extends Fragment implements ImageClickInterface {
    private ColorViewModel colorViewModel;
    private TextToSpeech textToSpeech;
    private FragmentColorBinding binding;
    private ColorRecyclerView adapterColor;
    private int nextOne = 1;
    private int previous;
    private List<ColorModel> colorModels;

    public ColorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentColorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boolean isFirst = SharedPreferencesManager.getEducationAssistantD(requireContext());
        if (isFirst) {
            textToSpeech = new TextToSpeech(getContext(), status -> GoogleTTS.getSpeech(getResources().getString(R.string.education_assistant), getContext(), status, this.textToSpeech));
            AppAlertDialogs.educationAssistant(requireContext());
            SharedPreferencesManager.setEducationAssistantD(requireContext(), false);
        }
        colorViewModel = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);
        colorViewModel.getData();
        adapterColor = new ColorRecyclerView(this);
        observeData();

    }


    private void observeData() {
        colorViewModel.loading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.colorProgress.setVisibility(View.VISIBLE);
            } else {
                binding.colorProgress.setVisibility(View.GONE);
            }
        });
        colorViewModel.error.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.colorErrorText.setVisibility(View.VISIBLE);
            } else {
                binding.colorErrorText.setVisibility(View.GONE);
            }
        });
        colorViewModel.colorModel.observe(getViewLifecycleOwner(), model -> {
            if (model.size() >= 1) {
                colorModels = model;
                show(colorModels.get(0));
                binding.colorNext.setVisibility(View.VISIBLE);
                binding.colorMatch.setVisibility(View.VISIBLE);
            }
        });
        actions();
    }

    private void actions() {
        binding.colorNext.setOnClickListener(v -> {
            if (nextOne < colorModels.size()) {
                show(colorModels.get(nextOne));
                nextOne++;
                binding.colorBack.setVisibility(View.VISIBLE);
            } else {
                Scores.updateScore(requireContext(), Scores.COLOR_SCORE);
                show(colorModels.get(0));
                nextOne = 1;
                binding.colorBack.setVisibility(View.GONE);
            }
        });
        binding.colorBack.setOnClickListener(v -> {

            previous = nextOne - 2;
            if (previous >= 0) {
                show(colorModels.get(previous));
                nextOne--;
            } else {
                binding.colorBack.setVisibility(View.GONE);
            }
        });
        binding.colorMatch.setOnClickListener(v -> {
            NavDirections directions = ColorFragmentDirections.actionColorFragmentToColorMatchFragment();
            Navigation.findNavController(v).navigate(directions);
        });
    }

    private void show(ColorModel colorModel) {
        adapterColor.addNewColor(colorModel);
        binding.colorRecyclerView.setAdapter(adapterColor);
    }

    @Override
    public void onItemClick(String text) {
        textToSpeech = new TextToSpeech(getContext(), status -> GoogleTTS.getSpeech(text, getContext(), status, this.textToSpeech));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        GoogleTTS.shotDownTTS(this.textToSpeech);
    }
}
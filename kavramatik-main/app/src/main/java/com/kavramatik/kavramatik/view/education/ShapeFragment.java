package com.kavramatik.kavramatik.view.education;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.adapter.ShapeRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentShapeBinding;
import com.kavramatik.kavramatik.model.ShapeModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.ShapeViewModel;

import java.util.List;

public class ShapeFragment extends Fragment implements ImageClickInterface {
    private FragmentShapeBinding binding;
    private TextToSpeech textToSpeech;
    private ShapeViewModel shapeViewModel;
    private ShapeRecyclerView adapter;
    private List<ShapeModel> shapeModels;
    private int nextOne = 1;
    private int previous;

    public ShapeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShapeBinding.inflate(inflater, container, false);
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
        shapeViewModel = new ViewModelProvider(requireActivity()).get(ShapeViewModel.class);
        shapeViewModel.getData();
        adapter = new ShapeRecyclerView(this);
        observeData();
        actions();
    }

    private void observeData() {
        shapeViewModel.loading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.progressBarShape.setVisibility(View.VISIBLE);
            } else {
                binding.progressBarShape.setVisibility(View.GONE);
            }
        });
        shapeViewModel.error.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.shapeErrorText.setVisibility(View.VISIBLE);
            } else {
                binding.shapeErrorText.setVisibility(View.GONE);
            }
        });
        shapeViewModel.shapeModel.observe(getViewLifecycleOwner(), model -> {
            if (model.size() >= 1) {
                binding.shapeNextButton.setVisibility(View.VISIBLE);
                shapeModels = model;
                show(shapeModels.get(0));
            }
        });
    }

    private void actions() {
        binding.shapeNextButton.setOnClickListener(v -> {
            if (nextOne < shapeModels.size()) {
                show(shapeModels.get(nextOne));
                nextOne++;
                binding.shapeBackButton.setVisibility(View.VISIBLE);
            } else {
                Scores.updateScore(requireContext(), Scores.SHAPE_SCORE);
                show(shapeModels.get(0));
                nextOne = 1;
                binding.shapeBackButton.setVisibility(View.GONE);
            }
        });
        binding.shapeBackButton.setOnClickListener(v -> {
            previous = nextOne - 2;
            if (previous >= 0) {
                show(shapeModels.get(previous));
                nextOne--;
            } else {
                binding.shapeBackButton.setVisibility(View.GONE);
            }
        });
    }

    private void show(ShapeModel shapeModel) {
        adapter.addItem(shapeModel);
        binding.shapeRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(String text) {
        textToSpeech = new TextToSpeech(getContext(), status -> GoogleTTS.getSpeech(text, getContext(), status, this.textToSpeech));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        GoogleTTS.shotDownTTS(this.textToSpeech);
    }
}
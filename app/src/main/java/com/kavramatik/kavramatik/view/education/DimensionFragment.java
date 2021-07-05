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
import com.kavramatik.kavramatik.adapter.DimensionRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentDimensionBinding;
import com.kavramatik.kavramatik.model.DimensionModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.DimensionViewModel;

import java.util.List;

public class DimensionFragment extends Fragment implements ImageClickInterface {
    private FragmentDimensionBinding binding;
    private TextToSpeech textToSpeech;
    private int nextOne = 1;
    private int previous;
    private DimensionViewModel viewModel;
    private List<DimensionModel> dimensionModelList;
    private DimensionRecyclerView adapter;

    public DimensionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDimensionBinding.inflate(inflater, container, false);
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
        viewModel = new ViewModelProvider(requireActivity()).get(DimensionViewModel.class);
        adapter = new DimensionRecyclerView(this);
        viewModel.getData();
        observeData();
    }

    private void observeData() {
        viewModel.isLoading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.dimensionProgress.setVisibility(View.VISIBLE);
            } else {
                binding.dimensionProgress.setVisibility(View.GONE);
            }
        });
        viewModel.isError.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.dimensionErrorText.setVisibility(View.VISIBLE);
            } else {
                binding.dimensionErrorText.setVisibility(View.GONE);
            }
        });
        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
            if (model.size() >= 1) {
                binding.dimensionNext.setVisibility(View.VISIBLE);
                dimensionModelList = model;
                show(dimensionModelList.get(0));
            }
        });
        actions();
    }

    private void actions() {
        binding.dimensionNext.setOnClickListener(v -> {
            if (nextOne < dimensionModelList.size()) {
                show(dimensionModelList.get(nextOne));
                nextOne++;
                binding.dimensionBack.setVisibility(View.VISIBLE);

            } else {
                Scores.updateScore(requireContext(), Scores.DIMENSION_SCORE);
                show(dimensionModelList.get(0));
                nextOne = 1;
                binding.dimensionBack.setVisibility(View.GONE);
            }
        });
        binding.dimensionBack.setOnClickListener(v -> {
            previous = nextOne - 2;
            if (previous >= 0) {
                show(dimensionModelList.get(previous));
                nextOne--;
            } else {
                binding.dimensionBack.setVisibility(View.GONE);
            }
        });
    }


    private void show(DimensionModel model) {
        adapter.addNewDimension(model);
        binding.dimensionRecyclerView.setAdapter(adapter);
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
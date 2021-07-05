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
import com.kavramatik.kavramatik.adapter.SenseRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentSenseBinding;
import com.kavramatik.kavramatik.model.SenseModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.SenseViewModel;

import java.util.List;

public class SenseFragment extends Fragment implements ImageClickInterface {
    private FragmentSenseBinding binding;
    private TextToSpeech textToSpeech;
    private SenseRecyclerView adapter;
    private int nextOne = 1;
    private List<SenseModel> senseModels;
    private int previous;
    private SenseViewModel viewModel;

    public SenseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSenseBinding.inflate(inflater, container, false);
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
        viewModel = new ViewModelProvider(requireActivity()).get(SenseViewModel.class);
        adapter = new SenseRecyclerView(this);
        viewModel.getData();
        observeData();
    }

    private void observeData() {
        viewModel.isLoading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.senseProgress.setVisibility(View.VISIBLE);
            } else {
                binding.senseProgress.setVisibility(View.GONE);
            }
        });
        viewModel.isError.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.senseErrorText.setVisibility(View.VISIBLE);
            } else {
                binding.senseErrorText.setVisibility(View.GONE);
            }
        });
        viewModel.listMutableLiveData.observe(getViewLifecycleOwner(), model -> {
            if (model.size() >= 1) {
                binding.senseNext.setVisibility(View.VISIBLE);
                senseModels = model;
                show(senseModels.get(0));
            }
        });
        actions();
    }

    private void actions() {
        binding.senseNext.setOnClickListener(v -> {
            if (nextOne < senseModels.size()) {
                show(senseModels.get(nextOne));
                nextOne++;
                binding.senseBack.setVisibility(View.VISIBLE);
            } else {
                Scores.updateScore(requireContext(), Scores.SENSE_SCORE);
                adapter.showNew(senseModels.get(0));
                nextOne = 1;
                binding.senseBack.setVisibility(View.GONE);
            }
        });
        binding.senseBack.setOnClickListener(v -> {
            previous = nextOne - 2;
            if (previous >= 0) {
                show(senseModels.get(previous));
                nextOne--;
            } else {
                binding.senseBack.setVisibility(View.GONE);
            }
        });
    }

    private void show(SenseModel model) {
        adapter.showNew(model);
        binding.senseRecyclerView.setAdapter(adapter);
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
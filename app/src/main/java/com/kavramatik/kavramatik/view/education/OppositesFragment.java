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
import com.kavramatik.kavramatik.adapter.OppositeRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentOppositesBinding;
import com.kavramatik.kavramatik.model.OppositesModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.OppositesViewModel;

import java.util.List;

public class OppositesFragment extends Fragment implements ImageClickInterface {

    private FragmentOppositesBinding binding;
    private OppositesViewModel viewModel;
    private OppositeRecyclerView adapter;
    private int nextOne = 1;
    private int previous;
    private List<OppositesModel> oppositesModelList;
    private TextToSpeech textToSpeech;

    public OppositesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOppositesBinding.inflate(inflater, container, false);
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
        viewModel = new ViewModelProvider(requireActivity()).get(OppositesViewModel.class);
        adapter = new OppositeRecyclerView(this);
        viewModel.getData();
        observeData();
    }

    private void observeData() {
        viewModel.isError.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.oppositesErrorText.setVisibility(View.VISIBLE);
            } else {
                binding.oppositesErrorText.setVisibility(View.GONE);
            }
        });
        viewModel.isLoading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.oppositesProgress.setVisibility(View.VISIBLE);
            } else {
                binding.oppositesProgress.setVisibility(View.GONE);
            }
        });
        viewModel.oppositeLiveData.observe(getViewLifecycleOwner(), model -> {
            if (model.size() >= 1) {
                binding.oppositesNext.setVisibility(View.VISIBLE);
                oppositesModelList = model;
                show(oppositesModelList.get(0));
            }
        });
        actions();
    }

    private void actions() {
        binding.oppositesNext.setOnClickListener(v -> {
            if (nextOne < oppositesModelList.size()) {
                show(oppositesModelList.get(nextOne));
                nextOne++;
                binding.oppositesBack.setVisibility(View.VISIBLE);
            } else {
                Scores.updateScore(requireContext(), Scores.OPPOSITE_SCORE);
                show(oppositesModelList.get(0));
                nextOne = 1;
                binding.oppositesBack.setVisibility(View.GONE);
            }
        });
        binding.oppositesBack.setOnClickListener(v -> {
            previous = nextOne - 2;
            if (previous >= 0) {
                show(oppositesModelList.get(previous));
                nextOne--;
            } else {
                binding.oppositesBack.setVisibility(View.GONE);
            }
        });
    }

    private void show(OppositesModel model) {
        adapter.showNew(model);
        binding.oppositesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        GoogleTTS.shotDownTTS(this.textToSpeech);
    }

    @Override
    public void onItemClick(String text) {
        textToSpeech = new TextToSpeech(getContext(), status -> GoogleTTS.getSpeech(text, getContext(), status, this.textToSpeech));
    }
}
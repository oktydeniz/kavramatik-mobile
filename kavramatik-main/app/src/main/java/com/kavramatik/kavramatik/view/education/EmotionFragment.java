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
import com.kavramatik.kavramatik.adapter.EmotionRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentEmotionBinding;
import com.kavramatik.kavramatik.model.EmotionModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.EmotionViewModel;

import java.util.List;

public class EmotionFragment extends Fragment implements ImageClickInterface {
    private FragmentEmotionBinding binding;
    private EmotionRecyclerView adapter;
    private EmotionViewModel emotionViewModel;
    private List<EmotionModel> emotionModelList;
    private TextToSpeech textToSpeech;
    private int nextOne = 1;
    private int previous;

    public EmotionFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmotionBinding.inflate(inflater, container, false);
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
        emotionViewModel = new ViewModelProvider(requireActivity()).get(EmotionViewModel.class);
        adapter = new EmotionRecyclerView(this);
        emotionViewModel.getData();
        observeData();
    }

    private void observeData() {
        emotionViewModel.isError.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.emotionText.setVisibility(View.VISIBLE);
            } else {
                binding.emotionText.setVisibility(View.GONE);
            }
        });
        emotionViewModel.isLoading.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.emotionProgress.setVisibility(View.VISIBLE);
            } else {
                binding.emotionProgress.setVisibility(View.GONE);
            }
        });
        emotionViewModel.mutableLiveData.observe(getViewLifecycleOwner(), model -> {
            if (model.size() >= 1) {
                binding.emotionNext.setVisibility(View.VISIBLE);
                emotionModelList = model;
                show(emotionModelList.get(0));
            }
        });
        actions();
    }

    private void actions() {
        binding.emotionNext.setOnClickListener(v -> {
            if (nextOne < emotionModelList.size()) {
                show(emotionModelList.get(nextOne));
                nextOne++;
                binding.emotionBack.setVisibility(View.VISIBLE);
            } else {
                Scores.updateScore(requireContext(), Scores.EMOTION_SCORE);
                show(emotionModelList.get(0));
                nextOne = 1;
                binding.emotionBack.setVisibility(View.GONE);
            }
        });
        binding.emotionBack.setOnClickListener(v -> {
            previous = nextOne - 2;
            if (previous >= 0) {
                show(emotionModelList.get(previous));
                nextOne--;

            } else {
                binding.emotionBack.setVisibility(View.GONE);
            }
        });
    }

    private void show(EmotionModel model) {
        adapter.addNewItem(model);
        binding.emotionRecyclerView.setAdapter(adapter);
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
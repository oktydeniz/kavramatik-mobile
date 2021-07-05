package com.kavramatik.kavramatik.view.education;

import android.app.Dialog;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.speech.tts.TextToSpeech;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentColorMatchBinding;
import com.kavramatik.kavramatik.model.ColorCompModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.Base64Util;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.ColorMatchViewModel;

import java.util.List;

public class ColorMatchFragment extends Fragment {
    private FragmentColorMatchBinding binding;
    private ColorMatchViewModel colorMatchViewModel;
    private int nextModel = 0;
    private Dialog dialog;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerLoop;
    private List<ColorCompModel> colorCompModelList;
    TextToSpeech tts;

    public ColorMatchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentColorMatchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean isFirst = SharedPreferencesManager.getMatchAssistant(requireContext());
        if (isFirst) {
            AppAlertDialogs.matchDialog(requireContext());
            SharedPreferencesManager.setIsMatchAssistant(requireContext(), false);
            String text = getString(R.string.match_assistant);
            tts = new TextToSpeech(getContext(), status -> GoogleTTS.getSpeech(text, getContext(), status, this.tts));
        }
        colorMatchViewModel = new ViewModelProvider(requireActivity()).get(ColorMatchViewModel.class);
        colorMatchViewModel.getData();
        observeData();
        views();
    }

    private void views() {
        binding.imageOneMatch.setOnLongClickListener(longClickListener);
        binding.imageTwoMatch.setOnLongClickListener(longClickListener);
        binding.mainColor.setOnDragListener(dragListener);

    }

    View.OnLongClickListener longClickListener = v -> {
        ClipData clipData = ClipData.newPlainText("", "");
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(clipData, dragShadowBuilder, v, 0);
        return true;
    };
    View.OnDragListener dragListener = (v, event) -> {
        int dragEvent = event.getAction();
        switch (dragEvent) {
            case DragEvent.ACTION_DROP:
                final View view = (View) event.getLocalState();
                if (view.getTag().equals(binding.mainColor.getTag())) {
                    view.setVisibility(View.GONE);
                    if (nextModel < colorCompModelList.size() - 1) {
                        nextModel++;
                    } else {
                        nextModel = 0;
                        showFinishDialog();
                    }
                    show();
                }

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
        }
        return true;
    };

    private void observeData() {
        colorMatchViewModel.errorColors.observe(getViewLifecycleOwner(), i -> {
            if (i) {
                binding.errorTextViewColor.setVisibility(View.VISIBLE);
            } else binding.errorTextViewColor.setVisibility(View.GONE);
        });
        colorMatchViewModel.loadingColors.observe(getViewLifecycleOwner(), i -> {
            if (i) binding.machColorsProgress.setVisibility(View.VISIBLE);
            else binding.machColorsProgress.setVisibility(View.GONE);
        });
        colorMatchViewModel.mutableLiveDataColors.observe(getViewLifecycleOwner(), models -> {
            if (models.size() >= 1) {
                colorCompModelList = models;
                show();
                playLoop();
            }
        });
    }

    private void show() {
        binding.imageOneMatch.setVisibility(View.VISIBLE);
        binding.imageTwoMatch.setVisibility(View.VISIBLE);

        binding.mainColor.setImageBitmap(setImageFromUrl(colorCompModelList.get(nextModel).getMainColor()));
        binding.imageOneMatch.setImageBitmap(setImageFromUrl(colorCompModelList.get(nextModel).getColorOne()));
        binding.imageTwoMatch.setImageBitmap(setImageFromUrl(colorCompModelList.get(nextModel).getColorTwo()));

        binding.mainColor.setTag(colorCompModelList.get(nextModel).getColorTag());
        binding.imageOneMatch.setTag(colorCompModelList.get(nextModel).getOneTag());
        binding.imageTwoMatch.setTag(colorCompModelList.get(nextModel).getTwoTag());

    }

    private Bitmap setImageFromUrl(String s) {
        return Base64Util.decodeToBitmap(s);

    }

    private void showFinishDialog() {
        //update score
        Scores.updateScore(requireContext(), Scores.MATCH_SCORE);
        //finish game
        stopLoop();
        play();
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.game_over);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        Button button = dialog.findViewById(R.id.finished);
        button.setOnClickListener(v -> {
            stop();
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_colorMatchFragment_to_colorFragment);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.finish_sound);
            mediaPlayer.setOnCompletionListener(mp -> stop());

        }
        mediaPlayer.start();
    }

    private void playLoop() {
        if (mediaPlayerLoop == null) {
            mediaPlayerLoop = MediaPlayer.create(requireContext(), R.raw.back_music);
            mediaPlayerLoop.setOnCompletionListener(mp -> stop());
        }
        mediaPlayerLoop.start();
    }


    private void stop() {
        stopPlayer();
        stopLoop();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    private void stopLoop() {
        if (mediaPlayerLoop != null) {
            mediaPlayerLoop.release();
            mediaPlayerLoop = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        stop();
        GoogleTTS.shotDownTTS(tts);
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }
}
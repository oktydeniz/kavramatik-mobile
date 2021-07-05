package com.kavramatik.kavramatik.view.education;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.adapter.STTRecyclerView;
import com.kavramatik.kavramatik.databinding.FragmentSpeechBinding;
import com.kavramatik.kavramatik.model.STTPassModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.ImageClickInterface;
import com.kavramatik.kavramatik.util.Scores;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechFragment extends Fragment implements ImageClickInterface {
    private FragmentSpeechBinding binding;
    private String keeper = "";
    private Dialog dialog;
    private STTRecyclerView sttRecyclerView;
    private STTPassModel sttModels;
    private int next = 0;
    MediaPlayer mediaPlayer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpeechBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferencesManager.setLoadingFirst(requireContext(), false);
        sttModels = SpeechFragmentArgs.fromBundle(getArguments()).getModel();
        permissions();
        sttRecyclerView = new STTRecyclerView(this);
        sttRecyclerView.addItem(sttModels.getAllModels().get(next));
        binding.sttModelRecycler.setAdapter(sttRecyclerView);
    }

    private void permissions() {
        ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), result -> SharedPreferencesManager.setPermissionResult(requireContext(), result)
        );
        activityResultLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    public void onItemClick(String text) {
        keeper = "";
        keeper = text;
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.start_speaking));
        try {
            intentActivityResultLauncher.launch(speechRecognizerIntent);
        } catch (ActivityNotFoundException e) {
            AppAlertDialogs.sstDialogForInstall(requireContext());
            Toast.makeText(getContext(), getResources().getString(R.string.stt_app_not_install), Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                control(matches.get(0));
            }
        }
    });

    private void control(String s) {
        if (s.toLowerCase().equals(keeper.toLowerCase())) {
            next++;
            if (next < sttModels.getAllModels().size()) {
                sttRecyclerView.addItem(sttModels.getAllModels().get(next));
                binding.sttModelRecycler.setAdapter(sttRecyclerView);
            } else {
                next = 0;
                showFinishDialog();
            }
        }
    }

    private void showFinishDialog() {
        Scores.updateScore(requireContext(), Scores.STT_SCORE);
        play();
        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.game_over);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.getWindow().setWindowAnimations(R.style.AnimationForDialogs);
        Button button = dialog.findViewById(R.id.finished);
        button.setOnClickListener(v -> {
            stop();
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_speechFragment_to_speechListFragment);
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

    private void stop() {
        stopPlayer();
    }

    private void stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }
}
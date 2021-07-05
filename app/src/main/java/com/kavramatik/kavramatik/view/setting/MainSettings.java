package com.kavramatik.kavramatik.view.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentMainSettingsBinding;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.IntentsTTS;

public class MainSettings extends Fragment {
    private FragmentMainSettingsBinding binding;
    private TextToSpeech textToSpeech;

    public MainSettings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        actions();
    }

    private void initialize() {
        int speak = GoogleTTS.valueSpeak;
        int pitch = GoogleTTS.valuePitch;
        binding.seekBarSpeed.setProgress(speak);
        binding.seekBarPitch.setProgress(pitch);
        showVersion();
    }

    private void actions() {
        binding.openGooglePlayForTTs.setOnClickListener(v -> IntentsTTS.installTTS(requireContext()));
        binding.openSettingsMenu.setOnClickListener(v -> IntentsTTS.openTTSSettings(requireContext()));
        binding.speakButton.setOnClickListener(v -> tryNewValues());
        binding.openTTSLanguages.setOnClickListener(v -> IntentsTTS.openTextLang(requireContext()));
        binding.openForRateUs.setOnClickListener(v -> IntentsTTS.rateUS(requireContext()));
    }

    private void showVersion() {
        String version = requireContext().getResources().getString(R.string.app_version);
        try {
            PackageInfo pInfo = requireContext().getPackageManager().getPackageInfo(requireContext().getPackageName(), 0);
            version += pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version += "0.0";
        }
        binding.appVersionText.setText(version);
    }

    private void tryNewValues() {
        GoogleTTS.shotDownTTS(textToSpeech);
        String text = binding.textView9.getText().toString();
        GoogleTTS.valuePitch = binding.seekBarPitch.getProgress();
        GoogleTTS.valueSpeak = binding.seekBarSpeed.getProgress();
        System.out.println(GoogleTTS.valuePitch + " " + GoogleTTS.valueSpeak);
        GoogleTTS.pitchTTS = (float) binding.seekBarPitch.getProgress() / 50;
        GoogleTTS.speedTTS = (float) binding.seekBarSpeed.getProgress() / 50;
        textToSpeech = new TextToSpeech(getContext(), status -> GoogleTTS.getSpeech(text, getContext(), status, this.textToSpeech));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        GoogleTTS.shotDownTTS(textToSpeech);
    }
}
package com.kavramatik.kavramatik.view.education;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentSpeechListBinding;
import com.kavramatik.kavramatik.model.STTModel;
import com.kavramatik.kavramatik.model.STTPassModel;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.GoogleTTS;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.viewModel.ColorViewModel;
import com.kavramatik.kavramatik.viewModel.DirectionViewModel;
import com.kavramatik.kavramatik.viewModel.EmotionViewModel;
import com.kavramatik.kavramatik.viewModel.NumberViewModel;
import com.kavramatik.kavramatik.viewModel.ShapeViewModel;

import java.util.ArrayList;

public class SpeechListFragment extends Fragment {

    private FragmentSpeechListBinding binding;
    private ColorViewModel colorViewModel;
    ArrayList<STTModel> sttModel;
    STTPassModel sttPassModel;
    TextToSpeech textToSpeech;
    boolean isFirs;
    AppAlertDialogs appAlertDialogs;
    private ShapeViewModel shapeViewModel;
    private NumberViewModel numberViewModel;
    private EmotionViewModel emotionViewModel;
    private DirectionViewModel directionViewModel;

    public SpeechListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSpeechListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFirs = SharedPreferencesManager.getLoadingFirst(requireContext());
        if (isFirs) {
            appAlertDialogs = new AppAlertDialogs(requireActivity());
            appAlertDialogs.startLoadingDialog();
        }

        boolean resultPermission = SharedPreferencesManager.getPermissionResult(requireContext());
        if (!resultPermission) {
            AppAlertDialogs.permissionDialog(requireContext());
            String permissionText = getResources().getString(R.string.permission_tex);
            textToSpeech = new TextToSpeech(requireContext(), status -> GoogleTTS.getSpeech(permissionText, requireContext(), status, textToSpeech));
        }

        sttModel = new ArrayList<>();
        getAllData();
        actions();
    }

    private void actions() {
        binding.colorsSTT.setOnClickListener(v -> {
            sttModel.clear();
            colorViewModel.colorModel.observe(getViewLifecycleOwner(), model -> {
                String question = getResources().getString(R.string.stt_question);
                for (int i = 0; i < model.size(); i++) {
                    STTModel stt = new STTModel(model.get(i).getColorText(), model.get(i).getColorImage(), question);
                    sttModel.add(stt);
                }
            });
            sttPassModel = new STTPassModel(sttModel);
            NavDirections directions = SpeechListFragmentDirections.actionSpeechListFragmentToSpeechFragment(sttPassModel);
            Navigation.findNavController(v).navigate(directions);
        });
        binding.shapesSTT.setOnClickListener(v -> {
            sttModel.clear();
            shapeViewModel.shapeModel.observe(getViewLifecycleOwner(), model -> {
                String question = getResources().getString(R.string.stt_question);
                for (int i = 0; i < model.size(); i++) {
                    STTModel stt = new STTModel(model.get(i).getShapeText(), model.get(i).getShapeImage(), question);
                    sttModel.add(stt);
                }
            });
            sttPassModel = new STTPassModel(sttModel);
            NavDirections directions = SpeechListFragmentDirections.actionSpeechListFragmentToSpeechFragment(sttPassModel);
            Navigation.findNavController(v).navigate(directions);
        });
        binding.directionSTT.setOnClickListener(v -> {
            sttModel.clear();
            directionViewModel.directionModel.observe(getViewLifecycleOwner(), model -> {
                String question = getResources().getString(R.string.stt_question);
                for (int i = 0; i < model.size(); i++) {
                    STTModel stt = new STTModel(model.get(i).getDirectionText(), model.get(i).getDirectionImage(), question);
                    sttModel.add(stt);
                }
            });
            sttPassModel = new STTPassModel(sttModel);
            NavDirections directions = SpeechListFragmentDirections.actionSpeechListFragmentToSpeechFragment(sttPassModel);
            Navigation.findNavController(v).navigate(directions);
        });
        binding.emotionSTT.setOnClickListener(v -> {
            sttModel.clear();
            emotionViewModel.mutableLiveData.observe(getViewLifecycleOwner(), model -> {
                String question = getResources().getString(R.string.stt_question);
                for (int i = 0; i < model.size(); i++) {
                    STTModel stt = new STTModel(model.get(i).getEmotionText(), model.get(i).getEmotionImage(), question);
                    sttModel.add(stt);
                }
            });
            sttPassModel = new STTPassModel(sttModel);
            NavDirections directions = SpeechListFragmentDirections.actionSpeechListFragmentToSpeechFragment(sttPassModel);
            Navigation.findNavController(v).navigate(directions);
        });
        binding.numberSTT.setOnClickListener(v -> {
            sttModel.clear();
            numberViewModel.numberModel.observe(getViewLifecycleOwner(), model -> {
                String question = getResources().getString(R.string.stt_question);
                for (int i = 0; i < model.size(); i++) {
                    STTModel stt = new STTModel(model.get(i).getNumberText(), model.get(i).getNumberImage(), question);
                    sttModel.add(stt);
                   /* STTModel stt2 = new STTModel(model.get(i).getNumberImageText(), model.get(i).getNumberQuantityImage(), question);
                    sttModel.add(stt2);*/
                }
            });
            sttPassModel = new STTPassModel(sttModel);
            NavDirections directions = SpeechListFragmentDirections.actionSpeechListFragmentToSpeechFragment(sttPassModel);
            Navigation.findNavController(v).navigate(directions);
        });

    }

    private void getAllData() {
        colorViewModel = new ViewModelProvider(requireActivity()).get(ColorViewModel.class);
        colorViewModel.getData();
        shapeViewModel = new ViewModelProvider(requireActivity()).get(ShapeViewModel.class);
        shapeViewModel.getData();
        numberViewModel = new ViewModelProvider(requireActivity()).get(NumberViewModel.class);
        numberViewModel.getData();
        emotionViewModel = new ViewModelProvider(requireActivity()).get(EmotionViewModel.class);
        emotionViewModel.getData();
        directionViewModel = new ViewModelProvider(requireActivity()).get(DirectionViewModel.class);
        directionViewModel.getData();
        if (isFirs) {
            Handler handler = new Handler();
            Runnable runnable = () -> appAlertDialogs.dismissLoading();
            handler.postDelayed(runnable, 5000);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.kavramatik.kavramatik.view.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentRegisterBinding;
import com.kavramatik.kavramatik.model.ResponseModel;
import com.kavramatik.kavramatik.network.ManagerAll;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.util.ValidateClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AppAlertDialogs appAlertDialogs;

    public RegisterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.singUpButton.setOnClickListener(v -> validateRules());
        appAlertDialogs = new AppAlertDialogs(getActivity());
    }

    private void validateRules() {
        if (ValidateClass.editTextEmailIsNull(binding.registerEmailEditText) &&
                ValidateClass.editTextPassword(binding.registerPasswordEditText) &&
                ValidateClass.editTextPassword(binding.registerPasswordValidateEditText) &&
                ValidateClass.editTextTextValue(binding.loginUserNameEditText) &&
                ValidateClass.machPassword(binding.registerPasswordEditText, binding.registerPasswordValidateEditText)) {
            register();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }

    private void register() {
        appAlertDialogs.startLoadingDialog();
        String name = binding.loginUserNameEditText.getText().toString();
        String email = binding.registerEmailEditText.getText().toString();
        String password = binding.registerPasswordValidateEditText.getText().toString();
        Call<ResponseModel> register = ManagerAll.getInstance().register(email, name, password);
        register.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse()) {
                            appAlertDialogs.dismissLoading();
                            Toast.makeText(getContext(), getResources().getString(R.string.welcome_messages, name), Toast.LENGTH_LONG).show();
                            int score = SharedPreferencesManager.getScore(requireContext());
                            int lastScore = response.body().getScore() + score;
                            SharedPreferencesManager.setScore(requireContext(), lastScore);
                            SharedPreferencesManager.setUserID(requireContext(), response.body().getUserId());
                            SharedPreferencesManager.setUserEmail(requireContext(), response.body().getUserEmail());
                            SharedPreferencesManager.setUserName(requireContext(), response.body().getUserName());
                            NavController controller = NavHostFragment.findNavController(RegisterFragment.this);
                            controller.navigate(R.id.action_registerFragment_to_choiceFragment);
                        } else if (!response.body().getResponse()) {
                            Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                            appAlertDialogs.dismissLoading();
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                appAlertDialogs.dismissLoading();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
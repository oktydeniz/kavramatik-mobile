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
import com.kavramatik.kavramatik.databinding.FragmentLoginBinding;
import com.kavramatik.kavramatik.model.ResponseModel;
import com.kavramatik.kavramatik.network.ManagerAll;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;
import com.kavramatik.kavramatik.util.ValidateClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AppAlertDialogs appAlertDialogs;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        actions();
    }

    private void actions() {
        appAlertDialogs = new AppAlertDialogs(getActivity());
        binding.singInButton.setOnClickListener(v -> validateRules());
        binding.singUpButton.setOnClickListener(v -> {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_loginFragment_to_registerFragment);
        });

        binding.forgotPassword.setOnClickListener(v -> sendMailScreen());
    }

    private void validateRules() {
        if (ValidateClass.editTextEmailIsNull(binding.loginEmailEditText) &&
                ValidateClass.editTextPassword(binding.loginPasswordEditText)) {
            login();
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
        }
    }

    private void login() {
        appAlertDialogs.startLoadingDialog();
        String email = binding.loginEmailEditText.getText().toString();
        String password = binding.loginPasswordEditText.getText().toString();
        Call<ResponseModel> call = ManagerAll.getInstance().login(email, password);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse()) {
                            appAlertDialogs.dismissLoading();
                            Toast.makeText(getContext(), getResources().getString(R.string.welcome_messages, response.body().getUserName()), Toast.LENGTH_LONG).show();
                            SharedPreferencesManager.setUserID(requireContext(), response.body().getUserId());
                            SharedPreferencesManager.setUserEmail(requireContext(), response.body().getUserEmail());
                            SharedPreferencesManager.setUserName(requireContext(), response.body().getUserName());
                            int score = SharedPreferencesManager.getScore(requireContext());
                            int lastScore = response.body().getScore() + score;
                            SharedPreferencesManager.setScore(requireContext(), lastScore);
                            NavController controller = NavHostFragment.findNavController(LoginFragment.this);
                            controller.navigate(R.id.action_loginFragment_to_choiceFragment);
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

    private void sendMailScreen() {
        NavController controller = NavHostFragment.findNavController(this);
        controller.navigate(R.id.action_loginFragment_to_sendMailFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
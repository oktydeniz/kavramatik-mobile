package com.kavramatik.kavramatik.view.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kavramatik.kavramatik.R;
import com.kavramatik.kavramatik.databinding.FragmentProfileBinding;
import com.kavramatik.kavramatik.model.ResponseModel;
import com.kavramatik.kavramatik.network.ManagerAll;
import com.kavramatik.kavramatik.util.AppAlertDialogs;
import com.kavramatik.kavramatik.util.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AppAlertDialogs appAlertDialogs;
    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeValues();
        actions();
    }

    private void actions() {
        binding.goToLoginFragment.setOnClickListener(v -> {
            NavController controller = NavHostFragment.findNavController(this);
            controller.navigate(R.id.action_profileFragment_to_loginFragment);
        });
        binding.singOutTextView.setOnClickListener(v -> singOut());
        binding.settingInButton.setOnClickListener(v -> {
            NavDirections directions = ProfileFragmentDirections.actionProfileFragmentToMainSettings();
            Navigation.findNavController(v).navigate(directions);
        });
    }

    private void initializeValues() {
        appAlertDialogs = new AppAlertDialogs(getActivity());
        getSharedPreferenceValues();

        //Set TextView status if true don't show and if not show the view
        int result = SharedPreferencesManager.getUserId(getContext());
        if (result == SharedPreferencesManager.defaultID) {
            binding.goToLoginFragment.setVisibility(View.VISIBLE);
            binding.singOutTextView.setVisibility(View.GONE);
        } else {
            binding.goToLoginFragment.setVisibility(View.GONE);
            binding.singOutTextView.setVisibility(View.VISIBLE);
        }
    }

    private void singOut() {
        setSharedPreferenceValues(SharedPreferencesManager.defaultID,
                SharedPreferencesManager.nullValue,
                SharedPreferencesManager.nullValue,
                SharedPreferencesManager.defaultScore);

        NavController controller = NavHostFragment.findNavController(ProfileFragment.this);
        controller.navigate(R.id.action_profileFragment_to_choiceFragment);
    }

    private void getSharedPreferenceValues() {
        int score = SharedPreferencesManager.getScore(getContext());
        String name = SharedPreferencesManager.getUserName(getContext());
        String email = SharedPreferencesManager.getUserEmail(getContext());

        binding.profileUserEmail.setText(email);
        binding.profileUserName.setText(name);
        binding.profileUserScore.setText(String.valueOf(score));
    }

    private void setSharedPreferenceValues(int id, String mail, String name, int score) {
        SharedPreferencesManager.setUserID(getContext(), id);
        SharedPreferencesManager.setUserEmail(getContext(), mail);
        SharedPreferencesManager.setUserName(getContext(), name);
        SharedPreferencesManager.setScore(getContext(), score);
    }

    private void updateProfile() {
        String mail = SharedPreferencesManager.getUserEmail(getContext());
        if (!mail.equals(SharedPreferencesManager.nullValue)) {
            appAlertDialogs.startLoadingDialog();
            Call<ResponseModel> call = ManagerAll.getInstance().showProfile(mail);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse()) {
                                setSharedPreferenceValues(response.body().getUserId(),
                                        response.body().getUserEmail(),
                                        response.body().getUserName(),
                                        response.body().getScore());
                                getSharedPreferenceValues();

                                appAlertDialogs.dismissLoading();
                            } else if (!response.body().getResponse()) {
                                appAlertDialogs.dismissLoading();
                                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                    appAlertDialogs.dismissLoading();
                    Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.first_sing_in), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore() {
        String mail = SharedPreferencesManager.getUserEmail(getContext());
        int userScore = SharedPreferencesManager.getScore(getContext());

        if (!mail.equals(SharedPreferencesManager.nullValue)) {
            Call<ResponseModel> call = ManagerAll.getInstance().setNewScore(mail, userScore);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getResponse()) {

                                SharedPreferencesManager.setScore(requireContext(), response.body().getScore());
                                setSharedPreferenceValues(response.body().getUserId(),
                                        response.body().getUserEmail(),
                                        response.body().getUserName(),
                                        response.body().getScore());
                                binding.profileUserScore.setText("");

                                getSharedPreferenceValues();
                            } else if (!response.body().getResponse()) {
                                Toast.makeText(getContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.first_sing_in), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.updateProfile) {
            updateScore();
        }
        return super.onOptionsItemSelected(item);
    }


}
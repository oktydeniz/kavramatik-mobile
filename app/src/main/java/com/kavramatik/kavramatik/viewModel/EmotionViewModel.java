package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.EmotionModel;
import com.kavramatik.kavramatik.service.EducationAPI;
import com.kavramatik.kavramatik.service.EducationAPIService;
import com.kavramatik.kavramatik.util.NetworkState;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class EmotionViewModel extends BaseViewModel {

    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<EmotionModel>> mutableLiveData;
    public MutableLiveData<Boolean> isError;
    public MutableLiveData<Boolean> isLoading;
    EducationAPI api;
    Application application;
    EducationDatabase database;
    private final Retrofit retrofit;
    private final CompositeDisposable compositeDisposable;

    public EmotionViewModel(Application application) {
        super(application);
        isLoading = new MutableLiveData<>();
        isError = new MutableLiveData<>();
        this.application = application;
        database = EducationDatabase.getInstance(application.getApplicationContext());
        mutableLiveData = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        retrofit = EducationAPIService.getInstance();
    }

    public void getData() {
        if (NetworkState.getInstance(application.getApplicationContext()).isOnline()) {
            getDataAPI();
        } else {
            getDataSQL();
        }
    }

    private void getDataAPI() {
        isLoading.setValue(true);
        api = retrofit.create(EducationAPI.class);
        compositeDisposable.add(api.getEmotions().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<EmotionModel>>() {
            @Override
            public void onSuccess(@NotNull List<EmotionModel> emotionModels) {
                isLoading.setValue(false);
                isError.setValue(false);
                mutableLiveData.setValue(emotionModels);
                insertData(emotionModels);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                isLoading.setValue(false);
                isError.setValue(true);
            }
        }));

    }

    private void getDataSQL() {
        try {
            List<EmotionModel> emotionModels = database.educationDao().getAllEmotion();
            if (emotionModels.size() >= 1) {
                isLoading.setValue(false);
                isError.setValue(false);
                mutableLiveData.setValue(emotionModels);
            } else {
                isLoading.setValue(false);
                isError.setValue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(List<EmotionModel> emotionModels) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            database.educationDao().deleteEmotion();
            database.educationDao().insertAllEmotion(emotionModels);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

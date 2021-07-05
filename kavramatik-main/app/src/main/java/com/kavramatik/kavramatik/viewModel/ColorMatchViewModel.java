package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.ColorCompModel;
import com.kavramatik.kavramatik.service.EducationAPI;
import com.kavramatik.kavramatik.service.EducationAPIService;
import com.kavramatik.kavramatik.util.NetworkState;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ColorMatchViewModel extends BaseViewModel implements Serializable {
    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<ColorCompModel>> mutableLiveDataColors;
    public MutableLiveData<Boolean> loadingColors;
    public MutableLiveData<Boolean> errorColors;
    private final CompositeDisposable compositeDisposable;
    EducationDatabase database;
    private final Retrofit retrofit;
    EducationAPI api;
    Application application;

    public ColorMatchViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.application = application;
        database = EducationDatabase.getInstance(application);
        retrofit = EducationAPIService.getInstance();
        compositeDisposable = new CompositeDisposable();
        errorColors = new MutableLiveData<>();
        loadingColors = new MutableLiveData<>();
        mutableLiveDataColors = new MutableLiveData<>();
    }

    public void getData() {
        if (NetworkState.getInstance(application.getApplicationContext()).isOnline()) {
            getDataAPI();
        } else {
            getDataSQL();
        }
    }

    private void getDataAPI() {
        loadingColors.setValue(true);
        api = retrofit.create(EducationAPI.class);
        compositeDisposable.add(api.getCompColors().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<ColorCompModel>>() {
            @Override
            public void onSuccess(@NotNull List<ColorCompModel> models) {
                loadingColors.setValue(false);
                errorColors.setValue(false);
                mutableLiveDataColors.setValue(models);
                insertData(models);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                loadingColors.setValue(false);
                errorColors.setValue(true);
            }
        }));
    }

    private void insertData(List<ColorCompModel> mutableLiveDataColors) {
        database.educationDao().deleteCompColors();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> database.educationDao().insertCompColor(mutableLiveDataColors));
    }

    private void getDataSQL() {
        try {
            List<ColorCompModel> colorCompModels = database.educationDao().getCompColors();
            if (colorCompModels.size() >= 1) {
                loadingColors.setValue(false);
                errorColors.setValue(false);
                mutableLiveDataColors.setValue(colorCompModels);
            } else {
                loadingColors.setValue(false);
                errorColors.setValue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorColors.setValue(true);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

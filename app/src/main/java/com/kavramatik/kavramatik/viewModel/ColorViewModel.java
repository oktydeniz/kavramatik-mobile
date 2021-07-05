package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.ColorModel;
import com.kavramatik.kavramatik.service.EducationAPI;
import com.kavramatik.kavramatik.service.EducationAPIService;
import com.kavramatik.kavramatik.util.NetworkState;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ColorViewModel extends BaseViewModel implements Serializable {
    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<ColorModel>> colorModel;
    public MutableLiveData<Boolean> loading;
    public MutableLiveData<Boolean> error;
    private final CompositeDisposable compositeDisposable;
    EducationDatabase database;
    private final Retrofit retrofit;
    EducationAPI api;
    Application application;

    public ColorViewModel(Application application) {
        super(application);
        database = EducationDatabase.getInstance(application);
        this.colorModel = new MutableLiveData<>();
        this.application = application;
        this.loading = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        retrofit = EducationAPIService.getInstance();
        error = new MutableLiveData<>();
    }

    public void getData() {
        if (NetworkState.getInstance(application.getApplicationContext()).isOnline()) {
            getDataAPI();
        } else {
            getDataSQL();
        }
    }

    private void getDataAPI() {
        loading.setValue(true);
        api = retrofit.create(EducationAPI.class);
        compositeDisposable.add(api.getColors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ColorModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ColorModel> colorModels) {
                        loading.setValue(false);
                        error.setValue(false);
                        colorModel.setValue(colorModels);
                        insertData(colorModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loading.setValue(false);
                        error.setValue(true);
                    }
                })
        );
    }

    private void insertData(List<ColorModel> models) {
        database.educationDao().deleteColors();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> database.educationDao().insertAllColor(models));
    }

    private void getDataSQL() {
        try {
            List<ColorModel> colorModels = database.educationDao().getAllColor();
            if (colorModels.size() >= 1) {
                loading.setValue(false);
                error.setValue(false);
                colorModel.setValue(colorModels);
            } else {
                loading.setValue(false);
                error.setValue(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

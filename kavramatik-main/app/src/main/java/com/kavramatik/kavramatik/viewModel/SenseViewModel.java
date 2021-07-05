package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.SenseModel;
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

public class SenseViewModel extends BaseViewModel {

    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<SenseModel>> listMutableLiveData;
    public MutableLiveData<Boolean> isLoading;
    public MutableLiveData<Boolean> isError;
    EducationAPI api;
    EducationDatabase database;
    Application application;
    private final CompositeDisposable compositeDisposable;
    private final Retrofit retrofit;

    public SenseViewModel(Application application) {
        super(application);
        this.application = application;
        database = EducationDatabase.getInstance(application.getApplicationContext());
        listMutableLiveData = new MutableLiveData<>();
        isError = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        retrofit = EducationAPIService.getInstance();
        compositeDisposable = new CompositeDisposable();
    }

    public void getData() {
        if (NetworkState.getInstance(application.getApplicationContext()).isOnline()) {
            getDataAPI();
        } else {
            getFromSQL();
        }
    }

    private void getDataAPI() {
        api = retrofit.create(EducationAPI.class);
        isLoading.setValue(true);
        compositeDisposable.add(api.getSenses().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<SenseModel>>() {

            @Override
            public void onSuccess(@NotNull List<SenseModel> senseModels) {
                isError.setValue(false);
                isLoading.setValue(false);
                listMutableLiveData.setValue(senseModels);
                insertAll(senseModels);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                isError.setValue(true);
                isLoading.setValue(false);
            }
        }));
    }

    private void getFromSQL() {
        try {
            List<SenseModel> senseModels = database.educationDao().getAllSense();
            if (senseModels.size() >= 1) {
                isLoading.setValue(false);
                isError.setValue(false);
                listMutableLiveData.setValue(senseModels);
            } else {
                isLoading.setValue(false);
                isError.setValue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertAll(List<SenseModel> senseModels) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            database.educationDao().deleteSense();
            database.educationDao().insertAllSense(senseModels);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}

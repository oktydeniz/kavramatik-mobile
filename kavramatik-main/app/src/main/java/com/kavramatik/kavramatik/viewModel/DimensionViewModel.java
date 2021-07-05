package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.DimensionModel;
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

public class DimensionViewModel extends BaseViewModel implements Serializable {
    private final Retrofit retrofit;
    Application application;
    EducationAPI api;
    @TypeConverters(DataConverter.class)
    public final MutableLiveData<List<DimensionModel>> listMutableLiveData;
    public MutableLiveData<Boolean> isError;
    public MutableLiveData<Boolean> isLoading;
    private final CompositeDisposable disposable;
    EducationDatabase database;

    public DimensionViewModel(Application application) {
        super(application);
        this.application = application;
        retrofit = EducationAPIService.getInstance();
        disposable = new CompositeDisposable();
        listMutableLiveData = new MutableLiveData<>();
        isError = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        database = EducationDatabase.getInstance(application.getApplicationContext());
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
        disposable.add(api.getDimensions().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<DimensionModel>>() {
                    @Override
                    public void onSuccess(@NotNull List<DimensionModel> dimensionModels) {
                        isError.setValue(false);
                        isLoading.setValue(false);
                        listMutableLiveData.setValue(dimensionModels);
                        insertSQL(dimensionModels);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        isError.setValue(true);
                        isLoading.setValue(false);
                    }
                }));
    }

    private void getDataSQL() {
        try {
            List<DimensionModel> dimensionModels = database.educationDao().getAllDimension();
            if (dimensionModels.size() >= 1) {
                isError.setValue(false);
                isLoading.setValue(false);
                listMutableLiveData.setValue(dimensionModels);
            } else {
                isError.setValue(true);
                isLoading.setValue(false);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void insertSQL(List<DimensionModel> dimensionModels) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            database.educationDao().deleteDimension();
            database.educationDao().insertAllDimension(dimensionModels);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

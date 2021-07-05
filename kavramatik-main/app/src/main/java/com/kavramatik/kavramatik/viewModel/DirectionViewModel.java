package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.DirectionModel;
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

public class DirectionViewModel extends BaseViewModel implements Serializable {

    @TypeConverters(DataConverter.class)
    public final MutableLiveData<List<DirectionModel>> directionModel;
    public final MutableLiveData<Boolean> loading;
    private final Application application;
    private final CompositeDisposable compositeDisposable;
    EducationAPI api;
    EducationDatabase database;
    private final Retrofit retrofit;
    public final MutableLiveData<Boolean> isFailed;

    public DirectionViewModel(Application application) {
        super(application);
        this.application = application;
        database = EducationDatabase.getInstance(application.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
        loading = new MutableLiveData<>();
        directionModel = new MutableLiveData<>();
        isFailed = new MutableLiveData<>();
        retrofit = EducationAPIService.getInstance();
    }

    public void getData() {
        if (NetworkState.getInstance(application.getApplicationContext()).isOnline()) {
            getDataAPI();
        } else {
            getFromSQL();
        }
    }


    private void getDataAPI() {
        loading.setValue(true);
        api = retrofit.create(EducationAPI.class);
        compositeDisposable.add(api.getDirections().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<DirectionModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<DirectionModel> directionModels) {
                        loading.setValue(false);
                        isFailed.setValue(false);
                        directionModel.setValue(directionModels);
                        insertData(directionModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loading.setValue(false);
                        isFailed.setValue(true);
                    }
                }));
    }

    private void insertData(List<DirectionModel> directionModels) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            database.educationDao().deleteDirection();
            database.educationDao().insertAllDirection(directionModels);
        });
    }

    private void getFromSQL() {
        try {
            List<DirectionModel> directionModels = database.educationDao().getAllDirection();
            if (directionModels.size() >= 1) {
                loading.setValue(false);
                isFailed.setValue(false);
                directionModel.setValue(directionModels);
            } else {
                loading.setValue(false);
                isFailed.setValue(true);
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

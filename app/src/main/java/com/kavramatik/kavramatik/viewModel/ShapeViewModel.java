package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.ShapeModel;
import com.kavramatik.kavramatik.service.EducationAPI;
import com.kavramatik.kavramatik.service.EducationAPIService;
import com.kavramatik.kavramatik.util.NetworkState;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ShapeViewModel extends BaseViewModel {

    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<ShapeModel>> shapeModel;
    public MutableLiveData<Boolean> loading;
    private final CompositeDisposable compositeDisposable;
    public MutableLiveData<Boolean> error;
    EducationDatabase database;
    Application application;
    private final Retrofit retrofit;
    EducationAPI api;

    public ShapeViewModel(Application application) {
        super(application);
        this.application = application;
        database = EducationDatabase.getInstance(application.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
        loading = new MutableLiveData<>();
        error = new MutableLiveData<>();
        shapeModel = new MutableLiveData<>();
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
        loading.setValue(true);
        api = retrofit.create(EducationAPI.class);
        compositeDisposable.add(api.getShapes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ShapeModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ShapeModel> shapeModels) {
                        loading.setValue(false);
                        error.setValue(false);
                        insertAll(shapeModels);
                        shapeModel.setValue(shapeModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        loading.setValue(false);
                        error.setValue(true);

                    }
                })
        );
    }

    private void insertAll(List<ShapeModel> shapeModels) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            database.educationDao().deleteShape();
            database.educationDao().insertAllShape(shapeModels);
        });
    }

    private void getDataSQL() {
        try {
            List<ShapeModel> shapeModels = database.educationDao().getAllShape();
            if (shapeModels.size() >= 1) {
                error.setValue(false);
                loading.setValue(false);
                shapeModel.setValue(shapeModels);
            } else {
                error.setValue(true);
                loading.setValue(false);
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

package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.NumberModel;
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

public class NumberViewModel extends BaseViewModel {
    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<NumberModel>> numberModel;
    public MutableLiveData<Boolean> isError;
    public MutableLiveData<Boolean> loading;
    private final CompositeDisposable compositeDisposable;
    EducationAPI api;
    EducationDatabase database;
    Application application;
    private final Retrofit retrofit;

    public NumberViewModel(Application application) {
        super(application);
        this.application = application;
        numberModel = new MutableLiveData<>();
        database = EducationDatabase.getInstance(application.getApplicationContext());
        isError = new MutableLiveData<>();
        loading = new MutableLiveData<>();
        retrofit = EducationAPIService.getInstance();
        compositeDisposable = new CompositeDisposable();
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
        compositeDisposable.add(api.getNumbers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<NumberModel>>() {

                    @Override
                    public void onSuccess(@NotNull List<NumberModel> numberModels) {
                        loading.setValue(false);
                        isError.setValue(false);
                        numberModel.setValue(numberModels);
                        insertAll(numberModels);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        loading.setValue(false);
                        isError.setValue(true);
                    }
                }));
    }

    private void insertAll(List<NumberModel> numberModels) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            database.educationDao().deleteNumber();
            database.educationDao().insertAllNumber(numberModels);
        });
    }

    private void getDataSQL() {
        try {
            List<NumberModel> numberModels = database.educationDao().getAllNumber();
            if (numberModels.size() >= 1) {
                isError.setValue(false);
                loading.setValue(false);
                numberModel.setValue(numberModels);
            } else {
                isError.setValue(true);
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

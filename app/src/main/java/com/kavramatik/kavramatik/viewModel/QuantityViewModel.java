package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.QuantityModel;
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

public class QuantityViewModel extends BaseViewModel {

    @TypeConverters(DataConverter.class)
    public final MutableLiveData<List<QuantityModel>> listMutableLiveData;
    public MutableLiveData<Boolean> isError;
    public MutableLiveData<Boolean> isLoading;
    EducationAPI api;
    Application application;
    EducationDatabase educationDatabase;
    private final CompositeDisposable compositeDisposable;
    private final Retrofit retrofit;

    public QuantityViewModel(Application application) {
        super(application);
        this.application = application;
        educationDatabase = EducationDatabase.getInstance(application.getApplicationContext());
        isLoading = new MutableLiveData<>();
        isError = new MutableLiveData<>();
        listMutableLiveData = new MutableLiveData<>();
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
        compositeDisposable.add(api.getQuantities().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<QuantityModel>>() {

            @Override
            public void onSuccess(@NotNull List<QuantityModel> quantityModels) {
                isError.setValue(false);
                isLoading.setValue(false);
                listMutableLiveData.setValue(quantityModels);
                insertAll(quantityModels);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                isError.setValue(true);
                isLoading.setValue(false);
            }
        }));
    }

    private void insertAll(List<QuantityModel> quantityModels) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            educationDatabase.educationDao().deleteQuantity();
            educationDatabase.educationDao().insertAllQuantity(quantityModels);
        });
    }

    private void getDataSQL() {
        try {
            List<QuantityModel> quantityModelList = educationDatabase.educationDao().getAllQuantity();
            if (quantityModelList.size() >= 1) {
                isError.setValue(false);
                isLoading.setValue(false);
                listMutableLiveData.setValue(quantityModelList);
            } else {
                isError.setValue(true);
                isLoading.setValue(false);
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

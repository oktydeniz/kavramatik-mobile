package com.kavramatik.kavramatik.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.TypeConverters;

import com.kavramatik.kavramatik.database.DataConverter;
import com.kavramatik.kavramatik.database.EducationDatabase;
import com.kavramatik.kavramatik.model.OppositesModel;
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

public class OppositesViewModel extends BaseViewModel {
    @TypeConverters(DataConverter.class)
    public MutableLiveData<List<OppositesModel>> oppositeLiveData;
    public MutableLiveData<Boolean> isError;
    public MutableLiveData<Boolean> isLoading;
    EducationAPI api;
    Application application;
    EducationDatabase database;
    private final Retrofit retrofit;
    public CompositeDisposable compositeDisposable;

    public OppositesViewModel(Application application) {
        super(application);
        this.application = application;
        retrofit = EducationAPIService.getInstance();
        compositeDisposable = new CompositeDisposable();
        isLoading = new MutableLiveData<>();
        isError = new MutableLiveData<>();
        database = EducationDatabase.getInstance(application.getApplicationContext());
        oppositeLiveData = new MutableLiveData<>();
    }

    public void getData() {
        if (NetworkState.getInstance(application.getApplicationContext()).isOnline()) {
            getDataAPI();
        } else {
            getDataSQL();
        }
    }

    private void getDataAPI() {
        api = retrofit.create(EducationAPI.class);
        isLoading.setValue(true);
        compositeDisposable.add(api.getOpposites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<OppositesModel>>() {
            @Override
            public void onSuccess(@NotNull List<OppositesModel> oppositesModels) {
                isError.setValue(false);
                isLoading.setValue(false);
                oppositeLiveData.setValue(oppositesModels);
                insertAll(oppositesModels);
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
            List<OppositesModel> oppositesModels = database.educationDao().getAllOpposites();
            if (oppositesModels.size() >= 1) {
                isLoading.setValue(false);
                isError.setValue(false);
                oppositeLiveData.setValue(oppositesModels);
            } else {
                isLoading.setValue(false);
                isError.setValue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertAll(List<OppositesModel> oppositesModels) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            database.educationDao().deleteOpposites();
            database.educationDao().insertAllOpposites(oppositesModels);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}


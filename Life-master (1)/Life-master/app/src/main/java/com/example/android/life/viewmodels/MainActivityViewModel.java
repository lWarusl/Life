package com.example.android.life.viewmodels;

import android.app.Application;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.life.adapters.LifeAdapter;
import com.example.android.life.pojos.LifeBoard;
import com.example.android.life.pojos.PersonField;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<PersonField>> mPeople = new MutableLiveData<>();
    private MutableLiveData<String> mButtonText = new MutableLiveData<>();
    private MutableLiveData<String> mTimeText = new MutableLiveData<>();
    private MutableLiveData<String> mCountLives = new MutableLiveData<>();
    private MutableLiveData<String> mCountX = new MutableLiveData<>();
    private MutableLiveData<String> mCountY = new MutableLiveData<>();


    public LiveData<List<PersonField>> people = mPeople;
    public LiveData<String> buttonText = mButtonText;
    public LiveData<String> timeText = mTimeText;
    public LiveData<String> countLives = mCountLives;
    public LiveData<String> countX = mCountX;
    public LiveData<String> countY = mCountY;

    private long mStartTime = 0;
    private int mDelay =1000;
    private LifeBoard mLifeBoard;

    private Handler mTimerHandler = new Handler();
    private Runnable mTimerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - mStartTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            mTimeText.postValue(String.format("%d:%02d", minutes, seconds));

            mLifeBoard.move();
            mPeople.postValue(mLifeBoard.toList());
            mLifeBoard.reproduce();
            mPeople.postValue(mLifeBoard.toList());
            mLifeBoard.kill();
            mPeople.postValue(mLifeBoard.toList());
            mCountLives.postValue(String.format("Lives: %d", mLifeBoard.countLives()));
            mCountX.postValue(String.format("X: %d", mLifeBoard.countX()));
            mCountY.postValue(String.format("Y: %d", mLifeBoard.countY()));
            if(mLifeBoard.checkIfEndOfLife()){
                mTimerHandler.removeCallbacks(mTimerRunnable);
                mButtonText.postValue("start");
            }else {
                mTimerHandler.postDelayed(this, mDelay);
            }

        }
    };


    private MainActivityViewModel(@NonNull Application application) {
        super(application);
        mButtonText.postValue("start");
        mTimeText.postValue("00:00");
    }

    public void startLife(View view){
        int noColumns = 10;
        int minStartPeople = 2;
        int maxStartPeople = 6;

        if(mButtonText.getValue().equals("start")){
            mButtonText.postValue("stop");

            mLifeBoard = new LifeBoard(noColumns);
            mLifeBoard.initBoard(minStartPeople, maxStartPeople);
            mPeople.postValue(mLifeBoard.toList());
            mCountLives.postValue(String.format("Lives: %d", mLifeBoard.countLives()));
            mCountX.postValue(String.format("X: %d", mLifeBoard.countX()));
            mCountY.postValue(String.format("Y: %d", mLifeBoard.countY()));

            mStartTime = System.currentTimeMillis();
            mTimerHandler.postDelayed(mTimerRunnable, mDelay);
            mButtonText.postValue("stop");
        }else {
            mTimerHandler.removeCallbacks(mTimerRunnable);
            mButtonText.postValue("start");
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mTimerHandler.removeCallbacks(mTimerRunnable);
        mButtonText.postValue("start");
    }

    @BindingAdapter("data")
    public static void setData(RecyclerView recyclerView, List<PersonField> personFields){
        LifeAdapter adapter = (LifeAdapter) recyclerView.getAdapter();
        if(adapter!=null) adapter.setPeople(personFields);
    }

    public static class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory{

        private Application mApplication;

        public MainActivityViewModelFactory(Application application){
            mApplication = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new MainActivityViewModel(mApplication);
        }
    }
}

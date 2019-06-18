package com.example.ricardo_pedrosa_trapero.ui;

import android.app.Notification;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ricardo_pedrosa_trapero.R;
import com.example.ricardo_pedrosa_trapero.base.Event;
import com.example.ricardo_pedrosa_trapero.data.Repository;
import com.example.ricardo_pedrosa_trapero.data.entity.RandomChuck;
import com.example.ricardo_pedrosa_trapero.data.model.ChuckNorrisJoke;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MainActivityViewModel extends ViewModel {
    private MutableLiveData<ChuckNorrisJoke> chuckNorrisJokeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Event<Notification>> notificationEvent = new MutableLiveData<>();

    void callChuckApi(String category, final View view) {
        loading.postValue(true);
        Call<RandomChuck> call;
        if (category.equals("all")) {
            call = Repository.getInstance().getChuckService().getRandomJoke();
        } else {
            call = Repository.getInstance().getChuckService().getJokeByCategory(category);
        }

        call.enqueue(new Callback<RandomChuck>() {
            @Override
            public void onResponse(Call<RandomChuck> call, Response<RandomChuck> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chuckNorrisJokeMutableLiveData.postValue(new ChuckNorrisJoke(
                            response.body().getValue()
                    ));
                    loading.postValue(false);
                    notificationEvent.postValue(new Event<>(new NotificationCompat.
                            Builder(view.getContext(), MainActivity.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_search_white_24dp)
                            .setContentTitle("Chuck joke")
                            .setContentText(response.body().getValue())
                            .build()));
                } else if (response.body() == null) {
                    chuckNorrisJokeMutableLiveData.postValue(new ChuckNorrisJoke(
                            "No joke this time...!"
                    ));
                    loading.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<RandomChuck> call, Throwable t) {
                chuckNorrisJokeMutableLiveData.postValue(new ChuckNorrisJoke(
                        t.getMessage()
                ));
                loading.postValue(false);
            }
        });
    }


    public LiveData<ChuckNorrisJoke> getChuckNorrisJokeMutableLiveData() {
        return chuckNorrisJokeMutableLiveData;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<Event<Notification>> getNotificationEvent() {
        return notificationEvent;
    }
}

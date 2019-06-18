package com.example.ricardo_pedrosa_trapero.data;

import com.example.ricardo_pedrosa_trapero.data.remote.ChuckService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private static Repository INSTANCE;
    private final ChuckService chuckService;

    private Repository(ChuckService chuckService) {
        this.chuckService = chuckService;
    }

    public static Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository(buildInstance());
        }
        return INSTANCE;
    }

    private static ChuckService buildInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.chucknorris.io/jokes/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ChuckService.class);
    }

    public ChuckService getChuckService() {
        return chuckService;
    }
}

package com.example.ricardo_pedrosa_trapero.data.remote;

import com.example.ricardo_pedrosa_trapero.data.entity.RandomChuck;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChuckService {
    @GET("random")
    Call<RandomChuck> getRandomJoke();

    @GET("random")
    Call<RandomChuck> getJokeByCategory(@Query("category") String category);

    @GET("categories")
    Call<String[]> getCategories();
}

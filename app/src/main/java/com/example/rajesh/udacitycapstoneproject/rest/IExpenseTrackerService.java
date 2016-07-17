package com.example.rajesh.udacitycapstoneproject.rest;


import com.example.rajesh.udacitycapstoneproject.rest.model.CurrentDayWeather;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface IExpenseTrackerService {
    @GET("data/2.5/weather")
    Call<CurrentDayWeather> getCurrentDayWetherInfo(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appId);
}

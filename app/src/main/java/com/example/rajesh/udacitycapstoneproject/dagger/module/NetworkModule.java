package com.example.rajesh.udacitycapstoneproject.dagger.module;


import com.example.rajesh.udacitycapstoneproject.rest.IExpenseTrackerService;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


@Module
public class NetworkModule {
    private final String END_POINT = "http://api.openweathermap.org/";

    @Provides
    @Singleton
    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    public GsonConverterFactory getGSONConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public Retrofit getRetrofit(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
                .baseUrl(END_POINT)
                .client(okHttpClient)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    @Singleton
    public IExpenseTrackerService getIMovieService(Retrofit retrofit) {
        return retrofit.create(IExpenseTrackerService.class);
    }
}

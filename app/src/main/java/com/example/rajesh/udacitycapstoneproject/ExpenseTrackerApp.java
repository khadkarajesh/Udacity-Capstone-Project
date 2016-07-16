package com.example.rajesh.udacitycapstoneproject;

import android.app.Application;

import com.example.rajesh.udacitycapstoneproject.dagger.component.DaggerExpenseTrackerComponent;
import com.example.rajesh.udacitycapstoneproject.dagger.component.ExpenseTrackerComponent;
import com.example.rajesh.udacitycapstoneproject.dagger.module.NetworkModule;
import com.facebook.stetho.Stetho;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;


public class ExpenseTrackerApp extends Application {
    public static ExpenseTrackerComponent expenseTrackerComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initializeHawk();

        expenseTrackerComponent = DaggerExpenseTrackerComponent
                .builder()
                .networkModule(new NetworkModule())
                .build();
    }

    private void initializeHawk() {
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .setLogLevel(LogLevel.FULL)
                .build();
    }

    public static ExpenseTrackerComponent getExpenseTrackerComponent() {
        return expenseTrackerComponent;
    }
}

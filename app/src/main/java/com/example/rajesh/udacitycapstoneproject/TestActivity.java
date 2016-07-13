package com.example.rajesh.udacitycapstoneproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Calendar;

import io.realm.Realm;

public class TestActivity extends AppCompatActivity {

    Realm mRealm;
    Double val = 4242.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        //calendar.add(Calendar.HOUR, 8);
    }
}

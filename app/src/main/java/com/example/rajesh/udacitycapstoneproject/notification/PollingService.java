package com.example.rajesh.udacitycapstoneproject.notification;


import android.app.IntentService;
import android.content.Intent;

import com.example.rajesh.udacitycapstoneproject.utils.AlarmUtil;


public class PollingService extends IntentService {

    private static final String POLLING_SERVICE = "polling_service";

    public PollingService() {
        super(POLLING_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AlarmUtil.setAlarm(this);
    }


}

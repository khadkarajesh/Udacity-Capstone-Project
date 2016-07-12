package com.example.rajesh.udacitycapstoneproject.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.rajesh.udacitycapstoneproject.notification.ExpenseTrackerBroadCastReceiver;

import timber.log.Timber;

public class AlarmUtil {

    public static void setAlarm(Context context) {
        Timber.d("called here set alarm");
        long milliSeconds = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent broadCastIntent = new Intent(context, ExpenseTrackerBroadCastReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadCastIntent, PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, milliSeconds, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, milliSeconds, pendingIntent);
        }
    }
}

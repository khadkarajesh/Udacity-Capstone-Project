package com.example.rajesh.udacitycapstoneproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rajesh.udacitycapstoneproject.activity.LoginActivity;
import com.example.rajesh.udacitycapstoneproject.data.ExpenseTrackerContract;

public class ExpenseTrackerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);

            Cursor cursor = context.getContentResolver().query(ExpenseTrackerContract.WeatherEntry.CONTENT_URI, null, null, null, null);
            cursor.moveToFirst();

            rv.setTextViewText(R.id.tv_weather_description, cursor.getString(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_DESCRIPTION)));

            double maxTemp = cursor.getDouble(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MAX));
            double minTemp = cursor.getDouble(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MIN));

            rv.setTextViewText(R.id.tv_max_min_temp, String.format("H %.0f\u2103 : L %.0f\u2103", maxTemp, minTemp));
            Glide.with(context)
                    .load(Constant.WEATHER_CONDITION_IMAGE_URL)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            rv.setImageViewBitmap(R.id.iv_weather_condition_icon, resource);
                        }
                    });

            Intent toastIntent = new Intent(context, LoginActivity.class);
            PendingIntent toastPendingIntent = PendingIntent.getActivity(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.container, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}

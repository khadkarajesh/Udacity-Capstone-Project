package com.example.rajesh.udacitycapstoneproject.weather;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.activity.BaseActivity;
import com.example.rajesh.udacitycapstoneproject.data.ExpenseTrackerContract;

import butterknife.Bind;
import timber.log.Timber;

public class WeatherActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String WEATHER_CONDITION_IMAGE_URL = "http://openweathermap.org/img/w/";
    @Bind(R.id.tv_time)
    TextView tvTime;

    @Bind(R.id.tv_max_min_temp)
    TextView tvMaxMinTemp;

    @Bind(R.id.iv_weather_condition_icon)
    ImageView ivWeatherConditionIcon;

    @Bind(R.id.tv_weather_description)
    TextView tvWeatherDescription;

    @Bind(R.id.tv_temp)
    TextView tvTemp;

    @Bind(R.id.tv_pressure)
    TextView tvPressure;

    @Bind(R.id.tv_humidity)
    TextView tvHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_weather;
    }

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, WeatherActivity.class);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ExpenseTrackerContract.WeatherEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        tvWeatherDescription.setText(data.getString(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_DESCRIPTION)));
        tvHumidity.setText(String.format("Humidity: %.0f", data.getDouble(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_HUMIDITY))));
        tvPressure.setText(String.format("Pressure: %.0f", data.getDouble(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_PRESSURE))));
        tvTemp.setText(String.format("%.0f\u2103", data.getDouble(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP))));
        Glide.with(this).load(WEATHER_CONDITION_IMAGE_URL + data.getString(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_WEATHER_ICON))+".png").into(ivWeatherConditionIcon);
        double maxTemp = data.getDouble(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MAX));
        double minTemp = data.getDouble(data.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MIN));
        tvMaxMinTemp.setText(String.format("H %.0f\u2103 : L %.0f\u2103", maxTemp, minTemp));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

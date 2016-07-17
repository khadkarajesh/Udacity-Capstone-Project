package com.example.rajesh.udacitycapstoneproject.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ExpenseTrackerContract {
    public static final String CONTENT_AUTHORITY = "com.example.rajesh.udacitycapstoneproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String WEATHER_PATH = "weather";

    public static class WeatherEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(WEATHER_PATH).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + WEATHER_PATH;

        public static final String TABLE_NAME = "weather";

        public static final String COLUMNS_TEMP_MIN = "temp_min";
        public static final String COLUMNS_TEMP_MAX = "temp_max";
        public static final String COLUMNS_HUMIDITY = "humidity";
        public static final String COLUMNS_PRESSURE = "pressure";
        public static final String COLUMNS_SUNRISE = "sunrise";
        public static final String COLUMNS_SUNSET = "sunset";
        public static final String COLUMNS_DESCRIPTION = "description";
        public static final String COLUMNS_WEATHER_ICON = "icon";

        public static Uri buildAccountUri(long accountId) {
            return ContentUris.withAppendedId(CONTENT_URI, accountId);
        }
    }
}

package com.example.rajesh.udacitycapstoneproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ExpenseTrackerDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Expense.db";
    private static final int DATABASE_VERSION = 1;


    public ExpenseTrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + ExpenseTrackerContract.WeatherEntry.TABLE_NAME
                + "("
                + ExpenseTrackerContract.WeatherEntry._ID + " INTEGER PRIMARY KEY,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP + " REAL NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MIN + " REAL NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MAX + " REAL NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_HUMIDITY + " REAL NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_PRESSURE + " REAL NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_SUNRISE + " INTEGER NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_SUNSET + " INTEGER NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_DESCRIPTION + " TEXT NOT NULL,"
                + ExpenseTrackerContract.WeatherEntry.COLUMNS_WEATHER_ICON + " TEXT NOT NULL"
                + ");";

        db.execSQL(CREATE_ACCOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExpenseTrackerContract.WeatherEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}

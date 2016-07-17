package com.example.rajesh.udacitycapstoneproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import timber.log.Timber;


public class ExpenseTrackerProvider extends ContentProvider {

    //SELECT SUM (expense_amount) FROM expense WHERE _id=1 AND expense_date >=1460893092295  AND expense_date <=1461411492295

    ExpenseTrackerDbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    UriMatcher uriMatcher = buildUriMatcher();

    private static final int ACCOUNT = 100;

    @Override
    public boolean onCreate() {
        dbHelper = new ExpenseTrackerDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        sqLiteDatabase = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case ACCOUNT:
                retCursor = sqLiteDatabase.query(ExpenseTrackerContract.WeatherEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        UriMatcher uriMatcher = buildUriMatcher();
        switch (uriMatcher.match(uri)) {
            case ACCOUNT:
                return ExpenseTrackerContract.WeatherEntry.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri resultUri = null;
        long _id;
        switch (uriMatcher.match(uri)) {
            case ACCOUNT:
                _id = dbHelper.getWritableDatabase().insert(ExpenseTrackerContract.WeatherEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    resultUri = ExpenseTrackerContract.WeatherEntry.buildAccountUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowDeleted = 0;
        switch (uriMatcher.match(uri)) {
            case ACCOUNT:
                rowDeleted = dbHelper.getWritableDatabase().delete(ExpenseTrackerContract.WeatherEntry.TABLE_NAME, null, null);
                break;
            default:
                Timber.d("failed to delete data");
        }
        if (rowDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = ExpenseTrackerContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, ExpenseTrackerContract.WEATHER_PATH, ACCOUNT);
        return uriMatcher;
    }


}

package com.example.rajesh.udacitycapstoneproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.rajesh.udacitycapstoneproject.activity.LoginActivity;
import com.example.rajesh.udacitycapstoneproject.data.ExpenseTrackerContract;

import timber.log.Timber;


public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public int getCount() {
        return cursor.getCount();
    }

    public RemoteViews getViewAt(int position) {
        cursor.moveToFirst();

        final RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.tv_weather_description, cursor.getString(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_DESCRIPTION)));

        Timber.d("weather condition %s", cursor.getString(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_DESCRIPTION)));
        double maxTemp = cursor.getDouble(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MAX));
        double minTemp = cursor.getDouble(cursor.getColumnIndex(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MIN));

        rv.setTextViewText(R.id.tv_max_min_temp, String.format("H %.0f\u2103 : L %.0f\u2103", maxTemp, minTemp));

       /* Glide.with(mContext)
                .load(Constant.WEATHER_CONDITION_IMAGE_URL)
                .asBitmap()
                .into(new BitmapImageViewTarget()
                {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                });
        rv.setImageViewBitmap(R.id.iv_weather_condition_icon, resource);*/

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.

        Intent fillInIntent = new Intent(mContext, LoginActivity.class);
        rv.setOnClickFillInIntent(R.id.container, fillInIntent);

        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
        try {
            System.out.println("Loading view " + position);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the remote views object.
        return rv;
    }

    public RemoteViews getLoadingView() {
        // You can create a custom loading view (for instance when getViewAt() is slow.) If you
        // return null here, you will get the default loading view.
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {
        // issue of solved
        //http://stackoverflow.com/questions/13187284/android-permission-denial-in-widget-remoteviewsfactory-for-content
        final long token = Binder.clearCallingIdentity();
        try {
            cursor = mContext.getContentResolver().query(ExpenseTrackerContract.WeatherEntry.CONTENT_URI, null, null, null, null);
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }
}
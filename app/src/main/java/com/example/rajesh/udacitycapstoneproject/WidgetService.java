package com.example.rajesh.udacitycapstoneproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.rajesh.udacitycapstoneproject.activity.LoginActivity;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.utils.MonthTimeStamp;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private RealmResults<Expense> expenses;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
       /* Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                expenses = realm.where(Expense.class)
                        .greaterThan(RealmTable.DATE, MonthTimeStamp.getStartTimeStamp(new Date()))
                        .lessThanOrEqualTo(RealmTable.DATE, MonthTimeStamp.getEndTimeStamp(new Date())).findAll();
            }
        });*/
    }

    public void onCreate() {
    }

    public void onDestroy() {
    }

    public int getCount() {
        return expenses.size();
    }

    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.tv_expense_title, expenses.get(position).getExpenseTitle());
        rv.setInt(R.id.iv_categories_indicator, "setBackgroundResource", Color.parseColor(expenses.get(position).getExpenseCategories().getCategoriesColor()));
        rv.setTextViewText(R.id.tv_price, expenses.get(position).getExpenseTitle());

        // Next, we set a fill-intent which will be used to fill-in the pending intent template
        // which is set on the collection view in StackWidgetProvider.

        Intent fillInIntent = new Intent(mContext, LoginActivity.class);
        rv.setOnClickFillInIntent(R.id.card_view, fillInIntent);

        // You can do heaving lifting in here, synchronously. For example, if you need to
        // process an image, fetch something from the network, etc., it is ok to do it here,
        // synchronously. A loading view will show up in lieu of the actual contents in the
        // interim.
       /* try {
            System.out.println("Loading view " + position);
            //Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

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
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Realm realm = Realm.getDefaultInstance();
                    expenses = realm.where(Expense.class)
                            .greaterThan(RealmTable.DATE, MonthTimeStamp.getStartTimeStamp(new Date()))
                            .lessThanOrEqualTo(RealmTable.DATE, MonthTimeStamp.getEndTimeStamp(new Date())).findAll();
                    realm.close();
                }
            });

        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }
}
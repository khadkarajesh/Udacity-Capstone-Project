package com.example.rajesh.udacitycapstoneproject.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.NotificationCompat;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.realm.Account;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.utils.AlarmUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;


public class ExpenseTrackerBroadCastReceiver extends BroadcastReceiver {

    private Context mContext;
    private Realm mRealm = null;
    private RealmResults<Expense> results;
    private RealmResults<Account> accounts;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mRealm = Realm.getDefaultInstance();

        AlarmUtil.setAlarm(context);

        checkRecurringExpense();
        checkRecurringAccount();
    }

    private void checkRecurringAccount() {
        accounts = mRealm
                .where(Account.class)
                .distinct(RealmTable.TITLE)
                .where()
                .equalTo(RealmTable.TYPE, Constant.RECURRING_TYPE)
                .findAll();

        for (int i = 0; i < accounts.size(); i++) {
            if (isMonthDayEqual(accounts.get(i).getDateCreated())) {
                showNotification(results.get(i).getId(), Constant.ADD_RECURRING_ACCOUNT_ACTION, "Expense Tracker", "Add Recurring Account");
            }
        }
    }

    private void checkRecurringExpense() {
        results = mRealm
                .where(Expense.class)
                .distinct(RealmTable.TITLE)
                .where()
                .equalTo(RealmTable.TYPE, Constant.RECURRING_TYPE)
                .findAll();
        for (int i = 0; i < results.size(); i++) {
            if (isMonthDayEqual(results.get(i).getExpenseDate())) {
                showNotification(results.get(i).getId(), Constant.ADD_RECURRING_EXPENSE_ACTION, "Expense Tracker", "Add Recurring Expense");
            }
        }
    }

    private void showNotification(long id, String action, String contentTitle, String contentText) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_notification);

        Intent intent = new Intent(mContext, ConfirmationActivity.class);
        intent.putExtra(RealmTable.ID, id);
        intent.setAction(action);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(new Random().nextInt(100000), builder.build());
    }

    private boolean isMonthDayEqual(Date expenseDate) {
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.get(Calendar.YEAR);
        calendarCurrent.get(Calendar.DAY_OF_MONTH);

        Calendar calendarExpense = Calendar.getInstance();
        calendarExpense.setTime(expenseDate);
        if (calendarCurrent.get(Calendar.YEAR) == calendarExpense.get(Calendar.YEAR)
                && calendarCurrent.get(Calendar.DAY_OF_MONTH) == calendarExpense.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

}

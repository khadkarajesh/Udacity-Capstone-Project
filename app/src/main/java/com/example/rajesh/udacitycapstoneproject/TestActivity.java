package com.example.rajesh.udacitycapstoneproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;

import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

public class TestActivity extends AppCompatActivity {

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mRealm = Realm.getDefaultInstance();

        RealmResults<Expense> results = mRealm.where(Expense.class).distinct(RealmTable.TITLE).where().equalTo(RealmTable.TYPE, Constant.RECURRING_TYPE).findAll();
        for (int i = 0; i < results.size(); i++) {
            Timber.d("expense title %s", results.get(i).getExpenseTitle());
        }

    }
}

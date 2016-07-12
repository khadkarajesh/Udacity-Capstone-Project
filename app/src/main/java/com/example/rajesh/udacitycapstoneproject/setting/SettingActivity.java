package com.example.rajesh.udacitycapstoneproject.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.activity.BaseActivity;
import com.orhanobut.hawk.Hawk;

import butterknife.Bind;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.sw_recurring_expense)
    Switch swRecurringExpense;

    @Bind(R.id.sw_recurring_account)
    Switch swRecurringAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        swRecurringAccount.setChecked(Hawk.get(Constant.RECURRING_ACCOUNT_NOTIFICATION, false));
        swRecurringExpense.setChecked(Hawk.get(Constant.RECURRING_EXPENSE_NOTIFICATION, false));

        swRecurringAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hawk.put(Constant.RECURRING_ACCOUNT_NOTIFICATION, swRecurringAccount.isChecked() ? true : false);
            }
        });

        swRecurringExpense.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hawk.put(Constant.RECURRING_EXPENSE_NOTIFICATION, swRecurringExpense.isChecked() ? true : false);
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.setting);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }
}

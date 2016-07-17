package com.example.rajesh.udacitycapstoneproject.account;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.CustomToolbar;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.activity.ToolbarBaseActivity;
import com.example.rajesh.udacitycapstoneproject.realm.Account;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.utils.ActivityState;
import com.example.rajesh.udacitycapstoneproject.utils.DateUtil;

import java.util.Calendar;

import butterknife.Bind;
import io.realm.Realm;
import timber.log.Timber;

public class AccountActivity extends ToolbarBaseActivity {

    @Bind(R.id.custom_toolbar_mailing_info)
    CustomToolbar customToolbarMailingInfo;

    @Bind(R.id.edt_account_title)
    EditText edtAccountTitle;

    @Bind(R.id.edt_account_created_date)
    EditText edtAccountCreatedDate;

    @Bind(R.id.edt_account_amount)
    EditText edtAccountAmount;

    @Bind(R.id.swh_account_type)
    SwitchCompat swhAccountType;

    private String toolbarTitle;
    private static final String ADD_ACCOUNT = "Add Account";
    private static final String EDIT_ACCOUNT = "Edit Account";
    private static final int FIRST_DATA_ITEM_ID = 1;
    private static final int ID_INCREMENTER = 1;
    private ActivityState activityState = null;
    private String accountId;
    private java.util.Date accountCreatedDate = null;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();

        accountId = getIntent().getStringExtra(Constant.ACCOUNT);
        activityState = accountId == null ? ActivityState.ADD : ActivityState.UPDATE;
        toolbarTitle = accountId == null ? ADD_ACCOUNT : EDIT_ACCOUNT;

        setToolbar();

        edtAccountCreatedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpenseDate();
            }
        });

        if (activityState == ActivityState.UPDATE) {
            preFillData();
        }
    }

    private void preFillData() {
        Account account = getAccountById(accountId);
        edtAccountTitle.setText(account.getTitle());
        edtAccountAmount.setText(String.valueOf(account.getAccountAmount()));
        edtAccountCreatedDate.setText(DateUtil.formatDate(account.getDateCreated()));
        swhAccountType.setChecked(account.getAccountType().equals(Constant.RECURRING_TYPE) ? true : false);
        accountCreatedDate = account.getDateCreated();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_account;
    }

    private void setToolbar() {
        String submitValue = activityState == ActivityState.ADD ? getString(R.string.txt_save) : getString(R.string.txt_update);
        customToolbarMailingInfo.setToolbar(R.drawable.ic_arrow_back, toolbarTitle, submitValue);
        customToolbarMailingInfo.setLeftButtonClickListener(new CustomToolbar.ToolbarLeftButtonClickListener() {
            @Override
            public void onLeftButtonClick() {
                onBackPressed();
            }
        });
        customToolbarMailingInfo.setRightButtonClickListener(new CustomToolbar.ToolbarRightButtonClickListener() {
            @Override
            public void onRightButtonClick() {
                validateInput();
            }
        });

    }

    private void validateInput() {
        View focusView = null;
        boolean valid = true;

        String accountTitle = edtAccountTitle.getText().toString().trim();
        String accountAmount = edtAccountAmount.getText().toString().trim();

        if (accountCreatedDate == null) {
            focusView = edtAccountCreatedDate;
            valid = false;
            edtAccountCreatedDate.setError(getString(R.string.msg_empty_date));
        }
        if (accountCreatedDate.after(Calendar.getInstance().getTime())) {
            focusView = edtAccountCreatedDate;
            valid = false;
            edtAccountCreatedDate.setError(getString(R.string.msg_valid_date));
        }

        if (accountTitle.isEmpty()) {
            focusView = edtAccountTitle;
            valid = false;
            edtAccountTitle.setError(getString(R.string.msg_empty_field));
        }

        if (accountAmount.isEmpty()) {
            focusView = edtAccountAmount;
            valid = false;
            edtAccountAmount.setError(getString(R.string.msg_empty_amount));
        }

        if (valid) {
            Timber.d("save button clicked");
            if (activityState == ActivityState.ADD) {
                saveAccount(accountTitle, accountAmount);
            } else {
                updateAccount(accountTitle, accountAmount);
            }
            finish();
        } else {
            focusView.requestFocus();
        }
    }

    private void updateAccount(String accountTitle, String accountAmount) {
        Timber.d("update account called");
        Account account = getAccountById(accountId);
        mRealm.beginTransaction();
        account.setTitle(accountTitle);
        account.setDateCreated(accountCreatedDate);
        account.setAccountAmount(Double.valueOf(accountAmount));
        account.setAccountType(swhAccountType.isChecked() ? Constant.RECURRING_TYPE : Constant.NON_RECURRING_TYPE);
        mRealm.commitTransaction();
    }

    private void saveAccount(String accountTitle, String accountAmount) {
        mRealm.beginTransaction();
        Account account = mRealm.createObject(Account.class);
        account.setId(getNextAccountId());
        account.setTitle(accountTitle);
        account.setAccountAmount(Double.valueOf(accountAmount));
        account.setDateCreated(accountCreatedDate);
        account.setAccountType(swhAccountType.isChecked() ? Constant.RECURRING_TYPE : Constant.NON_RECURRING_TYPE);
        mRealm.commitTransaction();
    }

    /**
     * Provides mExpense date in Format Jul 23,2016
     *
     * @return mExpense date as string
     */
    public void setExpenseDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                accountCreatedDate = calendar.getTime();
                edtAccountCreatedDate.setText(DateUtil.formatDate(calendar.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(true);
        datePickerDialog.show();
    }

    private int getNextAccountId() {
        if (mRealm.where(Account.class).max(RealmTable.ID) == null) {
            return FIRST_DATA_ITEM_ID;
        } else {
            return mRealm.where(Account.class).max(RealmTable.ID).intValue() + ID_INCREMENTER;
        }
    }

    private Account getAccountById(String id) {
        Account account = mRealm.where(Account.class).equalTo(RealmTable.ID, Integer.parseInt(id)).findFirst();
        return account;
    }

    public static Intent getLaunchIntent(Context context, String accountId) {
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra(Constant.ACCOUNT, accountId);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

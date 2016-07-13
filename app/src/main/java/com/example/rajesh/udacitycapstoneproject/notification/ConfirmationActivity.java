package com.example.rajesh.udacitycapstoneproject.notification;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.activity.LoginActivity;
import com.example.rajesh.udacitycapstoneproject.base.activity.BaseActivity;
import com.example.rajesh.udacitycapstoneproject.realm.Account;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;

import java.util.Date;

import io.realm.Realm;

public class ConfirmationActivity extends BaseActivity {
    private static final int FIRST_DATA_ITEM_ID = 1;
    private static final int ID_INCREMENTER = 1;

    private long id = 0;
    private Realm mRealm = null;
    private Expense mExpense = null;
    private Account mAccount = null;
    private String intentAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        String title = null;
        String content = null;

        id = getIntent().getLongExtra(RealmTable.ID, 0);
        intentAction = getIntent().getAction();

        if (intentAction.equals(Constant.ADD_RECURRING_EXPENSE_ACTION)) {
            mExpense = mRealm.where(Expense.class).equalTo(RealmTable.ID, id).findFirst();
            title = getString(R.string.add_expense);
            content = getString(R.string.confirmation_dialog_content, getString(R.string.expense), mExpense.getExpenseAmount(), getString(R.string.expense));
        } else if (intentAction.equals(Constant.ADD_RECURRING_ACCOUNT_ACTION)) {
            mAccount = mRealm.where(Account.class).equalTo(RealmTable.ID, id).findFirst();
            title = getString(R.string.add_account);
            content = getString(R.string.confirmation_dialog_content, getString(R.string.account), mAccount.getAccountAmount(), getString(R.string.account));
        }

        showConfirmationDialog(title, content);
    }

    private void showConfirmationDialog(String title, String content) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this).setTitle(title).setMessage(content).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeStateOfData();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void changeStateOfData() {
        if (intentAction.equals(Constant.ADD_RECURRING_EXPENSE_ACTION)) {
            insertRecurringExpense();
        }
        if (intentAction.equals(Constant.ADD_RECURRING_ACCOUNT_ACTION)) {
            insertRecurringAccount();
        }
        startActivity(LoginActivity.getLaunchIntent(this));
        finish();
    }

    private void insertRecurringExpense() {
        mRealm.beginTransaction();
        Expense expense = mRealm.createObject(Expense.class);
        expense.setId(getNextExpenseId());
        expense.setExpenseTitle(mExpense.getExpenseTitle());
        expense.setExpenseAmount(mExpense.getExpenseAmount());
        expense.setExpenseType(mExpense.getExpenseType());
        expense.setExpenseDescription(mExpense.getExpenseDescription());
        expense.setExpenseDate(new Date());
        expense.setExpenseCategories(mExpense.getExpenseCategories());
        mRealm.commitTransaction();
    }

    private void insertRecurringAccount() {
        mRealm.beginTransaction();
        Account account = mRealm.createObject(Account.class);
        account.setTitle(mAccount.getTitle());
        account.setId(getNextAccountId());
        account.setAccountAmount(mAccount.getAccountAmount());
        account.setDateCreated(new Date());
        account.setAccountType(mAccount.getAccountType());
        mRealm.commitTransaction();
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_confirmation;
    }

    private int getNextExpenseId() {
        if (mRealm.where(Expense.class).max(RealmTable.ID) == null) {
            return FIRST_DATA_ITEM_ID;
        } else {
            return mRealm.where(Expense.class).max(RealmTable.ID).intValue() + ID_INCREMENTER;
        }
    }

    private int getNextAccountId() {
        if (mRealm.where(Account.class).max(RealmTable.ID) == null) {
            return FIRST_DATA_ITEM_ID;
        } else {
            return mRealm.where(Account.class).max(RealmTable.ID).intValue() + ID_INCREMENTER;
        }
    }
}

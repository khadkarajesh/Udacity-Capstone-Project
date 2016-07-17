package com.example.rajesh.udacitycapstoneproject.expense;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.CustomToolbar;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.activity.ToolbarBaseActivity;
import com.example.rajesh.udacitycapstoneproject.realm.Expense;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.utils.ActivityState;
import com.example.rajesh.udacitycapstoneproject.utils.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

public class ExpenseActivity extends ToolbarBaseActivity {
    @Bind(R.id.edt_expense_title)
    EditText edtExpenseTitle;

    @Bind(R.id.edt_expense_date)
    EditText edtExpenseDate;

    @Bind(R.id.edt_expense_amount)
    EditText edtExpenseAmount;

    @Bind(R.id.edt_expense_description)
    EditText edtExpenseDescription;

    @Bind(R.id.tv_category_label)
    TextView tvCategoryLabel;

    @Bind(R.id.spinner_expense_categories)
    AppCompatSpinner spinnerExpenseCategories;

    @Bind(R.id.swh_expense_type)
    SwitchCompat swhExpenseType;

    private String toolbarTitle;

    private static final String ADD_EXPENSE = "Add Expense";
    private static final String EDIT_EXPENSE = "Edit Expense";
    private static final int FIRST_DATA_ITEM_ID = 1;
    private static final int ID_INCREMENTER = 1;

    @Bind(R.id.custom_toolbar_mailing_info)
    CustomToolbar customToolbarMailingInfo;

    private String expenseId;
    private ActivityState activityState = null;

    private ExpenseCategoryAdapter expenseCategoryAdapter;
    private java.util.Date expenseDate = null;
    private int categoryId = 0;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();

        expenseId = getIntent().getStringExtra(Constant.EXPENSE);
        activityState = expenseId == null ? ActivityState.ADD : ActivityState.UPDATE;
        toolbarTitle = expenseId == null ? ADD_EXPENSE : EDIT_EXPENSE;

        setToolbar();

        setExpenseCategory();

        edtExpenseDate.setOnClickListener(new View.OnClickListener() {
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
        Expense expense = getExpenseById(expenseId);
        edtExpenseTitle.setText(expense.getExpenseTitle());
        edtExpenseAmount.setText(String.valueOf(expense.getExpenseAmount()));
        edtExpenseDate.setText(DateUtil.formatDate(expense.getExpenseDate()));
        edtExpenseDescription.setText(expense.getExpenseDescription());
        swhExpenseType.setChecked(expense.getExpenseType().equals(Constant.RECURRING_TYPE) ? true : false);
        spinnerExpenseCategories.setSelection(expenseCategoryAdapter.getCategoryPositionById((int) expense.getId()));
        expenseDate = expense.getExpenseDate();
    }

    private void setExpenseCategory() {
        RealmResults<ExpenseCategories> expenseCategories = mRealm.where(ExpenseCategories.class).findAll();
        expenseCategoryAdapter = new ExpenseCategoryAdapter(this, expenseCategories);
        spinnerExpenseCategories.setAdapter(expenseCategoryAdapter);

        spinnerExpenseCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = expenseCategoryAdapter.getExpenseCategoryId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_expense;
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

        String expenseTitle = edtExpenseTitle.getText().toString().trim();
        String expenseDescription = edtExpenseDescription.getText().toString().trim();
        String expenseAmount = edtExpenseAmount.getText().toString().trim();

        if (expenseDate == null) {
            focusView = edtExpenseDate;
            valid = false;
            edtExpenseDate.setError(getString(R.string.msg_empty_date));
        }
        if (expenseDate.after(Calendar.getInstance().getTime())) {
            focusView = edtExpenseDate;
            valid = false;
            edtExpenseDate.setError(getString(R.string.msg_valid_date));
        }

        if (expenseTitle.isEmpty()) {
            focusView = edtExpenseDate;
            valid = false;
            edtExpenseTitle.setError(getString(R.string.msg_empty_field));
        }

        if (expenseDescription.isEmpty()) {
            focusView = edtExpenseDescription;
            valid = false;
            edtExpenseTitle.setError(getString(R.string.msg_empty_field));
        }

        if (expenseAmount.isEmpty()) {
            focusView = edtExpenseAmount;
            valid = false;
            edtExpenseDate.setError(getString(R.string.msg_empty_amount));
        }

        if (valid) {
            if (activityState == ActivityState.ADD) {
                saveExpense(expenseTitle, expenseDescription, expenseAmount);
            } else {
                updateExpense(expenseTitle, expenseDescription, expenseAmount);
            }
            finish();
        } else {
            focusView.requestFocus();
        }
    }

    private void updateExpense(String expenseTitle, String expenseDescription, String expenseAmount) {
        Expense expense = getExpenseById(expenseId);
        mRealm.beginTransaction();
        expense.setExpenseTitle(expenseTitle);
        expense.setExpenseDate(expenseDate);
        expense.setExpenseDescription(expenseDescription);
        expense.setExpenseAmount(Double.valueOf(expenseAmount));
        expense.setExpenseType(swhExpenseType.isChecked() ? Constant.RECURRING_TYPE : Constant.NON_RECURRING_TYPE);
        mRealm.commitTransaction();
    }

    private void saveExpense(String expenseTitle, String expenseDescription, String expenseAmount) {
        mRealm.beginTransaction();
        Expense expense = mRealm.createObject(Expense.class);
        expense.setId(getNextExpenseId());
        expense.setExpenseTitle(expenseTitle);
        expense.setExpenseDate(expenseDate);
        expense.setExpenseDescription(expenseDescription);
        expense.setExpenseAmount(Double.valueOf(expenseAmount));
        expense.setExpenseType(swhExpenseType.isChecked() ? Constant.RECURRING_TYPE : Constant.NON_RECURRING_TYPE);

        ExpenseCategories expenseCategories = mRealm.where(ExpenseCategories.class).equalTo(RealmTable.ID, categoryId).findFirst();
        expense.setExpenseCategories(expenseCategories);

        expenseCategories.getExpenses().add(expense);

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
                expenseDate = calendar.getTime();
                edtExpenseDate.setText(DateUtil.formatDate(calendar.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(true);
        datePickerDialog.show();
    }

    public static Intent getLaunchIntent(Context context, String expenseId) {
        Intent intent = new Intent(context, ExpenseActivity.class);
        intent.putExtra(Constant.EXPENSE, expenseId);
        return intent;
    }


    private int getNextExpenseId() {
        if (mRealm.where(Expense.class).max(RealmTable.ID) == null) {
            return FIRST_DATA_ITEM_ID;
        } else {
            return mRealm.where(Expense.class).max(RealmTable.ID).intValue() + ID_INCREMENTER;
        }
    }

    private Expense getExpenseById(String id) {
        Expense expense = mRealm.where(Expense.class).equalTo(RealmTable.ID, Integer.parseInt(id)).findFirst();
        return expense;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

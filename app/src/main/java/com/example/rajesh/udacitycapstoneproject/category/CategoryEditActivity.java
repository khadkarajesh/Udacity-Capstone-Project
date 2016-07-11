package com.example.rajesh.udacitycapstoneproject.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.CustomToolbar;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.activity.ToolbarBaseActivity;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.utils.ActivityState;

import butterknife.Bind;
import butterknife.OnClick;
import io.realm.Realm;
import me.priyesh.chroma.ChromaDialog;
import me.priyesh.chroma.ColorSelectListener;
import timber.log.Timber;


public class CategoryEditActivity extends ToolbarBaseActivity {
    private String toolbarTitle;

    private static final String ADD_CATEGORY = "Add Category";
    private static final String EDIT_CATEGORY = "Edit Category";
    private static final int FIRST_DATA_ITEM_ID = 1;
    private static final int ID_INCREMENTER = 1;

    @Bind(R.id.custom_toolbar_mailing_info)
    CustomToolbar customToolbarMailingInfo;

    @Bind(R.id.edt_category_name)
    EditText edtCategoryName;

    @Bind(R.id.iv_color_picker)
    ImageView ivColorPicker;

    private String expenseCategoryId;
    private String hexColor = "#ff08f0";
    private ActivityState activityState = null;

    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRealm = Realm.getDefaultInstance();

        expenseCategoryId = getIntent().getStringExtra(Constant.CATEGORY);
        activityState = expenseCategoryId == null ? ActivityState.ADD : ActivityState.UPDATE;
        toolbarTitle = expenseCategoryId == null ? ADD_CATEGORY : EDIT_CATEGORY;

        ivColorPicker.setBackgroundColor(Color.parseColor(getPickerColor()));
        setToolbar();
        if (activityState == ActivityState.UPDATE) {
            preFillData();
        }
    }

    private void preFillData() {
        ExpenseCategories expenseCategories = getExpenseCategoryById(expenseCategoryId);
        edtCategoryName.setText(expenseCategories.getCategoriesName());
        ivColorPicker.setBackgroundColor(Color.parseColor(expenseCategories.getCategoriesColor()));
    }

    private void setToolbar() {
        String submitValue = activityState == ActivityState.ADD ? "SAVE" : "UPDATE";
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
        String expenseCategory = edtCategoryName.getText().toString().trim();
        boolean valid = true;
        if (expenseCategory.isEmpty()) {
            edtCategoryName.setError(getString(R.string.empty_catgory_message));
            valid = false;
        }
        if (valid) {
            if (activityState == ActivityState.ADD) {
                insertCategory(expenseCategory, getPickerColor());
            } else {
                updateCategory(expenseCategory, getPickerColor());
            }
        }
    }

    private void insertCategory(String expenseCategory, String categoryColor) {
        try {
            mRealm.beginTransaction();
            int nextID = getNextCategoryId();
            ExpenseCategories expenseCategories = mRealm.createObject(ExpenseCategories.class);
            expenseCategories.setId(nextID);
            expenseCategories.setCategoriesName(expenseCategory);
            expenseCategories.setCategoriesColor(categoryColor);
            mRealm.commitTransaction();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        }
    }

    private void updateCategory(String expenseCategory, String categoryColor) {
        ExpenseCategories expenseCategories = getExpenseCategoryById(expenseCategoryId);
        mRealm.beginTransaction();
        expenseCategories.setCategoriesColor(categoryColor);
        expenseCategories.setCategoriesName(expenseCategory);
        mRealm.commitTransaction();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_category_edit;
    }

    public static Intent getLaunchIntent(Context context, String expenseCategoryId) {
        Intent intent = new Intent(context, CategoryEditActivity.class);
        intent.putExtra(Constant.CATEGORY, expenseCategoryId);
        return intent;
    }


    private int getNextCategoryId() {
        if (mRealm.where(ExpenseCategories.class).max(RealmTable.ID) == null) {
            return FIRST_DATA_ITEM_ID;
        } else {
            return mRealm.where(ExpenseCategories.class).max(RealmTable.ID).intValue() + ID_INCREMENTER;
        }
    }

    @OnClick({R.id.iv_color_picker})
    public void onClick(View view) {
        if (view.getId() == R.id.iv_color_picker) {
            new ChromaDialog.Builder()
                    .initialColor(Color.GREEN)
                    .onColorSelected(new ColorSelectListener() {
                        @Override
                        public void onColorSelected(@ColorInt int i) {
                            setPickerColor(String.format("#%06X", (0xFFFFFF & i)));
                            ivColorPicker.setBackgroundColor(Color.parseColor(hexColor));
                        }
                    })
                    .create()
                    .show(getSupportFragmentManager(), getString(R.string.color_picker_dialog));
        }
    }

    private String getPickerColor() {
        return hexColor;
    }

    private void setPickerColor(String color) {
        this.hexColor = color;
    }

    private ExpenseCategories getExpenseCategoryById(String id) {
        ExpenseCategories expenseCategories = mRealm.where(ExpenseCategories.class).equalTo(RealmTable.ID, Integer.parseInt(id)).findFirst();
        return expenseCategories;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}

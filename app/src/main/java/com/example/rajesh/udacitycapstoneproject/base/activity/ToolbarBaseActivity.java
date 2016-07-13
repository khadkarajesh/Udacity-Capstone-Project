package com.example.rajesh.udacitycapstoneproject.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.example.rajesh.udacitycapstoneproject.R;

import butterknife.Bind;

/**
 * Base Activity with toolbar
 */
public abstract class ToolbarBaseActivity extends BaseActivity {

    @Nullable
    @Bind(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureToolbar();
    }

    /**
     * implement {@link Toolbar} to the child Activities
     */
    private void configureToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    /**
     * Use when necessary
     *
     * @param color
     */
    public void setToolbarBackgroundColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }
}

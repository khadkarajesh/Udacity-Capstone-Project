package com.example.rajesh.udacitycapstoneproject.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.base.activity.BaseActivity;

import butterknife.Bind;

public class ReportActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.history_report);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ReportViewPagerAdapter adapter = new ReportViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HistoryFragment(), getString(R.string.history));
        adapter.addFragment(new ReportFragment(), getString(R.string.report));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_report;
    }

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, ReportActivity.class);
    }
}

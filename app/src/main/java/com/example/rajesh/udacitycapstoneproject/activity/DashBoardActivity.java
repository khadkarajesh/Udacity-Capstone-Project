package com.example.rajesh.udacitycapstoneproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.account.AccountFragment;
import com.example.rajesh.udacitycapstoneproject.base.activity.BaseActivity;
import com.example.rajesh.udacitycapstoneproject.category.CategoryFragment;
import com.example.rajesh.udacitycapstoneproject.dashboard.DashBoardFragment;
import com.example.rajesh.udacitycapstoneproject.expense.recurring.RecurringFragment;
import com.example.rajesh.udacitycapstoneproject.report.ReportActivity;
import com.example.rajesh.udacitycapstoneproject.setting.SettingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DashBoardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.ll_dashboard_wrapper)
    LinearLayout llDashboardWrapper;

    @Bind(R.id.nav_view)
    NavigationView navView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ImageView profilePic;
    private FirebaseUser firebaseUser;

    String toolbarTitle = null;

    private static final String ACCOUNTS_TITLE = "Accounts";
    private static final String CATEGORIES_TITLE = "Categories";
    private static final String HISTORY_AND_REPORT_TITLE = "History / Report";
    private static final String RECURRING_EXPENSE_TITLE = "Recurring Expense";
    private static final String DASHBOARD_TITLE = "Dashboard";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        navView.setNavigationItemSelectedListener(this);

        setUserProfile(navView);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_dash_board;
    }

    @Override
    protected void onResume() {
        super.onResume();
        addFragment(new DashBoardFragment(), Constant.FragmentTag.DASHBOARD_FRAGMENT_TAG);
        getSupportActionBar().setTitle(DASHBOARD_TITLE);
    }

    private void setUserProfile(NavigationView navigationView) {
        View view = navigationView.getHeaderView(0);
        profilePic = ButterKnife.findById(view, R.id.imageView);
        TextView txtUserName = ButterKnife.findById(view, R.id.txt_username);
        txtUserName.setText(firebaseUser.getDisplayName());
        TextView txtEmail = ButterKnife.findById(view, R.id.txt_email);
        txtEmail.setText(firebaseUser.getEmail().toString());
        if (firebaseUser.getPhotoUrl() != null) {
            setCircularProfileImageToNavHeader(firebaseUser.getPhotoUrl().toString());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        String fragmentTag = null;

        if (id == R.id.nav_history_report) {
            startActivity(ReportActivity.getLaunchIntent(this));
            return true;
        }
        if (id == R.id.nav_settings) {
            startActivity(SettingActivity.getLaunchIntent(this));
            return true;
        }

        switch (id) {
            case R.id.nav_account:
                fragment = new AccountFragment();
                fragmentTag = Constant.FragmentTag.ACCOUNT_FRAGMENT;
                toolbarTitle = ACCOUNTS_TITLE;
                break;
            case R.id.nav_categories:
                fragment = new CategoryFragment();
                fragmentTag = Constant.FragmentTag.CATEGORY_FRAGMENT;
                toolbarTitle = CATEGORIES_TITLE;
                break;
            case R.id.nav_recurring_expense:
                fragment = new RecurringFragment();
                fragmentTag = Constant.FragmentTag.EXPENSE_FRAGMENT;
                toolbarTitle = RECURRING_EXPENSE_TITLE;
                break;
            case R.id.nav_dashboard:
                fragment = new DashBoardFragment();
                fragmentTag = Constant.FragmentTag.DASHBOARD_FRAGMENT_TAG;
                toolbarTitle = DASHBOARD_TITLE;
                break;
            default:
                break;
        }
        addFragment(fragment, fragmentTag);
        getSupportActionBar().setTitle(toolbarTitle);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent getLaunchIntent(Context context) {
        Intent intent = new Intent(context, DashBoardActivity.class);
        return intent;
    }

    /**
     * Use glide to make the profile image circular
     *
     * @param imageUrl source image Url
     */
    private void setCircularProfileImageToNavHeader(String imageUrl) {
        Glide.with(DashBoardActivity.this).load(imageUrl)
                .asBitmap().override(200, 200)
                .error(R.drawable.ic_no_avatar)
                .placeholder(R.drawable.ic_no_avatar)
                .into(new BitmapImageViewTarget(profilePic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(DashBoardActivity.this.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        profilePic.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_dashboard_wrapper, fragment, tag).addToBackStack(tag).commit();
    }
}

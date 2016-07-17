package com.example.rajesh.udacitycapstoneproject.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rajesh.udacitycapstoneproject.BuildConfig;
import com.example.rajesh.udacitycapstoneproject.Constant;
import com.example.rajesh.udacitycapstoneproject.ExpenseTrackerApp;
import com.example.rajesh.udacitycapstoneproject.R;
import com.example.rajesh.udacitycapstoneproject.data.ExpenseTrackerContract;
import com.example.rajesh.udacitycapstoneproject.realm.ExpenseCategories;
import com.example.rajesh.udacitycapstoneproject.realm.table.RealmTable;
import com.example.rajesh.udacitycapstoneproject.rest.IExpenseTrackerService;
import com.example.rajesh.udacitycapstoneproject.rest.model.CurrentDayWeather;
import com.example.rajesh.udacitycapstoneproject.utils.AlarmUtil;
import com.example.rajesh.udacitycapstoneproject.weather.WeatherActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.orhanobut.hawk.Hawk;

import java.util.Arrays;
import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int FIRST_DATA_ITEM_ID = 1;
    private static final int ID_INCREMENTER = 1;
    public static final int GOOGLE_SIGN_IN_CODE = 5000;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 200;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    CallbackManager callbackManager;

    @Bind(R.id.username)
    EditText username;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.login)
    Button login;

    @Bind(R.id.sign_up)
    Button signUp;

    @Bind(R.id.adView)
    AdView mAdView;

    private Realm mRealm = null;
    private FirebaseAnalytics mFirebaseAnalytics;
    private LocationManager lm;
    private double longitude;
    private double latitude;
    @Inject
    IExpenseTrackerService iExpenseTrackerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startActivity(new Intent(this, WeatherActivity.class));
        mRealm = Realm.getDefaultInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ExpenseTrackerApp.getExpenseTrackerComponent().inject(this);

        AlarmUtil.setAlarm(this);

        ButterKnife.bind(this);

        if (!Hawk.get(Constant.APP_LAUNCH, false)) {
            autoPopulateCategories();
            Hawk.put(Constant.RECURRING_ACCOUNT_NOTIFICATION, true);
            Hawk.put(Constant.RECURRING_EXPENSE_NOTIFICATION, true);
        }

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid() + " display name " + user.getDisplayName());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(LocationServices.API)
                .build();


        //show add
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1642716182175443/2415407215");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void autoPopulateCategories() {
        String[] categories = new String[]{"Food",
                "Shelter",
                "Utilities",
                "Clothing",
                "Transportation",
                "Medical",
                "Insurance",
                "Household",
                "Personal",
                "Education",
                "Gifts",
                "Fun Money"};

        for (String category : categories) {
            mRealm.beginTransaction();
            ExpenseCategories expenseCategories = mRealm.createObject(ExpenseCategories.class);
            expenseCategories.setId(getNextCategoryId());
            expenseCategories.setCategoriesColor(getRandomColor());
            expenseCategories.setCategoriesName(category);
            mRealm.commitTransaction();
        }
        Hawk.put(Constant.APP_LAUNCH, true);
    }

    private void loginViaFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, email, user_birthday, user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG, "onError: " + exception.getLocalizedMessage());
                    }
                });
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            } else {
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            getLocation();
                            trackLogin(getString(R.string.google));
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick({R.id.btn_facebook_login, R.id.btn_google_login, R.id.sign_up, R.id.login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook_login:
                loginViaFacebook();
                break;
            case R.id.btn_google_login:
                googleSignIn();
                break;
            case R.id.sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.login:
                loginUser();
                break;
        }
    }

    private void loginUser() {
        View focusView = null;
        boolean valid = true;
        String usernameValue = username.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();
        if (usernameValue.isEmpty()) {
            username.setError(getString(R.string.empty_msg));
            valid = false;
            focusView = username;
        }

        if (passwordValue.isEmpty() || passwordValue.length() <= 5) {
            password.setError(getString(R.string.valid_password));
            valid = false;
            focusView = password;
        }

        if (valid) {
            loginViaEmail(usernameValue, passwordValue);
        } else {
            focusView.requestFocus();
        }
    }

    private void loginViaEmail(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getLocation();
                    trackLogin(getString(R.string.login_via_email));
                } else {
                    Toast.makeText(LoginActivity.this, "Couldn't login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            getLocation();
                            trackLogin(getString(R.string.facebook));
                        }
                    }
                });
    }


    private int getNextCategoryId() {
        if (mRealm.where(ExpenseCategories.class).max(RealmTable.ID) == null) {
            return FIRST_DATA_ITEM_ID;
        } else {
            return mRealm.where(ExpenseCategories.class).max(RealmTable.ID).intValue() + ID_INCREMENTER;
        }
    }

    private String getRandomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return String.format("#%06X", 0xFFFFFF & color);
    }

    public static Intent getLaunchIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    public void trackLogin(String loginType) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getString(R.string.id));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, loginType);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, getString(R.string.sign_in));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private void navigateToDashboard() {
        startActivity(DashBoardActivity.getLaunchIntent(LoginActivity.this));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            Timber.d("permission called here");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(LoginActivity.this, "permission allowed", Toast.LENGTH_SHORT).show();
                    getLocation();
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void getLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }
        }
        fetchWeatherData();
    }

    private void fetchWeatherData() {
        Call<CurrentDayWeather> currentDayWeatherCall = iExpenseTrackerService.getCurrentDayWetherInfo(String.valueOf(latitude), String.valueOf(longitude), BuildConfig.WEATHER_API_KEY);
        currentDayWeatherCall.enqueue(new Callback<CurrentDayWeather>() {
            @Override
            public void onResponse(Response<CurrentDayWeather> response, Retrofit retrofit) {
                saveWeather(response.body());
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void saveWeather(CurrentDayWeather currentDayWeather) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP, currentDayWeather.main.temp);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_DESCRIPTION, currentDayWeather.weather.get(0).description);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_WEATHER_ICON, currentDayWeather.weather.get(0).icon);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_HUMIDITY, currentDayWeather.main.humidity);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_PRESSURE, currentDayWeather.main.pressure);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MIN, currentDayWeather.main.temp_min);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_TEMP_MAX, currentDayWeather.main.temp_max);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_SUNRISE, currentDayWeather.sys.sunrise);
        contentValues.put(ExpenseTrackerContract.WeatherEntry.COLUMNS_SUNSET, currentDayWeather.sys.sunset);

        getContentResolver().delete(ExpenseTrackerContract.WeatherEntry.CONTENT_URI, null, null);

        Uri uri = getContentResolver().insert(ExpenseTrackerContract.WeatherEntry.CONTENT_URI, contentValues);
        if (ContentUris.parseId(uri) > 0) {
            navigateToDashboard();
        }
    }
}


package com.example.rajesh.udacitycapstoneproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @Bind(R.id.email)
    EditText email;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.confirm_password)
    EditText confirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btn_create_user)
    public void onClick() {
        createUser();
    }

    private void createUser() {
        View focusView = null;
        String emailValue = email.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();
        String confirmPasswordValue = confirmPassword.getText().toString().trim();

        boolean valid = true;

        if (emailValue.isEmpty()) {
            email.setError(getString(R.string.empty_msg));
            valid = false;
            focusView = email;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            email.setError(getString(R.string.valid_email_address_msg));
            valid = false;
            focusView = email;
        }

        if (passwordValue.isEmpty()) {
            password.setError(getString(R.string.empty_msg));
            focusView = password;
            valid = false;
        } else if (passwordValue.length() <= 4) {
            password.setError(getString(R.string.password_length_message));
            focusView = password;
            valid = false;
        }

        if (!passwordValue.equals(confirmPasswordValue)) {
            confirmPassword.setError(getString(R.string.password_doesnt_match));
            focusView = confirmPassword;
            valid = false;
        }

        if (valid) {
            createUser(emailValue, passwordValue);
        } else {
            focusView.requestFocus();
        }
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "failed to complete task", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(DashBoardActivity.getLaunchIntent(SignUpActivity.this));
                }
            }
        });
    }
}

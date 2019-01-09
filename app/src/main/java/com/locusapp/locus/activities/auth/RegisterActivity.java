package com.locusapp.locus.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.locusapp.locus.R;
import com.locusapp.locus.activities.app.DashboardActivity;


public class RegisterActivity extends AppCompatActivity {

    private TextView lblCreateAccount;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private String TAG = "RegisterActivity";
    private EditText etEmail, etPassword, etPasswordCheck;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lblCreateAccount = findViewById(R.id.lblCreateAccount);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordCheck = findViewById(R.id.etPasswordCheck);

        mAuth = FirebaseAuth.getInstance();

        lblCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().matches("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter an Email-Address", Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().toString().matches("")) {
                    Toast.makeText(RegisterActivity.this, "Please enter a Password", Toast.LENGTH_SHORT).show();
                } else if (etPasswordCheck.getText().toString().matches("")) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (etPassword.getText().toString().matches(etPasswordCheck.getText().toString()) ) {
                        email = etEmail.getText().toString();
                        password = etPassword.getText().toString();
                        firebaseCreateUserWithEmailAndPassword(email, password);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            proceedToDashboard(currentUser);
        }
    }

    private void firebaseCreateUserWithEmailAndPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            proceedToDashboard(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void proceedToDashboard(FirebaseUser user) {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void proceedToDashboard() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}

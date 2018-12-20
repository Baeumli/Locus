package com.locusapp.locus.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.locusapp.locus.R;
import com.locusapp.locus.activities.app.DashboardActivity;


public class RegisterActivity extends AppCompatActivity {

    private TextView lblCreateAccount;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lblCreateAccount = findViewById(R.id.lblCreateAccount);
        btnRegister = findViewById(R.id.btnRegister);

        lblCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

package me.baeumli.locus.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import me.baeumli.locus.R;

public class RegisterActivity extends AppCompatActivity {

    private TextView lblCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lblCreateAccount = findViewById(R.id.lblCreateAccount);

        lblCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

package me.baeumli.locus.activities.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import me.baeumli.locus.R;

public class LoginActivity extends AppCompatActivity {

    private TextView lblCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lblCreateAccount = findViewById(R.id.lblCreateAccount);

        lblCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getRegisterActivityIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(getRegisterActivityIntent);
            }
        });

    }

}

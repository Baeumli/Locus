package baeumli.me.locus.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import baeumli.me.locus.R;
import baeumli.me.locus.activities.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent getLoginActivityIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(getLoginActivityIntent);
        finish();
    }
}

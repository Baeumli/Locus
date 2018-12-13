package me.baeumli.locus.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.baeumli.locus.R;

public class MainActivity extends AppCompatActivity {

    private Button btnCallActivity1, btnCallActivity2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCallActivity1 = findViewById(R.id.btnCallActivity1);
        btnCallActivity2 = findViewById(R.id.btnCallActivity2);

        btnCallActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnCallActivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getSplashActivityIntent = new Intent(MainActivity.this, SplashActivity.class);

                startActivity(getSplashActivityIntent);

            }
        });



    }



}

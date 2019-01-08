package com.locusapp.locus.activities.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.locusapp.locus.R;
import com.locusapp.locus.models.GlideApp;

public class WinActivity extends AppCompatActivity {

    private ImageView imgViewWin;
    private TextView lblWinMessage;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        imgViewWin = findViewById(R.id.imgViewWin);
        lblWinMessage = findViewById(R.id.lblWinMessage);
        btnClose = findViewById(R.id.btnClose);

        String image = getIntent().getStringExtra("image");
        String winMessage = getIntent().getStringExtra("message");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(image);

        GlideApp.with(this).load(storageReference).into(imgViewWin);
        lblWinMessage.setText(winMessage);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

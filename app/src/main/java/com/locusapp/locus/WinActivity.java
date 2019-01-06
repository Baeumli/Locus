package com.locusapp.locus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.locusapp.locus.models.GlideApp;
import com.locusapp.locus.models.MyGlideAppModule;

public class WinActivity extends AppCompatActivity {

    private ImageView imgViewWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        imgViewWin = findViewById(R.id.imgViewWin);
        String image = getIntent().getStringExtra("image");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(image);

        GlideApp.with(this).load(storageReference).into(imgViewWin);
    }
}

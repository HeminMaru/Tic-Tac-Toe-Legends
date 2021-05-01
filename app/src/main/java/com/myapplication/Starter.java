package com.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Starter extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        getSupportActionBar().hide();
        MediaPlayer bgAudio = MediaPlayer.create(this , R.raw.bgmusic);
        Handler handler = new Handler();
        Runnable myRunnable = () -> {
            bgAudio.setLooping(true);
            bgAudio.start();
            Intent intent = new Intent(getApplicationContext(),LoginPage.class);
            startActivity(intent);
            finish();
        };
        handler.postDelayed(myRunnable, 3000);

    }

}
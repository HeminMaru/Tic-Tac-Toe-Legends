package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {
    Intent intent;
    TextView usernameDisplay;

    public  void passnplay(View view) {
        intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("passnplay",true);
        startActivity(intent);
    }
    public  void playwithai(View view) {
        intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("passnplay",false);
        startActivity(intent);
    }
    public void profileSetting(View view) {
        intent = new Intent(getApplicationContext(),profileSettings.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getSupportActionBar().hide();
        usernameDisplay = findViewById(R.id.Username_display_TextView);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usernameDisplay.setText(user.getDisplayName());

    }

}
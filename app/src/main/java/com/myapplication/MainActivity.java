package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
    Random r = new Random();
    int ActivePlayer = 0;     //0-Red ; 1-Black or 1-AI ; 9-Empty
    int[] gameStat = {9, 9, 9, 9, 9, 9, 9, 9, 9};
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    boolean gameOn = true;
    int lastMove;
    int ai_id;

    int Occupiedposition;
    ImageView counter;
    MediaPlayer clicksound;
    boolean passnplay;
    boolean playerplayed = false;
    long lastToast1Time = 0;
    long lastToast2Time = 0;
    long lastmoveTime = 0;


    public void dropIn(View view) {
        clicksound = MediaPlayer.create(this, R.raw.clicks9);
        clicksound.start();
        long now1 = System.currentTimeMillis();
        if(lastmoveTime+1000 < now1) {
            counter = (ImageView) view;
            int position = Integer.parseInt(counter.getTag().toString());
            lastMove = position;
            if (gameStat[position] == 9 && gameOn) {
                playerplayed = true;
                counter.setTranslationY(-1500);
                gameStat[position] = ActivePlayer;
                Occupiedposition++;
                if (ActivePlayer == 0) {
                    counter.setImageResource(R.drawable.red);
                    ActivePlayer = 1;
                } else {
                    counter.setImageResource(R.drawable.black);
                    ActivePlayer = 0;
                }
                counter.animate().rotation(3600).setDuration(400);
                counter.animate().translationY(0).setDuration(400);
                iswin(view);
                if (Occupiedposition == 9) {
                    gameOn = false;
                }
            } else {
                long now2 = System.currentTimeMillis();
                if (lastToast1Time + 1000 < now2) {
                    if (gameStat[position] != 9 && gameOn) {
                        Toast.makeText(this, "This space is already Filled.", 1000).show();
                        playerplayed = false;
                    } else {
                        Toast.makeText(this, "Game has ended,Press Play Again to start new Game", 1000).show();
                        playerplayed = false;
                    }
                    lastToast1Time = now2;
                }
            }
            if (!passnplay && gameOn && playerplayed) {
                Handler handler = new Handler();
                Runnable myRunnable = () -> computer_move(view);
                handler.postDelayed(myRunnable, 1000);
            }
            playerplayed = false;
            lastmoveTime = now1;
        } else {
            long now3 = System.currentTimeMillis();
            if (lastToast2Time + 1000 < now3) {
                Toast.makeText(this, "Please wait for your turn to come", 1000).show();
                lastToast2Time = now3;
            }
        }
    }

    public void iswin(View view) {
        for (int[] winPosition : winPositions) {
            if (gameStat[winPosition[0]] == gameStat[winPosition[1]] && gameStat[winPosition[1]] == gameStat[winPosition[2]] && gameStat[winPosition[0]] != 9) {
                gameOn = false;
                MediaPlayer winsound = MediaPlayer.create(this, R.raw.winsound);
                winsound.start();
                ImageView win = (ImageView) findViewById(R.id.winpng);
                win.animate().scaleX( 0.7f).setDuration(300);
                win.animate().scaleY( 0.7f).setDuration(300);
                if (ActivePlayer == 1)
                    Toast.makeText(this, "Red is the Winner!!!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Black is the winner!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void computer_move(View view) {
        ai_id = (r.nextInt(8));
        while (gameStat[ai_id] != 9){
            ai_id = (r.nextInt(8));
        }
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        counter = (ImageView) gridLayout.getChildAt(ai_id);
        counter.setImageDrawable(null);
        counter.setTranslationY(-1500);
        counter.setImageResource(R.drawable.black);
        gameStat[ai_id] = 1;
        ActivePlayer = 0;
        counter.animate().rotation(3600).setDuration(400);
        counter.animate().translationY(0).setDuration(400);
        iswin(view);
    }

    public void playagain(View view) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView counter = (ImageView) gridLayout.getChildAt(i);
            counter.setImageDrawable(null);
            gameStat[i] = 9;
        }
        gameOn = true;
        ActivePlayer = 0;
        Occupiedposition = 0;
        ImageView win = (ImageView) findViewById(R.id.winpng);
        win.animate().scaleX(0).setDuration(300);
        win.animate().scaleY(0).setDuration(300);
    }

    public void Undo(View view) {
        clicksound = MediaPlayer.create(this, R.raw.clicks9);
        clicksound.start();
        if(Occupiedposition == 0){
            Toast.makeText(this, "You haven't Yet  played, to undo your move.", Toast.LENGTH_SHORT).show();
        } else {
            if (gameOn) {
                if (ActivePlayer == 0) {
                    counter.setImageResource(R.drawable.black);
                    ActivePlayer = 1;
                } else {
                    counter.setImageResource(R.drawable.red);
                    ActivePlayer = 0;
                }
                GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
                ImageView counter = (ImageView) gridLayout.getChildAt(lastMove);
                counter.setImageDrawable(null);
                counter = (ImageView) gridLayout.getChildAt(ai_id);
                counter.setImageDrawable(null);
                gameStat[lastMove] = 9;
                gameStat[ai_id] = 9;
                if (!passnplay) ActivePlayer = 0;
            } else {
                Toast.makeText(this, "Game has already ended! Press Play Again to start new Game", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Quit(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        passnplay = intent.getBooleanExtra("passnplay",true);
    }
}
package com.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.media.Image;
import android.media.MediaParser;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    int ActivePlayer = 0;     //0-Red ; 1-Black ; 9-Empty
    int[] gameStat={9,9,9,9,9,9,9,9,9};
    int[][] winPositions={{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    boolean gameOn= true;
    int lastMove;
    int Occupiedposition;
    ImageView counter;
    MediaPlayer clicksound;
    public void dropIn (View view) {
            clicksound = MediaPlayer.create(this , R.raw.clicks9);
            clicksound.start();
            counter = (ImageView) view;
            int position = Integer.parseInt(counter.getTag().toString());
            lastMove = position;
            if (gameStat[position] == 9 && gameOn) {
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
                for (int[] winPosition : winPositions) {
                    if (gameStat[winPosition[0]] == gameStat[winPosition[1]] && gameStat[winPosition[1]] == gameStat[winPosition[2]] && gameStat[winPosition[0]] != 9) {
                        gameOn = false;
                        MediaPlayer winsound = MediaPlayer.create(this , R.raw.winsound);
                        winsound.start();
                        ImageView win = (ImageView) findViewById(R.id.winpng);
                        win.animate().scaleX((float) 0.7f).setDuration(300);
                        win.animate().scaleY((float) 0.7f).setDuration(300);
                        if (ActivePlayer == 1)
                            Toast.makeText(this, "Red is the Winner!!!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this, "Black is the winner!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                if(Occupiedposition == 9) {
                    gameOn = false;
                }
            }
            else if(gameStat[position] != 9 && gameOn){
                Toast.makeText(this, "This space is already Filled.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Game has ended,Press Play Again to start new Game", Toast.LENGTH_SHORT).show();
            }
        }
        public void playagain(View view) {
            GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
            for(int i = 0; i < gridLayout.getChildCount() ; i++) {
                ImageView counter = (ImageView) gridLayout.getChildAt(i);
                counter.setImageDrawable(null);
                gameStat[i]=9;
            }
            gameOn = true;
            ActivePlayer = 0;
            Occupiedposition = 0;
            ImageView win = (ImageView) findViewById(R.id.winpng);
            win.animate().scaleX(0).setDuration(300);
            win.animate().scaleY(0).setDuration(300);
        }
        public void Undo(View view) {
            clicksound = MediaPlayer.create(this , R.raw.clicks9);
            clicksound.start();
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
                gameStat[lastMove] = 9;
            }
            else {
                Toast.makeText(this, "Game has already ended! Press Play Again to start new Game", Toast.LENGTH_SHORT).show();
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
        }
}
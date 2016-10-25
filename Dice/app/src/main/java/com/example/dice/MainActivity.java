package com.example.dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private int userOverallScore = 0;
    private int userTurnScore = 0;
    private int compOverallScore = 0;
    private int compTurnScore = 0;
    ImageView image;
    int randomNum = 0;
    TextView text;
    Button roll, reset, hold;
    int flag = 0;

    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            roll.setEnabled(false);
            hold.setEnabled(false);
            roll_dice();
            if (randomNum != 1) {
                updateCompScore();
                if (flag <= 5 && compOverallScore < 100) {
                    flag++;
                    timerHandler.postDelayed(this, 1000);
                } else {
                    if (compOverallScore >= 100) {
                        text.setText("Computer Wins..!!");
                        roll.setEnabled(false);
                        hold.setEnabled(false);
                    } else {
                        text.setText("Your Score: " + userOverallScore + " Computer Score: " + compOverallScore);
                        roll.setEnabled(true);
                        hold.setEnabled(true);
                    }
                }
            } else {
                text.setText("Your Score: " + userOverallScore + " Computer Score: " + compOverallScore);
                roll.setEnabled(true);
                hold.setEnabled(true);
                timerHandler.removeCallbacks(timerRunnable);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (TextView) findViewById(R.id.edit_message);
        image = (ImageView) findViewById(R.id.imageView);

        roll = (Button) findViewById(R.id.roll);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll_dice();
                if (randomNum != 1)
                    userTurnScore = userTurnScore + randomNum;
                else {
                    userTurnScore = 0;
                    flag = 0;
                    timerHandler.postDelayed(timerRunnable, 1000);
                }
                text.setText("Your Score: " + userOverallScore + " Computer Score: " + compOverallScore + " Your turn Score: " + userTurnScore);
            }
        });
        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTurnScore = 0;
                userOverallScore = 0;
                compTurnScore = 0;
                compOverallScore = 0;
                text.setText("Your Score: 0 Computer Score:0");
            }
        });

        hold = (Button) findViewById(R.id.hold);
        hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userOverallScore = userOverallScore + userTurnScore;
                userTurnScore = 0;
                if (userOverallScore == 100) {
                    text.setText("You Win..!!");
                    roll.setEnabled(false);
                    hold.setEnabled(false);
                } else {
                    text.setText("Your Score: " + userOverallScore + " Computer Score: " + compOverallScore);
                    flag = 0;
                    timerHandler.postDelayed(timerRunnable, 1000);
                }
            }
        });
    }

    private void roll_dice() {
        Random rand = new Random();
        randomNum = rand.nextInt((6 - 1) + 1) + 1;
        switch (randomNum) {
            case 2:
                image.setImageResource(R.drawable.dice2);
                break;
            case 3:
                image.setImageResource(R.drawable.dice3);
                break;
            case 4:
                image.setImageResource(R.drawable.dice4);
                break;
            case 5:
                image.setImageResource(R.drawable.dice5);
                break;
            case 6:
                image.setImageResource(R.drawable.dice6);
                break;
            default:
                image.setImageResource(R.drawable.dice1);
        }
    }

    private void updateCompScore() {
        compTurnScore = randomNum;
        compOverallScore = compOverallScore + compTurnScore;
        text.setText("Your Score: " + userOverallScore + " Computer Score: " + compOverallScore + " Comp turn Score: " + compTurnScore);
    }
}

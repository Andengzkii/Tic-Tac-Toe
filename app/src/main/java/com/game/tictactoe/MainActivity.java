package com.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean playerOneActive;
    private TextView playerOneScore, playerTwoScore, playerStatus, timerTextView;
    private final Button[] buttons = new Button[9];
    private Button reset, playagain, viewRecentScore, viewLeaderboard;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    int rounds;
    private int playerOneScoreCount, playerTwoScoreCount;
    private String recentScore;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 1 minute
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = findViewById(R.id.score_Player1);
        playerTwoScore = findViewById(R.id.score_Player2);
        playerStatus = findViewById(R.id.textStatus);
        reset = findViewById(R.id.btn_reset);
        playagain = findViewById(R.id.btn_play_again);
        viewRecentScore = findViewById(R.id.btn_view_recent_score);

        timerTextView = findViewById(R.id.timer_text_view);

        buttons[0] = findViewById(R.id.btn0);
        buttons[1] = findViewById(R.id.btn1);
        buttons[2] = findViewById(R.id.btn2);
        buttons[3] = findViewById(R.id.btn3);
        buttons[4] = findViewById(R.id.btn4);
        buttons[5] = findViewById(R.id.btn5);
        buttons[6] = findViewById(R.id.btn6);
        buttons[7] = findViewById(R.id.btn7);
        buttons[8] = findViewById(R.id.btn8);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(this);
        }

        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        playerOneActive = true;
        rounds = 0;

        sharedPreferences = getSharedPreferences("Leaderboard", MODE_PRIVATE);

        startTimer();

        reset.setOnClickListener(view -> {
            playAgain();
            playerOneScoreCount = 0;
            playerTwoScoreCount = 0;
            updatePlayerScore();
            resetTimer();
            startTimer();
        });

        playagain.setOnClickListener(view -> {
            playAgain();
            resetTimer();
            startTimer();
        });

        viewRecentScore.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RecentScoreActivity.class);
            intent.putExtra("RECENT_SCORE", recentScore);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View view) {
        if (!((Button) view).getText().toString().equals("")) {
            return;
        } else if (checkWinner()) {
            return;
        }

        String buttonID = view.getResources().getResourceEntryName(view.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length()));

        if (playerOneActive) {
            ((Button) view).setText("X");
            ((Button) view).setTextColor(Color.parseColor("#ffc34a"));
            gameState[gameStatePointer] = 0;
        } else {
            ((Button) view).setText("O");
            ((Button) view).setTextColor(Color.parseColor("#70fc3a"));
            gameState[gameStatePointer] = 1;
        }
        rounds++;

        if (checkWinner()) {
            if (playerOneActive) {
                playerOneScoreCount++;
                updatePlayerScore();
                playerStatus.setText("Player-1 has won");
                recentScore = "Player-1 won";
            } else {
                playerTwoScoreCount++;
                updatePlayerScore();
                playerStatus.setText("Player-2 has won");
                recentScore = "Player-2 won";
            }
            resetTimer();
            saveScore();
        } else if (rounds == 9) {
            playerStatus.setText("No Winner");
            recentScore = "Draw";
            resetTimer();
        } else {
            playerOneActive = !playerOneActive;
            resetTimer(); // Reset timer on player move
        }
    }

    private boolean checkWinner() {
        boolean winnerResults = false;
        for (int[] winningPositions : winningPositions) {
            if (gameState[winningPositions[0]] == gameState[winningPositions[1]] &&
                    gameState[winningPositions[1]] == gameState[winningPositions[2]] &&
                    gameState[winningPositions[0]] != 2) {
                winnerResults = true;
            }
        }
        return winnerResults;
    }

    private void playAgain() {
        rounds = 0;
        playerOneActive = true;
        for (int i = 0; i < buttons.length; i++) {
            gameState[i] = 2;
            buttons[i].setText("");
        }
        playerStatus.setText("Status");
    }

    private void updatePlayerScore() {
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                playerStatus.setText("Time's up!");
                // Handle what happens when timer finishes (e.g., end game, reset timer)
            }
        }.start();
    }

    private void resetTimer() {
        countDownTimer.cancel();
        timeLeftInMillis = 60000; // Reset to 1 minute
        updateTimer();
        countDownTimer.start(); // Start timer again after reset
    }

    private void updateTimer() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format("%02d:%02d", seconds / 60, seconds % 60);
        timerTextView.setText(timeLeftFormatted);
    }

    private void saveScore() {
        int totalScore = playerOneScoreCount + playerTwoScoreCount;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("TOTAL_SCORE", totalScore);
        editor.apply();
    }
}

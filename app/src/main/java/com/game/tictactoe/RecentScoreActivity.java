package com.game.tictactoe;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecentScoreActivity extends AppCompatActivity {
    private TextView recentScoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_score);

        recentScoreTextView = findViewById(R.id.recent_score);

        // Get the data from the Intent
        String recentScore = getIntent().getStringExtra("RECENT_SCORE");
        recentScoreTextView.setText(recentScore);
    }
}
package com.game.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreferencesHelper {

    private static final String PREF_NAME = "LeaderboardPrefs";
    private static final String SCORE_KEY = "score_list";

    public static void saveScore(Context context, String score) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<String> scoreList = getScores(context);
        scoreList.add(score);
        sharedPreferences.edit().putString(SCORE_KEY, TextUtils.join(",", scoreList)).apply();
    }

    public static List<String> getScores(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String scoresString = sharedPreferences.getString(SCORE_KEY, "");
        String[] scoreArray = scoresString.split(",");
        return new ArrayList<>(Arrays.asList(scoreArray));
    }
}

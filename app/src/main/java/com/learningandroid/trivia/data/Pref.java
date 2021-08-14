package com.learningandroid.trivia.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    public static final String HIGH_SCORE_ID = "high_score_id";
    public static final String QUESTION_NUMBER = "question_number";
    public static final String SCORE = "Score";
    private SharedPreferences preferences;

    public Pref(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveHighestScore(int score) {
        int lastscore = preferences.getInt(HIGH_SCORE_ID, 0);
        if(score > lastscore) {
            preferences.edit().putInt(HIGH_SCORE_ID , score).apply();
        }
    }

    public int getHighestScore() {
        return preferences.getInt(HIGH_SCORE_ID , 0);
    }

    public void saveQuestionNumber(int questionNumber) {
        preferences.edit().putInt(QUESTION_NUMBER, questionNumber).apply();
    }

    public int getQuestionNumber() {
        return preferences.getInt(QUESTION_NUMBER , 1);
    }

    public void saveScore(int score) {
        preferences.edit().putInt(SCORE, score).apply();
    }

    public int  getScore() {
        return preferences.getInt(SCORE , 0);
    }
}

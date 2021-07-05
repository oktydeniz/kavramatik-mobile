package com.kavramatik.kavramatik.util;

import android.content.Context;

public class Scores {

    public static final int MATCH_SCORE = 65;
    public static final int COLOR_SCORE = 5;
    public static final int DIMENSION_SCORE = 5;
    public static final int DIRECTION_SCORE = 5;
    public static final int EMOTION_SCORE = 5;
    public static final int NUMBER_SCORE = 5;
    public static final int OPPOSITE_SCORE = 5;
    public static final int QUANTITY_SCORE = 5;
    public static final int SENSE_SCORE = 5;
    public static final int SHAPE_SCORE = 5;
    public static final int STT_SCORE = 65;


    public static void updateScore(Context context, int newScore) {
        int score = SharedPreferencesManager.getScore(context);
        score += newScore;
        SharedPreferencesManager.setScore(context, score);
    }

}

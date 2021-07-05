package com.kavramatik.kavramatik.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTINGS = "com.kavramatik.kavramatik";

    private static final String isFirstTime = "isFirstTime";
    private static final String userID = "userID";
    public static final int defaultID = -100;
    public static final int defaultScore = 0;
    private static final String userEmail = "userEmail";
    private static final String userName = "userName";
    private static final String score = "score";
    public static final String nullValue = "----";
    public static final String permission = "permission";
    public static final String educationDialog = "educationAssistant";
    public static final String sttIsFirst = "sttIsFirst";
    public static final String isLoadingFirst = "isLoadingFirst";
    public static final String isMatchFirst = "isMatchFirst";

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getUserEmail(Context context) {
        return getSharedPreference(context).getString(userEmail, nullValue);
    }

    public static String getUserName(Context context) {
        return getSharedPreference(context).getString(userName, nullValue);
    }

    public static int getScore(Context context) {
        return getSharedPreference(context).getInt(score, defaultScore);
    }

    public static int getUserId(Context context) {
        return getSharedPreference(context).getInt(userID, defaultID);

    }

    public static void setUserEmail(Context context, String mail) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(userEmail, mail);
        editor.apply();
    }

    public static void setUserName(Context context, String name) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(userName, name);
        editor.apply();
    }

    public static void setScore(Context context, int lastScore) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(score, lastScore);
        editor.apply();
    }

    public static void setUserID(Context context, int id) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(userID, id);
        editor.apply();
    }

    public static void setIsFirstTime(Context context, boolean isFirst) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(isFirstTime, isFirst);
        editor.apply();
    }

    public static boolean getIsFirstTime(Context context) {
        return getSharedPreference(context).getBoolean(isFirstTime, true);
    }

    public static boolean getPermissionResult(Context context) {
        return getSharedPreference(context).getBoolean(permission, false);
    }

    public static void setPermissionResult(Context context, boolean prm) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(permission, prm);
        editor.apply();
    }

    public static void setEducationAssistantD(Context context, boolean prm) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(educationDialog, prm);
        editor.apply();
    }

    public static boolean getEducationAssistantD(Context context) {
        return getSharedPreference(context).getBoolean(educationDialog, true);
    }

    public static void setSttIsFirst(Context context, boolean prm) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(sttIsFirst, prm);
        editor.apply();
    }

    public static boolean getSttIsFirst(Context context) {
        return getSharedPreference(context).getBoolean(sttIsFirst, true);
    }

    public static boolean getLoadingFirst(Context context) {
        return getSharedPreference(context).getBoolean(isLoadingFirst, true);
    }

    public static void setLoadingFirst(Context context, boolean prm) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(isLoadingFirst, prm);
        editor.apply();
    }

    public static boolean getMatchAssistant(Context context) {
        return getSharedPreference(context).getBoolean(isMatchFirst, true);
    }

    public static void setIsMatchAssistant(Context context, boolean prm) {
        final SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(isMatchFirst, prm);
        editor.apply();
    }

}

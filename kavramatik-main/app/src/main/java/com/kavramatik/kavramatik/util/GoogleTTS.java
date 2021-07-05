package com.kavramatik.kavramatik.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class GoogleTTS {

    public static float speedTTS = 0.9f;
    public static float pitchTTS = 0.8f;
    public static int valueSpeak = 65;
    public static int valuePitch = 42;

    public static void getSpeech(String s, Context context, int status, TextToSpeech speech) {
        try {
            if (status == TextToSpeech.SUCCESS) {
                speech.setLanguage(Locale.getDefault());
                Locale locale = new Locale("tr", "TR");
                if (speedTTS >= 0.1f && pitchTTS >= 0.1f) {
                    speech.setPitch(pitchTTS);
                    speech.setSpeechRate(speedTTS);
                }
                int result = speech.setLanguage(locale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    AppAlertDialogs.showAlertDialogForSettings(context);
                } else {
                    speech.speak(s, TextToSpeech.QUEUE_FLUSH, null, "");
                }
            } else {
                AppAlertDialogs.noTTSPacketDialog(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shotDownTTS(TextToSpeech textToSpeech) {
        try {
            if (textToSpeech != null) {
                textToSpeech.shutdown();
                textToSpeech.stop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

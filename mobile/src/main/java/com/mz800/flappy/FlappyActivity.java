package com.mz800.flappy;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class FlappyActivity extends Activity {
    public static final String PREFERENCE_NAME = "flappy.prefs";
    public static final String SCENE_NUMBER = "scene.number";
    public static final String SCROLL_SHIFT = "scroll.shift";
    public static final String OPEN_SCENES = "open.scenes";

    int retrieveSceneNumber() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, 0);
        return p.getInt(SCENE_NUMBER, 0);
    }

    void storeSceneNumber(int scNo) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, 0);
        SharedPreferences.Editor ed = p.edit();
        ed.putInt(SCENE_NUMBER, scNo);
        ed.apply();
    }
}

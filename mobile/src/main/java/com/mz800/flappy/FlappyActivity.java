package com.mz800.flappy;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class FlappyActivity extends Activity {
    private static final String TAG = FlappyActivity.class.getSimpleName();

    public static final String PREFERENCE_NAME = "flappy.prefs";
    public static final String SCENE_NUMBER = "scene.number";
    public static final String SCROLL_SHIFT = "scroll.shift";
    public static final String OPEN_SCENES = "open.scenes";
    public static final String SCORES = "metadata";

    protected SurfaceView view;

    @Override
    protected void onResume() {
        super.onResume();
        Device.init(view);
        loadScoreDetails();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        Device.music.pause();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    void saveScoreDetails() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);

            for (ScreenScore screenScore : ScreenScore.screenScores) {
                screenScore.writeExternal(out);
            }
            out.close();
            String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

            SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = p.edit();
            ed.putString(SCORES, base64);
            ed.commit(); // persist immediately so we are safe
        } catch (IOException e) {
            Log.e(TAG, "Exception when saving score details", e);
            Toast.makeText(this, "Cannot save scores!", Toast.LENGTH_LONG).show();
        }
    }

    void loadScoreDetails() {
        if (ScreenScore.initialized) return;
        ScreenScore.init();
        try {
            SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
            String base64 = p.getString(SCORES, null);

            if (base64 != null) {
                byte[] data = Base64.decode(base64.getBytes(), Base64.DEFAULT);
                ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(data));
                for (ScreenScore screenScore : ScreenScore.screenScores) {
                    screenScore.readExternal(stream);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Exception when loading score details", e);
            Toast.makeText(this, "Cannot load scores!", Toast.LENGTH_LONG).show();
        }
        ScreenScore.initialized = true;
    }

    int retrieveCurrentShift(int minShift) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return p.getInt(SCROLL_SHIFT, minShift);
    }

    void storeScrollShift(int currentShift) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putInt(SCROLL_SHIFT, currentShift);
        ed.apply();
    }

    int retrieveSceneNumber() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return p.getInt(SCENE_NUMBER, 0);
    }

    void storeSceneNumber(int scNo) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putInt(SCENE_NUMBER, scNo);
        ed.apply();
    }

    int retrieveOpenScenes() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return p.getInt(OPEN_SCENES, 0);
    }

    void storeOpenScene(int num) {
        int current = retrieveOpenScenes();
        if (num <= current) return;
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putInt(OPEN_SCENES, num);
        ed.apply();
    }

    int[] loadScore(int scNo) {
        loadScoreDetails();
        int previousScores = 0;
        for (int i = 0; i < scNo - 1; i++) {
            previousScores += ScreenScore.screenScores[i].myBestScore;
        }
        return new int[]{previousScores,
                (scNo - 1) % 5 == 0                     // for every fifth scene
                        ? 5 :                         // give 5 lives
                        ScreenScore.screenScores[scNo - 2].myLives}; // or the previous num of lives
    }

    void storeScore(int scNo, int score, int lives) {
        if (ScreenScore.screenScores[scNo].myBestScore >= score) return;
        ScreenScore.screenScores[scNo].myBestScore = score;
        ScreenScore.screenScores[scNo].myLives = lives;
        saveScoreDetails();
    }
}

package com.mz800.flappy;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.mz800.flappy.score.BestScoreService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.UUID;

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
    public static final String PLAYER_NAME = "player.name";
    public static final String PLAYER_ID = "player.id";

    protected SurfaceView view;
    protected static BestScoreService bestScoreService;

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

    /**
     * Stores the open scene only if the given number is bigger than already stored!
     *
     * @param num
     */
    void storeOpenScene(int num) {
        int current = retrieveOpenScenes();
        if (num <= current) return;
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putInt(OPEN_SCENES, num);
        ed.apply();
    }

    String retrievePlayerName() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String name = p.getString(PLAYER_NAME, null);
        if (name == null) {
            name = getDefaultPlayerName();
            storePlayerName(name);
        }
        return name;
    }

    private static String[] codeNames = {
            "No Name", "No Name", "Cupcake", "Donut", "Eclair", "Eclair", "Eclair", "Froyo", "Gingerbread", "Gingerbread",
            "Honeycomb", "Honeycomb", "Honeycomb", "Ice Cream Sandwich", "Ice Cream Sandwich", "Jelly Bean", "Jelly Bean", "Jelly Bean", "KitKat", "N/A",
            "Lollipop", "Lollipop", "Marshmallow"};

    private String getShortUUID() {
        Random r = new Random();
        byte[] buf = new byte[6];
        r.nextBytes(buf);

        return Base64.encodeToString(buf, Base64.NO_PADDING);
    }

    private String getDeviceTypeName() {
        String s;
        try {
            int apiLevel = Build.VERSION.SDK_INT;
            String codeName = apiLevel >= codeNames.length ? "Future" : codeNames[apiLevel - 1];
            s = Build.MANUFACTURER.substring(0, 1).toUpperCase() + Build.MANUFACTURER.substring(1) // make first letter upper case
                    + " " + Build.MODEL + " on " + codeName + " " + Build.VERSION.RELEASE + " (" + apiLevel + ") ";
        } catch (Exception e) {
            s = "";
        }
        s = s + Device.displayWidth + "x" + Device.displayHeight + " " + getShortUUID();
        return s;
    }

    private String getDefaultPlayerName() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                Uri CONTENT_URI = ContactsContract.Profile.CONTENT_URI;
                String DISPLAY_NAME = ContactsContract.Profile.DISPLAY_NAME;
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
                if (cursor.moveToFirst()) { // is the at least one account?
                    return cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception when reading contact profile", e);
                return getDeviceTypeName();
            }
        } else {
            // pre-ICS device
            return getDeviceTypeName();
        }
        // no account found
        return getDeviceTypeName();
    }

    void storePlayerName(String playerName) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putString(PLAYER_NAME, playerName);
        ed.apply();
    }

    /**
     * Generates new UUID player ID and stores it if there is no generated yet.
     *
     * @return
     */
    String retrievePlayerId() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String playerId = p.getString(PLAYER_ID, null);
        if (playerId == null) {
            playerId = UUID.randomUUID().toString();
            storePlayerId(playerId);
        }
        return playerId;
    }

    void storePlayerId(String playerId) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putString(PLAYER_ID, playerId);
        ed.apply();
    }

    /**
     * Returns two-ints-array where first int is the sum of scores of the scene and second is the number of lives.
     *
     * @param scNo
     * @return
     */
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

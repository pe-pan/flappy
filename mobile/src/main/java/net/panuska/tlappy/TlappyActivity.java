package net.panuska.tlappy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.panuska.tlappy.mobile.R;

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
 * Android version by Petr Panuska, 2016.
 */
public class TlappyActivity extends Activity {
    private static final String TAG = TlappyActivity.class.getSimpleName();

    public static final String PREFERENCE_NAME = "tlappy.prefs";
    public static final String SCENE_NUMBER = "scene.number";
    public static final String SCROLL_SHIFT = "scroll.shift";
    public static final String OPEN_SCENES = "open.scenes";
    public static final String SCORES = "metadata";
    public static final String PLAYER_NAME = "player.name";
    public static final String PLAYER_ID = "player.id";
    public static final String HAS_RECORD = "has.record";
    public static final String HAS_CHANGED_NAME = "has.changed.name";

    protected SurfaceView view;
    protected static BestScoreService bestScoreService;

    @Override
    protected void onResume() {
        super.onResume();
        Device.init(view);
        loadScoreDetails();
        if (bestScoreService == null) {
            bestScoreService = new BestScoreService(this);
        }
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

    public String retrievePlayerName() {
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
            "Lollipop", "Lollipop", "Marshmallow", "Nougat", "Nougat"};

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
            String codeName = apiLevel > codeNames.length ? "Future" : codeNames[apiLevel - 1];
            s = Build.MANUFACTURER.substring(0, 1).toUpperCase() + Build.MANUFACTURER.substring(1) // make first letter upper case
                    + " " + Build.MODEL + " on " + codeName + " " + Build.VERSION.RELEASE + " (" + apiLevel + ") ";
        } catch (Exception e) {
            s = "";
        }
        s = s + Device.displayWidth + "x" + Device.displayHeight + " " + getShortUUID();
        return s;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected String getOwnerDisplayName() {
        try {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) { // is the at least one account?
                return cursor.getString(cursor.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception when reading contact profile", e);
        }
        return getDeviceTypeName(); // no account found or an exception when reading contacts
    }

    private String getDefaultPlayerName() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return getOwnerDisplayName();
        } else {
            // pre-ICS device
            return getDeviceTypeName();
        }
    }

    public void storePlayerName(String playerName) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putString(PLAYER_NAME, playerName);
        ed.apply();
        storeHasChangedName(true);
        if (hasRecord()) {
            bestScoreService.putPlayer(retrievePlayerId(), playerName);
        }
    }

    private static String playerId = null;
    /**
     * Generates new UUID player ID and stores it if there is no generated yet.
     *
     * @return
     */
    public String retrievePlayerId() {
        if (playerId != null) return playerId;
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String playerId = p.getString(PLAYER_ID, null);
        if (playerId == null) {
            playerId = UUID.randomUUID().toString();
            storePlayerId(playerId);
        }
        TlappyActivity.playerId = playerId;
        return playerId;
    }

    private void storePlayerId(String playerId) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putString(PLAYER_ID, playerId);
        ed.apply();
    }

    public void storeHasRecord(boolean b) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putBoolean(HAS_RECORD, b);
        ed.apply();
    }

    public boolean hasRecord() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return p.getBoolean(HAS_RECORD, false);
    }

    public void storeHasChangedName(boolean b) {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = p.edit();
        ed.putBoolean(HAS_CHANGED_NAME, b);
        ed.apply();
    }

    public boolean hasChangedName() {
        SharedPreferences p = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return p.getBoolean(HAS_CHANGED_NAME, true);  // the very first time, it should send the user name
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
        int lives = getLives(scNo - 1);
        return new int[]{previousScores, lives};
    }

    /**
     * How many lives this scene should start with?
     * @param scNo
     * @return
     */
    int getLives(int scNo) {
        int lives = scNo % 5 == 0                           // for every fifth scene
                ? 5 :                                       // give 5 lives
                ScreenScore.screenScores[scNo - 1].myLives; // or the previous num of lives
        if (lives <= 0) lives = 1;                          // if the previous game was not played (user typed a password), give it 1 life
        return lives;
    }

    /**
     * Does not store scores if a better one exists already.
     * @param scNo
     * @param score
     * @param lives
     * @return true if score saved; false if not saved (as there is a better one already)
     */
    boolean storeScore(int scNo, int score, int lives) {
        ScreenScore myScore = ScreenScore.screenScores[scNo];
        if (myScore.myBestScore > score) return false;
        if (myScore.myBestScore == score && myScore.myLives >= lives) return false;
        ScreenScore.screenScores[scNo].myBestScore = score;
        ScreenScore.screenScores[scNo].myLives = lives;
        saveScoreDetails();
        bestScoreService.addBestScore(scNo, new BestScore(score, lives, ScreenScore.screenScores[scNo].overallAttempts, System.currentTimeMillis(), retrievePlayerId()));
        return true;
    }

    void increaseAttempts(int scNo) {
        loadScoreDetails();
        ScreenScore.screenScores[scNo].overallAttempts++;
        saveScoreDetails();
    }

    /**
     * Show message and do something once the user clicks OK.
     * @param message
     * @param listener
     */
    void showMessage(String message, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.ok), listener);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_message, null);
        TextView text = (TextView) view.findViewById(R.id.message);
        text.setText(message);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Show message; do nothing when the user clicks OK.
     * @param message
     */
    void showMessage(String message) {
        showMessage(message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
    }
}

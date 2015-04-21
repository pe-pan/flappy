package com.mz800.flappy;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.mz800.core.R;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Music {
    private static final String TAG = Music.class.getSimpleName();

    private static Music me;

    static void init(Context c) {
        Log.d(TAG, "Init");
        me = new Music(c);
    }

    static Music getInstance() {
        return me;
    }

    static Music getInstance(Context c) {
        if (me == null) init(c);
        return getInstance();
    }

    private boolean soundOn = true;

    private MediaPlayer player;

    private Music(Context c) {
        player = MediaPlayer.create(c, R.raw.flappy_song);
        player.setLooping(true);
    }

    void disableMusic() {
        soundOn = false;
    }

    void start() {
        Log.d(TAG, "Start");
        if (me == null) return;
        if (soundOn) {
            player.start();
        }
    }

    void stop() {
        Log.d(TAG, "Stop");
        if (me == null) return;
        if (soundOn) {
            player.pause();
            player.seekTo(0);
        }
    }

    void pause() {
        Log.d(TAG, "Pause");
        if (me == null) return;
        if (soundOn) {
            player.pause();
        }
    }

    void destroy() {
        Log.d(TAG, "Destroy");
        if (me == null) return;
        if (soundOn) {
            player.release();
        }
        me = null;
    }
}

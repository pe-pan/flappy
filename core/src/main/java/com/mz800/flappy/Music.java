package com.mz800.flappy;

import android.content.Context;
import android.media.MediaPlayer;

import com.mz800.core.R;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Music {

    private static Music me;

    static void init(Context c) {
        me = new Music(c);
    }

    static Music getInstance() {
        return me;
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
        if (soundOn) {
            player.seekTo(0);
            player.start();
        }
    }

    void stop() {
        if (soundOn) {
            player.pause();
        }
    }
}

package com.mz800.flappy;

import android.util.Log;
import android.view.SurfaceView;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class Device {
    private static final String TAG = Device.class.getSimpleName();
    public static Keyboard keyboard;
    public static VRAM vram;
    public static Music music;

    public static boolean initialized = false;
    public static void init (SurfaceView view) {
        if (initialized) return;
        Log.d(TAG, "Init");
        keyboard = Keyboard.getInstance();
        vram = VRAM.getInstance();
        vram.createWindow(view);
        music = Music.getInstance(view.getContext());
        initialized = true;
    }
}

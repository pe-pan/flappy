package com.mz800.flappy;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;

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

    public static int displayWidth;
    public static int displayHeight;

    public static boolean initialized = false;
    public static void init (SurfaceView view) {
        if (initialized) return;
        Log.d(TAG, "Init");
        keyboard = Keyboard.getInstance();
        vram = VRAM.getInstance();
        vram.createWindow(view);
        music = Music.getInstance(view.getContext());
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        displayWidth = display.getWidth();
        displayHeight = display.getHeight();
        if (displayHeight > displayWidth) {
            Log.d(TAG, "Swapping display width and height "+displayWidth+", "+displayHeight);
            displayWidth = displayWidth + displayHeight;
            displayHeight = displayWidth - displayHeight;
            displayWidth = displayWidth - displayHeight;
        }
        initialized = true;
    }
}

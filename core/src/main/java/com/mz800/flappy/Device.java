package com.mz800.flappy;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.mz800.core.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class Device {
    private static final String TAG = Device.class.getSimpleName();
    static byte[] memory = new byte[65536];
    public static Keyboard keyboard;
    public static VRAM vram;
    public static Music music;

    public static int displayWidth;
    public static int displayHeight;

    public static boolean initialized = false;
    public static void init (SurfaceView view) {
        if (initialized) {
            Log.d(TAG, "Device already initialized");
            return;
        }
        Log.d(TAG, "Init");
        initMemory(view);
        Images.load();
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

    private static void initMemory(View view) {
        Log.d(TAG, "Initializing memory");
        try {
            InputStream is = view.getResources().openRawResource(R.raw.flappy);
            int read = 0;
            while (read < 65536) {
                int res = is.read(memory, read, 65536 - read);
                if (res < 0) {
                    break;
                }
                read += res;
            }
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error when loading memory", e);
        }
    }
}

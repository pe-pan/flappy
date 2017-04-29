package net.panuska.tlappy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.SurfaceView;

import net.panuska.tlappy.awt.BufferedImage;
import net.panuska.tlappy.awt.Color;
import net.panuska.tlappy.mobile.R;

import static net.panuska.tlappy.Device.vram;
import static net.panuska.tlappy.FullscreenActivity.DOWN;
import static net.panuska.tlappy.FullscreenActivity.LEFT;
import static net.panuska.tlappy.FullscreenActivity.RIGHT;
import static net.panuska.tlappy.FullscreenActivity.SPACE_BAR;
import static net.panuska.tlappy.FullscreenActivity.UP;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2017.
 */
public class CustomControlsActivity extends TlappyActivity {
    private static final String TAG = CustomControlsActivity.class.getSimpleName();

    private SparseIntArray tempKeyboardMap;   // used for custom key recording
    static final SparseIntArray keyboardMap = new SparseIntArray();
    private AsyncTask<Void, Void, Void> game;

    volatile int state;
    volatile int index;
    volatile int maxIndex;
    volatile String message, systemMessage;

    private static final int[] STATES = {LEFT, RIGHT, UP, DOWN, SPACE_BAR, -1};
    private static final BufferedImage[][] animation = {
            {Images.chickenLeft, Images.chickenLeft1},
            {Images.chickenRight, Images.chickenRight1},
            {Images.chickenUp, Images.chickenUp1, Images.chickenUp, Images.chickenUp2},
            {Images.chickenDown, Images.chickenDown1, Images.chickenDown, Images.chickenDown2},
            {Images.chickenDownFire1, Images.chickenDownFire2, Images.chickenDownFire1, Images.chickenDownFire2, Images.chickenDown, Images.chickenDown},
            {Images.chickenJump1, Images.chickenJump2, Images.chickenDown, Images.chickenDown, Images.chickenDown, Images.chickenDown}
    };
    private static final int[] MESSAGES = {
            R.string.move_left,
            R.string.move_right,
            R.string.move_up,
            R.string.move_down,
            R.string.throw_mushroom,
            R.string.controls_saved};

    volatile BufferedImage[] selectedAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customcontrols);

        view = (SurfaceView) findViewById(R.id.customControlsContent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Main.getInstance(view);  //todo hack
        vram.clear();

        index = 0;
        maxIndex = Integer.MAX_VALUE;  // hopefully big enough
        step = 0;
        switchState();  // to init
        systemMessage = null;
        tempKeyboardMap = new SparseIntArray();

        game = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (index = 0; index < maxIndex; index++) {
                    vram.clear();
                    vram.imageNoOfs(20, 10, selectedAnimation[index % selectedAnimation.length]);
                    printMessageInCenter(20, message, Color.YELLOW);
                    printMessageInCenter(22, systemMessage, Color.RED);
                    vram.refresh();
                    sleep(16 * 6);
                }
                finish();
                return null;
            }
        }.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.cancel(true);
        game = null;
    }

    private int step = 0;
    private void switchState() {
        state = STATES[step];
        message = getString(MESSAGES[step]);
        selectedAnimation = animation[step];
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            systemMessage = null;
            if (event.isSystem()) {
                systemMessage = getString(R.string.cant_record);
                return true;
            }
            if (tempKeyboardMap.get(event.getKeyCode(), -1) != -1) {
                systemMessage = getString(R.string.key_in_use);
                return true;
            }
            tempKeyboardMap.put(event.getKeyCode(), state);
            step++;
            switchState();
            if (step == 5) {
                mergeArrays(keyboardMap, tempKeyboardMap);
                maxIndex = index + 12;                          // give it 12 more animation steps and finish
                storeCustomControls(keyboardMap);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    static void initCustomControls() {
        keyboardMap.clear();
        keyboardMap.put(KeyEvent.KEYCODE_DPAD_CENTER, SPACE_BAR);
        keyboardMap.put(KeyEvent.KEYCODE_SPACE, SPACE_BAR);
        keyboardMap.put(KeyEvent.KEYCODE_DPAD_LEFT, LEFT);
        keyboardMap.put(KeyEvent.KEYCODE_DPAD_RIGHT, RIGHT);
        keyboardMap.put(KeyEvent.KEYCODE_DPAD_UP, UP);
        keyboardMap.put(KeyEvent.KEYCODE_DPAD_DOWN, DOWN);
    }

    private void printMessageInCenter(int y, String message, Color color) {
        if (message != null) {
            vram.printText(Constants.SCREEN_WIDTH / Constants.SPRITE_HALF / 2 - message.length() / 2, y, message, color);
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted");
        }
    }

    private static void mergeArrays(SparseIntArray copyTo, SparseIntArray copyFrom) {
        for (int i = 0; i < copyFrom.size(); i++) {
            int key = copyFrom.keyAt(i);
            int value = copyFrom.valueAt(i);
            copyTo.put(key, value);
        }
    }
}

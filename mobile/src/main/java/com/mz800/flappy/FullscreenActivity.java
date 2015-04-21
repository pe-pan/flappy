package com.mz800.flappy;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import static com.mz800.flappy.Device.keyboard;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class FullscreenActivity extends Activity {
    private static final String TAG = FullscreenActivity.class.getSimpleName();

    private View gameMenu;
    private AsyncTask<Void, Void, Void> game;
    SurfaceView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        contentView = (SurfaceView) findViewById(R.id.fullscreen_content);
        gameMenu = findViewById(R.id.gameMenu);

        game = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Main.getInstance(contentView).game();
                } catch (Exception e) {
                    Log.e("Activity", "Exception thrown "+e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        final SurfaceView contentView = (SurfaceView) findViewById(R.id.fullscreen_content);
        Device.init(contentView);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {                         // when getting back from pause
            Device.vram.refresh();
        }
    }

    private static final int MOVE_LIMIT = 5;

    private float initialX;
    private float initialY;
    private float lastX;
    private float lastY;
    boolean isMove;

    static final int SPACE_BAR = 32;
    static final int LEFT = 37;
    static final int UP = 38;
    static final int RIGHT = 39;
    static final int DOWN = 40;

    private int key;
    private boolean spaceMightBeenHit = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = lastX = event.getRawX();
                initialY = lastY = event.getRawY();
                Log.d(TAG, "Action Down");
                spaceMightBeenHit = true;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Action Up");
                if (!isMove) {
                    if (spaceMightBeenHit) {
                        Log.d(TAG, "Space Bar down and up");
                        keyboard.keyDown(SPACE_BAR);
                        keyboard.keyUp(SPACE_BAR);
                    }
                }

                if (key != 0) {
                    Log.d(TAG, "Key up " + key);
                    keyboard.keyUp(key);
                }
                key = 0;
                isMove = false;
                spaceMightBeenHit = false;
                break;
            case MotionEvent.ACTION_POINTER_UP :
            case MotionEvent.ACTION_POINTER_2_UP:
                Log.d(TAG, "Action Pointer Up");
                keyboard.keyUp(SPACE_BAR);
                spaceMightBeenHit = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN :
            case MotionEvent.ACTION_POINTER_2_DOWN:
                Log.d(TAG, "Action Pointer Down");
                keyboard.keyDown(SPACE_BAR);
                spaceMightBeenHit = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float currX = event.getRawX();
                float currY = event.getRawY();

                float diffX = currX - lastX;
                float diffY = currY - lastY;

                if (Math.abs(diffX) > MOVE_LIMIT || Math.abs(diffY) > MOVE_LIMIT) {
                    initialX = lastX;
                    initialY = lastY;
                    lastX = currX;
                    lastY = currY;
                } else {
                    diffX = currX - initialX;
                    diffY = currY - initialY;
                }
                String what = "Nothing";
                int newKey = 0;
                spaceMightBeenHit = true;
                isMove = false;
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > MOVE_LIMIT) {
                        isMove = true;
                        spaceMightBeenHit = false;
                        if (diffX < 0) {
                            what = "Left " + diffX;
                            newKey = LEFT;
                        }
                        if (diffX > 0) {
                            what = "Right " + diffX;
                            newKey = RIGHT;
                        }
                    }
                } else {
                    if (Math.abs(diffY) > MOVE_LIMIT) {
                        isMove = true;
                        spaceMightBeenHit = false;
                        if (diffY < 0) {
                            what = "Up " + diffY;
                            newKey = UP;
                        }
                        if (diffY > 0) {
                            what = "Down " + diffY;
                            newKey = DOWN;
                        }
                    }
                }
                if (key != 0)
                    keyboard.keyUp(key);
                if (newKey != 0)
                    keyboard.keyDown(newKey);
                key = newKey;
                Log.d(TAG, what + " - " + event.getPointerCount());
                break;
            default:
                Log.d(TAG, "Unknown action " + event.getAction());
                spaceMightBeenHit = false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (gameMenu.getVisibility() != View.VISIBLE) {
            pause();
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void restartScene(View view) {
        gameMenu.setVisibility(View.GONE);
        Main.setState(Main.LOOSE_LIFE);
        Device.music.stop();
    }

    public void resumeScene(View view) {
        gameMenu.setVisibility(View.GONE);
        Main.setState(Main.NORMAL_WAIT);
        Device.music.start();
    }

    public void selectScreen(View view) {
    }

    @Override
    public void onBackPressed() {
        if (gameMenu.getVisibility() == View.VISIBLE) {
            closeMenu();
        } else {
            pause();
        }
    }

    private void closeMenu() {
        gameMenu.setVisibility(View.GONE);
        Main.setState(Main.NORMAL_WAIT);
        Device.music.start();
    }

    private void pause() {
        gameMenu.setVisibility(View.VISIBLE);
        Main.setState(Main.KEEP_WAITING);
        Device.music.pause();
    }

    public void playIntro(View view) {
        Main.setState(Main.EXIT_GAME);
        Device.music.stop();
        startActivity(new Intent(this, IntroActivity.class));
        game.cancel(true);
        finish();
    }
}

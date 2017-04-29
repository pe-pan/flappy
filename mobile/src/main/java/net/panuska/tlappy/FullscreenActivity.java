package net.panuska.tlappy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import net.panuska.tlappy.mobile.R;

import static net.panuska.tlappy.Device.keyboard;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class FullscreenActivity extends TlappyActivity {
    private static final String TAG = FullscreenActivity.class.getSimpleName();

    private View gameMenu;
    private AsyncTask<Void, Void, Void> game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        view = (SurfaceView) findViewById(R.id.fullscreen_content);
        gameMenu = findViewById(R.id.gameMenu);

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        loadOrInitCustomControls(CustomControlsActivity.keyboardMap);
        game = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                int scNo = getIntent().getIntExtra(SCENE_NUMBER, 1);
                Main main = Main.getInstance(view);
                main.listener = new Main.Listener() {
                    @Override
                    public int[] gameStarting(int scNo) {
                        return loadScore(scNo);
                    }

                    @Override
                    public void newLife(int scNo) {
                        increaseAttempts(scNo-1);
                    }

                    @Override
                    public void gameFinished(int scNo, int score, int lives, int time) {
                        storeOpenScene(scNo);
                        storeScore(scNo - 1, score, lives);
                        int sceneSpace = getResources().getInteger(R.integer.sceneSpace);
                        int sceneWidth = SelectSceneActivity.getSceneWidth(sceneSpace);
                        int minShift = SelectSceneActivity.getMinShift(sceneWidth);
                        int currentShift = retrieveCurrentShift(minShift);
                        storeScrollShift(currentShift + sceneSpace + sceneWidth);
                    }
                };
                main.game(scNo);
                return null;
            }
        }.execute();
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int translatedKey = CustomControlsActivity.keyboardMap.get(event.getKeyCode(), -1);
        if (translatedKey != -1) { // value found
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                keyboard.keyDown(translatedKey);
                Log.d(TAG, "key down: "+event.getKeyCode());
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                keyboard.keyUp(translatedKey);
                Log.d(TAG, "key up  : "+event.getKeyCode());
            }
        } else Log.d(TAG, "key not found "+event.getKeyCode());
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialX = lastX = event.getRawX();
                initialY = lastY = event.getRawY();
                Log.d(TAG, "Action Down");
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Action Up");
                if (!isMove) {
                    Log.d(TAG, "Space Bar down and up");
                    keyboard.keyDown(SPACE_BAR);
                    keyboard.keyUp(SPACE_BAR);
                }

                if (key != 0) {
                    Log.d(TAG, "Key up " + key);
                    keyboard.keyUp(key);
                }
                key = 0;
                isMove = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
                Log.d(TAG, "Action Pointer Up");
                keyboard.keyUp(SPACE_BAR);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
                Log.d(TAG, "Action Pointer Down");
                keyboard.keyDown(SPACE_BAR);
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
                isMove = false;
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > MOVE_LIMIT) {
                        isMove = true;
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
        Main.setState(Main.EXIT_GAME);
        Device.music.stop();
        startActivity(new Intent(this, SelectSceneActivity.class));
        game.cancel(true);
        finish();
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
        Log.d(TAG, "Closing menu");
        gameMenu.setVisibility(View.GONE);
        Main.setState(Main.NORMAL_WAIT);
        Device.music.start();
    }

    private void pause() {
        Log.d(TAG, "Pausing");
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

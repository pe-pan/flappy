package net.panuska.tlappy;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;

import net.panuska.tlappy.mobile.R;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class BestScoreActivity extends TlappyActivity {
    private static final String TAG = BestScoreActivity.class.getSimpleName();
    private AsyncTask animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestscore);
        view = (SurfaceView) findViewById(R.id.bestscore_content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BestScoreScreen.speed = BestScoreScreen.speed == BestScoreScreen.FAST ? BestScoreScreen.SLOW : BestScoreScreen.FAST;
                Log.d(TAG, "Waiting time set to: "+BestScoreScreen.speed);
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) { // emulate Android TV DPAD key as to finger click
        int keyCode = event.getKeyCode();
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return super.dispatchKeyEvent(event);
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    view.callOnClick();
                    break;
                } else {
                    return super.dispatchKeyEvent(event);
                }
            default: return super.dispatchKeyEvent(event);
        }
        return true;
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        Main.getInstance(view); //todo this should not be necessary -> move vram into BestScore class
        animation = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final int sceneNumber = getIntent().getIntExtra(SCENE_NUMBER, 0);
                bestScoreService.updateBestScoresAndPlayers(sceneNumber);
                Main.resetState(Main.NORMAL_WAIT);
                BestScoreScreen.bestScore(new BestScoreScreen.GiveBestScore() {
                    @Override
                    public BestScore[] callback() {
                        return bestScoreService.getBestScoresFromCache(sceneNumber);
                    }
                });
                return null;
            } }.execute();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        if (animation != null) {
            Log.d(TAG, "Cancelling animation");
            Main.setState(Main.EXIT_GAME);
            animation.cancel(true);
            animation = null;
        }
    }
}

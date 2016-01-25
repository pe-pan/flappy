package com.mz800.flappy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class BestScoreActivity extends FlappyActivity {
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
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        Main.getInstance(view); //todo this should not be necessary -> move vram into BestScore class
        animation = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Main.resetState(Main.NORMAL_WAIT);
                BestScoreScreen.bestScore();
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

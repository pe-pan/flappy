package com.mz800.flappy;

import android.content.Intent;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class IntroActivity extends FlappyActivity {
    private static final String TAG = IntroActivity.class.getSimpleName();

    private View mainMenu;
    private AsyncTask intro;
    private SurfaceView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        contentView = (SurfaceView) findViewById(R.id.intro_content);
        mainMenu = findViewById(R.id.mainMenu);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainMenu.setVisibility(mainMenu.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        intro = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Main.getInstance(contentView);
                    new Intro().intro();
                } catch (Exception e) {
                    Log.e("Activity", "Exception thrown " + e.getMessage(), e);
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        Device.init(contentView);
        loadScoreDetails();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Device.music.start();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {                         // when getting back from pause
            Device.vram.refresh();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        Device.music.pause();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    public void playIntro(View view) {
        mainMenu.setVisibility(View.GONE);
        //todo remove the code below from the production!
        Main.cheating = !Main.cheating;
        Toast.makeText(this, "Cheating "+(Main.cheating ? "ON" : "OFF"), Toast.LENGTH_LONG).show();
        //todo remove the code above from the production!
    }

    public void playGame(View view) {
        startActivity(new Intent(this, FullscreenActivity.class).putExtra(FullscreenActivity.SCENE_NUMBER, retrieveSceneNumber()+1));
        Main.setState(Main.EXIT_GAME);
        mainMenu.setVisibility(View.GONE);
        intro.cancel(true);
        finish();
    }

    public void selectScreen(View view) {
        Main.setState(Main.EXIT_GAME);
        mainMenu.setVisibility(View.GONE);
        Device.music.stop();
        startActivity(new Intent(this, SelectSceneActivity.class));
        intro.cancel(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mainMenu.getVisibility() == View.VISIBLE) {
            mainMenu.setVisibility(View.GONE);
        } else {
            mainMenu.setVisibility(View.VISIBLE);
        }
    }

    public void exitGame(View view) {
        Device.music.destroy();
        super.onBackPressed();
    }
}

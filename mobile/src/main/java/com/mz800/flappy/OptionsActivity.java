package com.mz800.flappy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class OptionsActivity extends FlappyActivity {
    private static final String TAG = OptionsActivity.class.getSimpleName();
    private static final int MAX_PLAYER_NAME_LEN = 64;

    private EditText playerNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        playerNameView = (EditText) findViewById(R.id.playerName);
        playerNameView.setText(retrievePlayerName());
    }

    public void saveOptions(View view) {
        String oldPlayerName = retrievePlayerName();
        String newPlayerName = playerNameView.getText().toString().trim();
        if (newPlayerName.length() > MAX_PLAYER_NAME_LEN) newPlayerName = newPlayerName.substring(0, MAX_PLAYER_NAME_LEN);
        if (!newPlayerName.equals(oldPlayerName)) {
            storePlayerName(newPlayerName);
            //todo remove the code below from the production!
            Main.cheating = !Main.cheating;
            Toast.makeText(this, "Cheating " + (Main.cheating ? "ON" : "OFF"), Toast.LENGTH_LONG).show();
            //todo remove the code above from the production!
        }
        closeOptions(view);
    }

    public void closeOptions(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, IntroActivity.class));
        finish();
        super.onBackPressed();
    }
}

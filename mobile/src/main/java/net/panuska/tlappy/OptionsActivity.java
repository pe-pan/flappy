package net.panuska.tlappy;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.panuska.tlappy.mobile.R;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class OptionsActivity extends TlappyActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = OptionsActivity.class.getSimpleName();
    private static final int MAX_PLAYER_NAME_LEN = 64;

    private EditText playerNameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        playerNameView = (EditText) findViewById(R.id.playerName);
        playerNameView.setText(retrievePlayerName());
        passwordView = (EditText) findViewById(R.id.password);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                showMessage(getString(R.string.give_contact_permission), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(OptionsActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                });
            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 77;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.READ_CONTACTS) &&
                    grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                playerNameView.setText(getOwnerDisplayName());
            }
        }
    }

    public void saveOptions(View view) {
        String oldPlayerName = retrievePlayerName();
        String newPlayerName = playerNameView.getText().toString().trim();
        if (newPlayerName.length() > MAX_PLAYER_NAME_LEN) newPlayerName = newPlayerName.substring(0, MAX_PLAYER_NAME_LEN);
        if (!newPlayerName.equals(oldPlayerName)) {
            storePlayerName(newPlayerName);
        }

        String password = passwordView.getText().toString();
        if (password.length() > 0) {
            int i = Scene.checkPass(password);
            if (i < 0) {
                Toast.makeText(this, R.string.incorrect_password, Toast.LENGTH_LONG).show();
            } else {
                int openScene = retrieveOpenScenes();
                if (openScene < (i + 1) * 5) {
                    openScene = (i + 1) * 5;
                    if (openScene >= Constants.NUM_SCENES)
                        openScene = Constants.NUM_SCENES - 1; // do not open scene #201 (there is no such!)
                    storeOpenScene(openScene);
                    Toast.makeText(this, getString(R.string.open_scene, openScene + 1), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.scene_already_open, openScene + 1), Toast.LENGTH_LONG).show();
                }
            }
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

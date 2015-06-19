package com.mz800.flappy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class ScreenScore implements Externalizable {
    public static boolean initialized = false;
    public static final ScreenScore[] screenScores = new ScreenScore[Constants.NUM_SCENES];

    int myBestScore;
    int myBestTime;
    int myLives;

    int bestScore;
    int bestTime;
    String bestPlayer;

    static void init() {
        for (int i = 0; i < screenScores.length; i++) {
            if (screenScores[i] == null) screenScores[i] = new ScreenScore();
        }
    }

    @Override
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        myBestScore = input.readInt();
        myBestTime = input.readInt();
        myLives = input.readInt();
        bestScore = input.readInt();
        bestTime = input.readInt();
        bestPlayer = (String) input.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(myBestScore);
        output.writeInt(myBestTime);
        output.writeInt(myLives);
        output.writeInt(bestScore);
        output.writeInt(bestTime);
        output.writeObject(bestPlayer);
    }
}

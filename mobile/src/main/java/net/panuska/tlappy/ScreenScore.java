package net.panuska.tlappy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class ScreenScore implements Externalizable {
    private static final long serialVersionUID = 6450251885936319999L;
    public static boolean initialized = false;
    public static final ScreenScore[] screenScores = new ScreenScore[Constants.NUM_SCENES];

    int myBestScore;
    int myBestTime;
    int myLives;

    int overallAttempts; // this is the overall number of attempts to finish or play this scene
                         // this is not related to myBestScore, myBestTime or myLives

    int bestScore;
    int bestTime;
    String bestPlayer;

    public ScreenScore() { }

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
        overallAttempts = input.readInt();
        bestScore = input.readInt();
        bestTime = input.readInt();
        bestPlayer = (String) input.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(myBestScore);
        output.writeInt(myBestTime);
        output.writeInt(myLives);
        output.writeInt(overallAttempts);
        output.writeInt(bestScore);
        output.writeInt(bestTime);
        output.writeObject(bestPlayer);
    }
}

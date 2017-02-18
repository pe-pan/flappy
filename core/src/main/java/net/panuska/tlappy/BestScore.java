package net.panuska.tlappy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class BestScore implements Externalizable {
    private static final String TAG = BestScore.class.getSimpleName();
    public static final int TOP_VISIBLE_SCORES = 10;
    private static final long serialVersionUID = 5397326438849609255L;

    private int score;
    private int lives;
    private int attempts;
    private long date;
    private String playerId;

    //for externalizable
    public BestScore() {
    }

    /**
     * Lazy filled name of the player.
     */
    private String playerName = "Newbie";  //todo string constant

    @Override
    public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
        score = input.readInt();
        lives = input.readInt();
        attempts = input.readInt();
        date  = input.readLong();
        playerId = (String) input.readObject();
        //keep playerName default value
    }

    @Override
    public void writeExternal(ObjectOutput output) throws IOException {
        output.writeInt(score);
        output.writeInt(lives);
        output.writeInt(attempts);
        output.writeLong(date);
        output.writeObject(playerId);
    }

    public BestScore(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        readExternal(stream);
    }

    public BestScore(int score, int lives, int attempts, long date, String playerId) {
        this.score = score;
        this.lives = lives;
        this.attempts = attempts;
        this.date = date;
        this.playerId = playerId;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getAttempts() {
        return attempts;
    }

    public long getDate() {
        return date;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isBetter(BestScore anotherScore) {
        if (anotherScore.score < this.score) return true;
        if (anotherScore.score > this.score) return false;
        // scores equal
        if (anotherScore.lives < this.lives) return true;
        if (anotherScore.lives > this.lives) return false;
        // lives equal
        if (anotherScore.attempts < this.attempts) return true;
        if (anotherScore.attempts > this.attempts) return false;
        // attempts equal
        if (anotherScore.date < this.date) return true;
        if (anotherScore.date > this.date) return false;
        // dates equal

        // they are equal
        return false;
    }

    @Override
    public String toString() {
        return "BestScore{" +
                "score=" + score +
                ", lives=" + lives +
                ", attempts=" + attempts +
                ", date=" + date +
                ", playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                '}';
    }
}

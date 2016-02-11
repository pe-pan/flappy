package com.mz800.flappy.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
@Entity
public class SceneRecord {

    public SceneRecord() {
    }

    //for testing purposes only
    public SceneRecord(int sceneNo, String playerId, int score, int lives, int attempts, Date date) {
        this.sceneNo = sceneNo;
        this.playerId = playerId;
        this.score = score;
        this.lives = lives;
        this.attempts = attempts;
        this.date = date;

        this.id = calculateUniqueID();
    }

    public String calculateUniqueID() {
        return sceneNo+playerId;
    }

    public @Id String id;
    public @Index int sceneNo;
    public String playerId;

    // the top list is sorted according to those fields
    // the fields are significant in the defined order
    public @Index int score;
    public @Index int lives;
    public @Index int attempts;
    public @Index Date date;

    @Override
    public String toString() {
        return "SceneRecord{" +
                "id='" + id + '\'' +
                ", sceneNo=" + sceneNo +
                ", playerId='" + playerId + '\'' +
                ", score=" + score +
                ", lives=" + lives +
                ", attempts=" + attempts +
                ", date=" + date +
                '}';
    }
}

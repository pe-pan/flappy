package com.mz800.flappy.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
@Entity
public class Player {
    public @Id @Index String id;
    public String name;
    /**
     * Time of the player when being saved at the server. When changing the player name, it's given new time. It's possible to ask for all the players since certain time (to update all the changes).
     * -1 means the time was not given yet
     */
    @Index
    public long time;

    public Player() {
    }

    //for testing purposes only
    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.time = -1; // not given yet
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}

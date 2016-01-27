package com.mz800.flappy;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 *   Android version by Petr Panuska, 2016.
 */
public class Constants {

    static final int SCREEN_WIDTH = 320;
    static final int SCREEN_HEIGHT = 200;
    static final int NUM_SCENES = 200;
    static final int LIVES = 5;
    public static final int SPRITE_SIZE = 16;
    public static final int SPRITE_HALF = 8;
    public static final int FONT_WIDTH = SPRITE_HALF;

    static final int UP = 1;
    static final int DOWN = 2;
    static final int LEFT = 3;
    static final int RIGHT = 4;
    static final int FIRE = 5;
    static final int ESC = 255;
    
    static final int INTRO = -1;
    static final int GAME = -2;
    static final int SETUP = -3;

    static final byte SPACE = 0;
    static final byte MUSHROOM = 0x10;
    static final byte CHICKEN = 0x20;
    static final byte BLUE_ENEMY = 0x30;
    static final byte RED_ENEMY = 0x40;
    static final byte RED_BALL = 0x50;
    static final byte BLUE_BALL = 0x60;
    static final byte WALL = 0x70;
    static final byte BLUE_WALL = 0x71; // ORIGINAL: (byte) 0x80;
    
}

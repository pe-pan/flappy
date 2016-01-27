package com.mz800.flappy.awt;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 *   Android version by Petr Panuska, 2015.
 */
public enum Color {
    BLACK(0xff000000),
    RED(0xffff0000),
    BLUE(0xff0000ff),
    YELLOW(0xffffff00),
    WHITE(0xffffffff),
    DARK_GRAY(0xff404040),
    LIGHT_GRAY(0xffc0c0c0),

    black(0xff000000),
    red(0xffff0000),
    blue(0xff0000ff),
    yellow(0xffffff00),
    white(0xffffffff),
    dark_gray(0xff404040);

    private final int color;
    Color(int color) { this.color = color; }
    public int getValue() { return color; }
}

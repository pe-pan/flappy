package com.mz800.flappy;

import java.awt.image.BufferedImage;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Moveable {

    protected final VRAM vram = VRAM.getInstance();
    protected int x, y;
    protected byte objType;
    protected byte number;
    protected byte sig;
    protected boolean finished = false;
    protected byte[][] model;
    protected Scene scene;

    protected Moveable(int x, int y, byte objType, int num, Scene scene) {
        this.x = x;
        this.y = y;
        this.objType = objType;
        this.number = (byte) num;
        this.sig = (byte) (objType | num);
        this.model = scene.getModel();
        this.scene = scene;
    }

    boolean isFinished() {
        return finished;
    }

    protected void drawMe(BufferedImage img) {
        vram.image(x, y, img);
        model[y][x] = sig;
        model[y][x + 1] = sig;
        model[y + 1][x] = sig;
        model[y + 1][x + 1] = sig;
    }

    protected void drawMe2x1(BufferedImage img) {
        vram.image(x, y, img);
        model[y][x] = sig;
        model[y][x + 1] = sig;
    }
    
    protected void empty2x1(int x, int y) {
        vram.emptyRect(x, y, 2, 8);
        model[y][x] = 0;
        model[y][x + 1] = 0;
    }

    protected void empty1x2(int x, int y) {
        vram.emptyRect(x, y, 1, 16);
        model[y][x] = 0;
        model[y + 1][x] = 0;
    }
}

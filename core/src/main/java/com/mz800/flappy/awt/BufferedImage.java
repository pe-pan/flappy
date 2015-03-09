package com.mz800.flappy.awt;

import android.graphics.Bitmap;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class BufferedImage {
    private Bitmap b;
    private Graphics g;
    public static final int TYPE_INT_RGB = Bitmap.Config.ARGB_8888.ordinal();

    public BufferedImage(int width,
                         int height,
                         int imageType) {
        b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        g = new Graphics(b);
    }

    private BufferedImage(Bitmap b, int x, int y, int width, int height) {
        this.b = Bitmap.createBitmap(b, x, y, width, height);
    }

    public Graphics getGraphics() {
        return g;
    }

    public Bitmap getBitmap() {
        return b;
    }

    public BufferedImage getSubimage (int x, int y, int w, int h) {
        return new BufferedImage(b, x, y, w, h);
    }

    public int getWidth() {
        return b.getWidth();
    }

    public int getHeight() {
        return b.getHeight();
    }
}

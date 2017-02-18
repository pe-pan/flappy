package net.panuska.tlappy.awt;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

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

    public BufferedImage(BitmapDrawable d) {
        Bitmap immb = d.getBitmap();
        b = immb.copy(Bitmap.Config.ARGB_8888, false);
    }

    public BufferedImage(int width,
                         int height,
                         int imageType) {
        b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        g = new Graphics(b);
    }

    private BufferedImage(Bitmap b) {
        this.b = b;
    }

    public Graphics getGraphics() {
        return g;
    }

    public Bitmap getBitmap() {
        return b;
    }

    public BufferedImage getSubimage (int x, int y, int w, int h) {
        Bitmap b = Bitmap.createBitmap(this.b, x, y, w, h);
        return new BufferedImage(b);
    }

    /**
     * Returns a copy of this image rotated by 90 degrees.
     * @return
     */
    public BufferedImage getRotatedImage() {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap b = Bitmap.createBitmap(this.getBitmap(), 0, 0, this.getWidth(), this.getHeight(),  matrix, false);
        return new BufferedImage(b);
    }

    /**
     * Returns a copy of this image mirrored by vertical axis
     * @return
     */
    public BufferedImage getMirroredImage() {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        Bitmap b = Bitmap.createBitmap(this.getBitmap(), 0, 0, this.getWidth(), this.getHeight(),  matrix, false);
        return new BufferedImage(b);
    }

    /**
     * Returns 16x16 subimage starting at x, y
     * @param x
     * @param y
     * @return
     */
    public BufferedImage getSubimage (int x, int y) {
        return getSubimage(x, y, 16, 16);
    }

    public int getWidth() {
        return b.getWidth();
    }

    public int getHeight() {
        return b.getHeight();
    }
}

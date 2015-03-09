package com.mz800.flappy.awt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class Graphics {
    private Canvas c;
    static final private Paint p = new Paint();

    public Graphics(Bitmap b) {
        c = new Canvas(b);
    }

    public void setColor(Color c) {
        p.setColor(c.getValue());
    }

    public void fillRect(int x, int y, int width, int height) {
        p.setStyle(Paint.Style.FILL);
        c.drawRect(x, y, x+width, y+height, p);
    }

    public void drawRect(int x, int y, int width, int height) {
        p.setStyle(Paint.Style.STROKE);
        c.drawRect(x, y, x+width, y+height, p);
    }

    public boolean drawImage(BufferedImage img, int x, int y, Paint paint) {
        c.drawBitmap(img.getBitmap(), x, y, paint);
        return true;
    }
}

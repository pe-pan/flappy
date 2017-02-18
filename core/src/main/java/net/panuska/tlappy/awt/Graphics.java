package net.panuska.tlappy.awt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import net.panuska.tlappy.Constants;
import net.panuska.tlappy.ImageUtils;

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

    public void drawString(String str, int x, int y, Color color) {
        Paint p = new Paint();
        p.setTypeface(Typeface.DEFAULT_BOLD);
        p.setTextSize(Constants.SPRITE_SIZE);
        p.setColor(color.getValue());
        c.drawText(str, x, y + Constants.SPRITE_SIZE, p);
    }

    /**
     * Draws string using monospaced (Flappy built-in) font.
     * @param text
     * @param x
     * @param y
     * @param color
     */
    public void drawText(String text, int x, int y, Color color) {
        //todo copied from VRAM.printText()
        for (int i = 0, maxi = text.length(); i < maxi; i++) {
            int z = text.charAt(i);
            if (z < 0x20 || z > 0x7a) {
                z = '!';
            }
            z -= 0x20;
            BufferedImage imgChar = ImageUtils.createLetterImage(z, color, Color.BLACK);
            drawImage(imgChar, x, y, null);
            x += Constants.FONT_WIDTH;
        }
    }
}

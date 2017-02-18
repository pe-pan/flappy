package net.panuska.tlappy;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

import net.panuska.tlappy.awt.Color;
import net.panuska.tlappy.awt.Graphics;
import net.panuska.tlappy.awt.BufferedImage;

/**
 * Image utilities.
 * 
 * @author Petr Slechta 2014; Petr Panuska 2016
 */
public class ImageUtils {

    private static final int ZOOM = 1;

    private ImageUtils() {
    }
    static BufferedImage loadDrawable(Resources r, int drawableId) {
        BitmapDrawable d = (BitmapDrawable)r.getDrawable(drawableId);
        return new BufferedImage(d);
    }

    public static final int FONT_SIZE = 8;
    public static BufferedImage createLetterImage(int z, Color color, Color background) {
        BufferedImage img = new BufferedImage(FONT_SIZE * ZOOM, FONT_SIZE * ZOOM, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        for (int i = 0; i < FONT_SIZE; i++) {
            int b = Fonts.bitmap[z][i];
            for (int j = 0; j < FONT_SIZE; j++) {
                boolean px = (b & 0x80) == 0x80;
                b = b << 1;
                g.setColor(px ? color : background);
                g.fillRect(j * ZOOM, i * ZOOM, ZOOM, ZOOM);
            }
        }
        return img;
    }
}

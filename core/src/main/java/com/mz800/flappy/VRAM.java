package com.mz800.flappy;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mz800.flappy.awt.BufferedImage;
import com.mz800.flappy.awt.Color;
import com.mz800.flappy.awt.Graphics;

/**
 * VideoRAM "routines".
 * 
 * @author Petr Slechta
 */
public class VRAM {

    private static final int ZOOM = 1;
    private static final int Y_OFS = 3;
    private static VRAM me;

    static VRAM getInstance() {
        if (me == null) me = new VRAM();
        return me;
    }
    private BufferedImage img;
    private SurfaceView window;

    public VRAM() {
        this.img = new BufferedImage(320 * ZOOM, 200 * ZOOM, BufferedImage.TYPE_INT_RGB);
    }

    BufferedImage getImage() {
        return img;
    }

    void clear() {
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 320 * ZOOM, 200 * ZOOM);
    }

    private void drawImage(BufferedImage image, int x, int y) {
        Graphics g = img.getGraphics();
        g.drawImage(image, x, y, null);
    }

    void image(int x, int y, BufferedImage img) {
        drawImage(img, x * 8 * ZOOM, (y + Y_OFS) * 8 * ZOOM);
    }

    void imageNoOfs(int x, int y, BufferedImage img) {
        drawImage(img, x * 8 * ZOOM, y * 8 * ZOOM);
    }

    void textImagePartLeft(int x, int y, BufferedImage img, int len) {
        int ox = len * 8 * ZOOM;
        BufferedImage subImg = img.getSubimage(0, 0, ox, img.getHeight());
        drawImage(subImg, x * 8 * ZOOM, y * 8 * ZOOM);
    }
    
    void textImagePartRight(int x, int y, BufferedImage img, int charNo) {
        int ox = charNo * 8 * ZOOM;
        BufferedImage subImg = img.getSubimage(ox, 0, img.getWidth() - ox, img.getHeight());
        drawImage(subImg, x * 8 * ZOOM, y * 8 * ZOOM);
    }

    void imageNoOfs(int x, int y, BufferedImage img, int yOfs) {
        drawImage(img, x * 8 * ZOOM, y * 8 * ZOOM + yOfs * ZOOM);
    }

    void emptyRect(int x, int y, int w, int h) {
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(x * 8 * ZOOM, (y + Y_OFS) * 8 * ZOOM, w * 8 * ZOOM, h * ZOOM);
    }

    void emptyRectNoOfs(int x, int y, int w, int h) {
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(x * 8 * ZOOM, y * 8 * ZOOM, w * 8 * ZOOM, h * ZOOM);
    }

    void emptyRectNoOfs(int x, int y, BufferedImage image) {
        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(x * 8 * ZOOM, y * 8 * ZOOM, image.getWidth(), image.getHeight());
    }

    void printText(int x, int y, String text, Color color) {
        printText(x, y, text, color, 0, Color.BLACK);
    }

    void printText(int x, int y, String text, Color color, Color background) {
        printText(x, y, text, color, 0, background);
    }

    void printText(int x, int y, String text, Color color, int yOfs, Color background) {
        for (int i = 0, maxi = text.length(); i < maxi; i++) {
            int z = text.charAt(i);
            if (z < 0x20 || z >= 0xE0) {
                z = '!';
            }
            if (z >= 0xA0) {
                z -= 0xA0;
            } else if (z >= 0x80) {
                z = '!';
            } else {
                z -= 0x20;
            }
            BufferedImage imgChar = ImageUtils.createLetterImage(1, 8, color, Device.memory, 0x6add + 8 * z, background);
            imageNoOfs(x, y, imgChar, yOfs);
            x++;
        }
    }

    void rect(int x, int y, int w, int h, Color color) {
        Graphics g = img.getGraphics();
        g.setColor(color);
        int x2 = x * ZOOM;
        int y2 = y * ZOOM;
        int w2 = w * ZOOM;
        int h2 = h * ZOOM;
        for (int i = 0; i < ZOOM; i++) {
            g.drawRect(x2, y2, w2, h2);
            g.drawRect(x2 + i, y2 + i, w2 - 2 * i, h2 - 2 * i);
        }
    }

    void refresh() {
        SurfaceHolder holder = window.getHolder();
        Canvas c = holder.lockCanvas();
        if (c!= null) {
            if (ratio == 0) {
                int targetHeight = c.getHeight();
                int targetWidth = c.getWidth();
                if (targetHeight > targetWidth) {
                    targetHeight = targetHeight + targetWidth;   // h + w
                    targetWidth = targetHeight - targetWidth;    // h + w - w -> h
                    targetHeight = targetHeight - targetWidth;   // h + w - h -> w
                    Log.d("VRAM", "Ratio swapped "+targetWidth+", "+targetHeight);
                }
                int sourceHeight = img.getHeight();
                int sourceWidth = img.getWidth();
                float ratioH = (float) targetHeight / (float) sourceHeight;
                float ratioW = (float) targetWidth / (float) sourceWidth;
                ratio = Math.min(ratioH, ratioW);
                Log.d("VRAM", "ratio: " + ratio);
            }
            c.scale(ratio, ratio);
            c.drawBitmap(img.getBitmap(), 0, 0, paint);
            holder.unlockCanvasAndPost(c);
        }
    }

    private float ratio = 0;
    private Paint paint;
    void createWindow(SurfaceView window) {
        this.window = window;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
    }
    
}

package com.mz800.flappy;

import com.mz800.flappy.awt.Color;
import com.mz800.flappy.awt.Graphics;
import com.mz800.flappy.awt.BufferedImage;

/**
 * Image utilities.
 * 
 * @author Petr Slechta
 */
class ImageUtils {

    private static final int ZOOM = 1;

    private ImageUtils() {
    }
    static boolean msbFirst = false;
    static Color[] colors = new Color[]{Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW};

    static BufferedImage createImage2x2x2(byte[] memory, int addr) {
        return createImage(2, 16, 2, colors, memory, addr);
    }

    static BufferedImage createImageMirror2x2x2(byte[] memory, int addr) {
        return createImageMirror(2, 16, 2, colors, memory, addr);
    }

    static BufferedImage createImage2x1x1(byte[] memory, int addr) {
        return createImage(1, 8, 2, colors, memory, addr);
    }

    static BufferedImage createImage2x3x2(byte[] memory, int addr) {
        return createImage(3, 16, 2, colors, memory, addr);
    }

    static BufferedImage createImage2x2x3(byte[] memory, int addr) {
        return createImage(2, 24, 2, colors, memory, addr);
    }

    static BufferedImage createImage2x2x1(byte[] memory, int addr) {
        return createImage(2, 8, 2, colors, memory, addr);
    }

    static BufferedImage createImageMirror(int w, int h, int planes, Color[] colors, byte[] memory, int addr) {
        msbFirst = true;
        BufferedImage res = createImage(2, 16, 2, colors, memory, addr);
        msbFirst = false;
        return res;
    }

    static BufferedImage createImage(int w, int h, int planes, Color[] colors, byte[] memory, int addr) {
        BufferedImage img = new BufferedImage(w * 8 * ZOOM, h * ZOOM, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int[] pixel = new int[8];
                for (int plane = 0; plane < planes; plane++) {
                    int b = memory[addr + plane * w * h];
                    for (int k = 0; k < 8; k++) {
                        pixel[k] <<= 1;
                        boolean px;
                        if (msbFirst) {
                            px = (b & 0x80) == 0x80;
                            b = b << 1;
                        } else {
                            px = (b & 0x01) == 0x01;
                            b = b >> 1;
                        }
                        if (px) {
                            pixel[k] |= 1;
                        }
                    }
                }
                for (int k = 0; k < 8; k++) {
                    g.setColor(colors[pixel[k]]);
                    g.fillRect((j * 8 + k) * ZOOM, i * ZOOM, ZOOM, ZOOM);
                }
                addr++;
            }
        }
        return img;
    }

    static BufferedImage createLetterImage(int w, int h, Color color, byte[] memory, int addr) {
        return createLetterImage(w, h, color, memory, addr, Color.BLACK);
    }

    static BufferedImage createLetterImage(int w, int h, Color color, byte[] memory, int addr, Color background) {
        BufferedImage img = new BufferedImage(w * 8 * ZOOM, h * ZOOM, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int b = memory[addr];
                for (int k = 0; k < 8; k++) {
                    boolean px;
                    px = (b & 0x80) == 0x80;
                    b = b << 1;
                    g.setColor(px ? color : background);
                    g.fillRect((j * 8 + k) * ZOOM, i * ZOOM, ZOOM, ZOOM);
                }
                addr++;
            }
        }
        return img;
    }

    static BufferedImage createTextImage(byte[] memory, int addr, int len, Color color, Color background) {
        return createTextImage(memory, addr, len, color, background, 0);
    }

    static BufferedImage createTextImage(byte[] memory, int addr, int len, Color color, Color background, int skip) {
        int w = len * 8 * ZOOM;
        BufferedImage img = new BufferedImage(w, 8 * ZOOM, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        for (int let = 0; let < len; let++) {
            addr += skip;
            for (int i = 0; i < 8; i++) {
                int b = memory[addr++];
                for (int k = 0; k < 8; k++) {
                    boolean px = (b & 0x80) == 0x80;
                    b = b << 1;
                    g.setColor(px ? color : background);
                    g.fillRect((let * 8 + k) * ZOOM, i * ZOOM, ZOOM, ZOOM);
                }
            }
        }
        return img;
    }
}

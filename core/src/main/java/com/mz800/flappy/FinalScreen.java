package com.mz800.flappy;

import com.mz800.flappy.awt.Color;
import static com.mz800.flappy.Device.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class FinalScreen {

    private static byte[] arr7D19 = new byte[]{ // steps 1
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4,
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, (byte) 0xFF
    };
    private static byte[] arr7CDC = new byte[]{ // steps 2
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
        4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, (byte) 0xFF
    };
    private static byte[] arr7C64 = new byte[]{ // max steps + act steps
        0x3C, 0x3A, 0x3A, 0x38, 0x38, 0x36, 0x36, 0x34, 0x34, 0x32, 0x32, 0x30, 0x30, 0x2E, 0x2E, 0x2C,
        0x2C, 0x2A, 0x2A, 0x28, 0x28, 0x26, 0x26, 0x24, 0x24, 0x22, 0x22, 0x20, 0x20, 0x1E, 0x1E, 0x1C,
        0x1C, 0x1A, 0x1A, 0x18, 0x18, 0x16, 0x16, 0x14, 0x14, 0x12, 0x12, 0x10, 0x10, 0x0E, 0x0E, 0x0C,
        0x0C, 0x0A, 0x0A, 8, 8, 6, 6, 4, 4, 2, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0
    };
    private static byte[] arr7BEC = new byte[120]; // x y

    private void chickenBorder() {
        Chicken chicken = new Chicken(new Scene(1));
        Chicken.DemoStepInfo par = new Chicken.DemoStepInfo();
        for (int mc = 0; mc < 0x76; mc++) {
            int noCh = mc / 2 + 1;
            if ((mc & 1) == 0) {
                noCh--;
            }
            for (int c = 0; c < noCh; c++) {
                if ((c & 1) == 1) {
                    rut(arr7D19, c, chicken);
                } else {
                    rut(arr7CDC, c, chicken);
                }
            }

            vram.refresh();
            Main.wait(10);

            if ((mc & 1) == 0) {
                noCh++;
            }

            for (int c = 0; c < noCh; c++) {
                if ((mc & 1) == 0 && mc == 2 * c) {
                    arr7BEC[2 * c] = 0x12;
                    arr7BEC[2 * c + 1] = 0x16;
                    arr7C64[c + 0x3c] = 0;
                    vram.imageNoOfs(0x12, 0x16, Images.chickenDown);
                } else {
                    int cc = 2 * c;
                    if (arr7C64[c] == arr7C64[c + 0x3c]) { // final position
                        vram.imageNoOfs(arr7BEC[cc], arr7BEC[cc + 1], Images.chickenDown);
                    }
                    if (arr7C64[c + 0x3c] < arr7C64[c]) {
                        par.steps = (c & 1) == 0 ? arr7CDC : arr7D19;
                        par.pStep = arr7C64[c + 0x3c];
                        par.cx = arr7BEC[cc];
                        par.cy = arr7BEC[cc + 1];
                        chicken.demoDoStep2(par);
                        arr7BEC[cc] = (byte) par.cx;
                        arr7BEC[cc + 1] = (byte) par.cy;
                        arr7C64[c + 0x3c]++;
                    }
                }
            }
            //sync
            vram.refresh();
            Main.wait(50);
        }
        vram.imageNoOfs(20, 22, Images.chickenDown);
        vram.refresh();
    }

    private void rut(byte[] arr, int c, Chicken chicken) {
        Chicken.DemoStepInfo par = new Chicken.DemoStepInfo();
        int cc = 2 * c;
        if (arr7C64[c + 0x3c] >= arr7C64[c]) {
            return;
        }
        par.steps = arr;
        par.pStep = arr7C64[c + 0x3c];
        par.cx = arr7BEC[cc];
        par.cy = arr7BEC[cc + 1];
        chicken.demoDoStep1(par);
        arr7BEC[cc] = (byte) par.cx;
        arr7BEC[cc + 1] = (byte) par.cy;
    }
    
    void finalCertificate() {
        vram.clear();
        vram.refresh();
        music.start();
        chickenBorder();
        final Color c = Color.WHITE;
        vram.printText(4, 2, "* Certificate of Commendation *", c, 4, Color.BLACK);
        vram.printText(8, 4, "This is to certify that", c);
        vram.printText(5, 5, "you have successfully carried", c);
        vram.printText(13, 6, "all of the 200", c);
        vram.printText(7, 7, "Blue Stones into Blue Area", c);
        vram.printText(12, 8, "using your mind,", c);
        vram.printText(4, 9, "motor skills and reflex actions", c);
        vram.printText(3, 10, "and at times all your nevers while", c);
        vram.printText(8, 11, "rubbing your drowsy eyes.", c);
        vram.printText(3, 12, "Your perseverance is commendable.", c);
        vram.printText(5, 14, "The dB-SOFT Inc. hereby honors", c);
        vram.printText(7, 15, "you for your intelligence,", c);
        vram.printText(6, 16, "physical strength, fight and", c);
        vram.printText(3, 17, "for your haveing ample free time.", c);
        vram.printText(6, 18, "You are a wonderful person.", c);
        vram.printText(3, 20, "* Program Director Shoichi Shibata", c);
        vram.printText(3, 21, "* Programmer       Kazuyuki Okuyama", c);
        vram.refresh();
        Main.wait(4000);
        vram.clear();
        vram.refresh();
    }
    
}

package com.mz800.flappy;

import com.mz800.flappy.awt.BufferedImage;
import com.mz800.flappy.awt.Color;
import com.mz800.flappy.awt.Graphics;

import static com.mz800.flappy.Device.music;
import static com.mz800.flappy.Device.vram;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class BestScoreScreen {
    public static final int FAST = 1;
    public static final int SLOW = 50;

    static int speed = SLOW;

    private static BufferedImage[][] horizontalIcons = { { Images.redEnemyRight, Images.redEnemyRightS2Demo}, {Images.redEnemyLeft, Images.redEnemyLeftS2Demo}};
    private static BufferedImage[] verticalIcons = { Images.redEnemyDownS1Demo, Images.redEnemyDownS2Demo };
    private static BufferedImage[] flappySummoningFrog = { Images.chickenLeft, Images.chickenLeftFire1, Images.chickenLeftFire2, Images.chickenLeft, Images.chickenDown, null, null };
    private static BufferedImage[] frogAppears = { null, null, null, Images.redEnemySleep4, Images.redEnemySleep3, Images.redEnemySleep2, Images.redEnemySleep1 }; // length of this array must be the same as flappySummoningFrog array
    private static BufferedImage[] frogDisappears = { Images.noEnemySleep1, Images.noEnemySleep2, Images.noEnemySleep3, Images.noEnemySleep4 };
    private static BufferedImage background;
    public static void bestScore(BestScore[] bestScores) {
        vram.clear();
        vram.refresh();
        music.start();
        speed = SLOW;
        chickenBorder();
        if (!Main.isWaiting()) return;

        background = getBestScoreList(bestScores);

        oldX = -1;
        int x = 2*18;
        int y = 2*1;

        summonFrog(x, y);

        for (int i = 0; i < 10; i++) {
            drawImage(x, y, Images.redEnemyDown, background);
            for (int j = 0; j < 2*17; j++) {
                int yMod4 = (y >> 1) & 0x1; // if y mod 4 == 1 then 1 else 0;
                int inc = (yMod4 << 1) - 1; // if y mod 4 == 1 then +1 else -1;
                x -= inc;                   // go left or right depending on if y mod 4 is divisible by 4
                drawImage(x, y, horizontalIcons[yMod4][x & 0x01], background);
                if (!Main.isWaiting()) return;
            }
            drawImage(x, y, Images.redEnemyDown, background);
            if (i == 9) { // Frog has finished his way down
                speed = SLOW;
                summonFrog(x, y);  // get Frog stunned
                for (BufferedImage image : frogDisappears) {  // let Frog to disappear
                    drawImage(x, y, image, background);
                }
                drawImage(x, y, null, background);
            } else {
                for (int j = 0; j < 2; j++) {
                    y++;
                    drawImage(x, y, verticalIcons[j], background);
                    if (!Main.isWaiting()) return;
                }
            }
        }
    }

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

    //todo taken from FinalScreen class
    private static void chickenBorder() {
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
            Main.wait(speed);

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
                if (!Main.isWaiting()) return;
            }
            //sync
            vram.refresh();
            Main.wait(speed);
        }
        vram.imageNoOfs(20, 22, Images.chickenDown);
        vram.refresh();
    }

    private static void rut(byte[] arr, int c, Chicken chicken) {
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


    private static BufferedImage getBestScoreList(BestScore[] bestScores) {
        //create black background
        BufferedImage background = new BufferedImage(18 * Constants.SPRITE_SIZE, 10 * Constants.SPRITE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, background.getWidth(), background.getHeight());

        for (int i = 0; i < bestScores.length; i++) {
            BestScore bestScore = bestScores[i];
            int y = i * Constants.SPRITE_SIZE;
            g.drawString(bestScore.getPlayerId(), 0, y, Color.YELLOW);
            g.drawText("   ", 8 * ((18 - 6) * 2 - 1), y, Color.WHITE);  // todo this is to clean too long player name; do it differently -> g.drawString should be restricted to rectangle
            g.drawText(" "+bestScore.getLives()+"x", 8 * ((18 - 6) * 2 - 1), y + Constants.SPRITE_HALF, Color.LIGHT_GRAY);
            g.drawImage(Images.chickenDown, (18 - 5) * Constants.SPRITE_SIZE, y, null);
            g.drawText("At", (18 - 4) * Constants.SPRITE_SIZE, y, Color.LIGHT_GRAY);
            g.drawText("S:", (18 - 4) * Constants.SPRITE_SIZE, y + Constants.SPRITE_HALF, Color.LIGHT_GRAY);
            g.drawText(String.format("%5dx", bestScore.getAttempts()), (18 - 3) * Constants.SPRITE_SIZE, y, Color.WHITE);
            g.drawText(String.format("%06d", bestScore.getScore()), (18 - 3) * Constants.SPRITE_SIZE, y + Constants.SPRITE_HALF, Color.YELLOW);
        }
        return background;
    }

    private static void summonFrog(int x, int y) {
        for (int i = 0; i < flappySummoningFrog.length; i++) {
            BufferedImage flappy = flappySummoningFrog[i];
            if (flappy != null) vram.imageNoOfs(x + 2, y, flappy);
            if (i == 2 && y == 2) vram.imageNoOfs(x + 1, y, Images.mushroom);  // show mushroom only first time (when summoning Frog not when dispersing Frog)
            BufferedImage frog = frogAppears[i];
            if (frog != null) {
                drawImage(x, y, frog, background);
            } else {
                vram.refresh();
                Main.wait(speed);
            }
        }
    }

    private static int oldX = -1, oldY = -1;
    private static void drawImage(int x, int y, BufferedImage img, BufferedImage background) {
        if (oldX >= 0) {
            vram.imageNoOfs(oldX, oldY, background.getSubimage((oldX - 2) * Constants.SPRITE_HALF, (oldY - 2) * Constants.SPRITE_HALF, Constants.SPRITE_SIZE, Constants.SPRITE_SIZE));
        }
        if (img != null) vram.imageNoOfs(x, y, img);
        vram.refresh();
        Main.wait(speed);
        oldX = x;
        oldY = y;
    }
}

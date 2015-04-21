package com.mz800.flappy;

import com.mz800.flappy.awt.Color;
import static com.mz800.flappy.Constants.*;
import static com.mz800.flappy.Device.*;
import com.mz800.flappy.awt.BufferedImage;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
public class Intro {

    private static final BufferedImage[] CHICKENS = {
        Images.chickenUp, Images.chickenDown, Images.chickenLeft, Images.chickenRight,
        Images.chickenUp1, Images.chickenDown1, Images.chickenLeft1, Images.chickenRight1,
        Images.chickenUp2, Images.chickenDown2, Images.chickenLeft1, Images.chickenRight1
    };
    private static final BufferedImage[] BLUE_ENEMS = {
        null, null, Images.blueEnemyLeft2, Images.blueEnemyRight2,
        null, null, Images.blueEnemyLeft1, Images.blueEnemyRight1,
        null, null, Images.blueEnemyLeft1, Images.blueEnemyRight1
    };
    private static final BufferedImage[] RED_ENEMS = {
        Images.redEnemyUp, Images.redEnemyDown, Images.redEnemyLeft, Images.redEnemyRight,
        Images.redEnemyUpS1, Images.redEnemyDownS1, Images.redEnemyLeftS2, Images.redEnemyRightS2,
        Images.redEnemyUpS2, Images.redEnemyDownS2, Images.redEnemyLeftS2, Images.redEnemyRightS2
    };
    private static final BufferedImage[] BLUE_SLEEP = {
        Images.blueEnemySleep1, Images.blueEnemySleep2, Images.blueEnemySleep3,
        Images.blueEnemySleep4, Images.blueEnemySleep5, Images.blueEnemySleep6
    };
    private static final byte[] CHICKEN_P1 = {
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 3, 0,
        2, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 3, (byte) 0x8A, (byte) 0x8B, 4,
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, (byte) 0xFE, -1
    };
    private static final byte[] CHICKEN_P2 = {
        3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2,
        2, 2, 2, 2, 2, 2, 3, (byte) 0x8A, (byte) 0x8B, 4, 4, 4, 4, 4, 4, 4, 4,
        4, 4, 4, 4, 4, (byte) 0xFE, -1
    };
    private static final byte[] BLUE_ENEMY_P1 = {
        3, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 4, 3, 3,
        3, 4, 3, 3, 3, 3, 3, 3, 4, 4, 3, 3, 4, 4, 4, 4,
        4, 4,
        (byte) 0x81, (byte) 0x81, (byte) 0x82, (byte) 0x82, (byte) 0x83, (byte) 0x83,
        (byte) 0x84, (byte) 0x84, (byte) 0x85, (byte) 0x85,
        0, 0, 0, 0, 0, 0, 0, 0, -1
    };
    private static final byte[] BLUE_ENEMY_P2 = {
        3, 4, 4, 4, 3, 3, 4, 4, 4, 3, 4, 3, 3, 3,
        3, 3, 3, 4, 3, 4, 4, 4, 4, 4,
        (byte) 0x81, (byte) 0x81, (byte) 0x82, (byte) 0x82, (byte) 0x83, (byte) 0x83,
        (byte) 0x84, (byte) 0x84, (byte) 0x85, (byte) 0x85,
        0, 0, 0, 0, 0, 0, 0, 0, -1
    };
    private static final byte[] RED_ENEMY_P1 = {
        1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 1, (byte) 0xE8, -1
    };
    private static final byte[] BLUE_BALL_P1 = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 2, 0, 2,
        0, 0, 0, 0, 0, 0, 2,
        2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, -1
    };
    private static final byte[] BLUE_BALL_P2 = {
        0, 0, 0, 0, 3, 0, 3, 2, 2,
        (byte) 0xE0, 2, 0, 0, 0, 0, 0, 0, (byte) 0xE3, 2, 2,
        (byte) 0xE1, 2, 0, 0, 0, 0, 0, 0, (byte) 0xE4, 2, 2,
        (byte) 0xE2, 2, 0, 0, 0, 0, 0, 0, (byte) 0xE5, 2, 2,
        2, 0, 0, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, -1
    };
    private static final byte[] MUSHROOM_P1 = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        11, 10, 9, 8, -1
    };
    private static final byte[] MUSHROOM_P2 = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 14, 13, 12, -1
    };
    private static final byte[] EXTRA_P1 = {
        0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
        8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 10, 11, 12, 13, 14
    };
    private static final byte[] EXTRA_P2 = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 15, 16, 17, 18, 19, 20, 21, 22
    };
    private final byte[] memory;
    private ElementStatus chicken;
    private ElementStatus blueEnemy;
    private ElementStatus redEnemy;
    private ElementStatus blueBall;
    private byte[] mushroomArr;
    private int mushroomPtr;
    private int mushroomY;
    private byte[] textArr;
    private int textPtr;
    private boolean secondStep;
    private boolean firstSubStep;

    Intro() {
        this.memory = Main.memory;
    }

    void intro() {
        vram.clear();
        music.start();
        Main.resetState(Main.NORMAL_WAIT);
        int r;
        while (Main.isWaiting()) {
            r = part1();
            if (r != Main.NORMAL_WAIT) {
                return;
            }
            r = part2();
            if (r != Main.NORMAL_WAIT) {
                return;
            }
        }
    }

    private int part1() {
        int r = drawLogo(true);
        if (r != 0) {
            return r;
        }
        vram.emptyRectNoOfs(0, 9, 40, (25 - 9) * 8);
        drawWall(13, 0x33DA);
        drawWall(14, 0x33DA);
        drawWall(22, 0x33EE);
        drawWall(23, 0x33F8);
        vram.imageNoOfs(28, 11, Images.chickenDown);
        vram.imageNoOfs(24, 16, Images.redEnemyDown);
        vram.imageNoOfs(11, 20, Images.blueEnemyLeft2);
        vram.imageNoOfs(17, 11, Images.blueBall);
        vram.imageNoOfs(22, 12, Images.mushroom);
        vram.printText(18, 18, "1984", Color.yellow);
        vram.refresh();

        chicken = new ElementStatus(28, 11, CHICKEN_P1, CHICKENS);
        blueEnemy = new ElementStatus(11, 20, BLUE_ENEMY_P1, BLUE_ENEMS);
        redEnemy = new ElementStatus(24, 16, RED_ENEMY_P1, RED_ENEMS);
        blueBall = new ElementStatus(17, 11, BLUE_BALL_P1, Images.blueBall);
        mushroomArr = MUSHROOM_P1;
        mushroomY = 20;
        textArr = EXTRA_P1;
        textPtr = 0;
        return moveElements();
    }

    private int part2() {
        int r = drawLogo(false);
        if (r != 0) {
            return r;
        }
        vram.emptyRectNoOfs(0, 9, 40, (25 - 9) * 8);

        int w = 14 * 8;
        int h = 0x5e;
        vram.rect(19, 76, w, h, Color.RED);
        vram.rect(21, 76, w - 4, h, Color.RED);
        vram.imageNoOfs(3, 10, Images.redEnemyDown);
        vram.printText(6, 11, "50", Color.yellow);
        vram.printText(9, 11, "Points", Color.RED);
        vram.imageNoOfs(3, 13, Images.blueEnemyLeft2);
        vram.printText(6, 14, "30", Color.yellow);
        vram.printText(9, 14, "Points", Color.RED);
        vram.printText(4, 16, "<< Bonus >>", Color.BLUE);
        vram.imageNoOfs(4, 18, Images.mushroom);
        vram.printText(6, 18, "10", Color.yellow);
        vram.printText(9, 18, "Points", Color.RED);
        vram.printText(7, 20, "& TIME...", Color.RED);

        w = 9 * 8;
        h = 0x5e;
        vram.rect(28 * 8 + 4, 76, w, h, Color.BLUE);
        vram.rect(28 * 8 + 6, 76, w - 4, h, Color.BLUE);
        vram.imageNoOfs(32, 10, Images.arrUp);
        vram.imageNoOfs(29, 13, Images.arrLeft);
        vram.imageNoOfs(32, 13, Images.chickenDown);
        vram.imageNoOfs(35, 13, Images.arrRight);
        vram.imageNoOfs(32, 16, Images.arrDown);
        vram.imageNoOfs(30, 19, Images.space1);
        vram.imageNoOfs(32, 19, Images.space2);
        vram.imageNoOfs(34, 19, Images.space3);
        drawWall(12, 0x3402);
        drawWall(13, 0x3402);
        drawWall(24, 0x3416);
        // 25e0

        vram.imageNoOfs(25, 10, Images.chickenDown);
        vram.imageNoOfs(19, 14, Images.redEnemyDown);
        vram.imageNoOfs(19, 17, Images.blueEnemyLeft2);
        vram.imageNoOfs(19, 20, Images.redEnemyDown);
        vram.imageNoOfs(21, 10, Images.blueBall);
        vram.imageNoOfs(23, 11, Images.mushroom);
        vram.imageNoOfs(10, 22, Images.blueEnemyLeft2);
        vram.refresh();

        chicken = new ElementStatus(25, 10, CHICKEN_P2, CHICKENS);
        blueEnemy = new ElementStatus(10, 22, BLUE_ENEMY_P2, BLUE_ENEMS);
        redEnemy = null;
        blueBall = new ElementStatus(21, 10, BLUE_BALL_P2, Images.blueBall);
        mushroomArr = MUSHROOM_P2;
        mushroomY = 22;
        textArr = EXTRA_P2;
        textPtr = 0;
        return moveElements();
    }

    private int moveElements() {
        mushroomPtr = 0;
        int r;
        while (!chicken.finished) {
            secondStep = false;
            moveMushroom();
            moveRedEnemy();
            moveBlueEnemy();
            moveBlueBall();
            moveChicken();
            //drawExtra();
            vram.refresh();
            r = Main.wait(20);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }

            secondStep = true;
            moveMushroom();
            moveRedEnemy();
            moveBlueEnemy();
            moveBlueBall();
            moveChicken();
            drawExtra();
            vram.refresh();
            r = Main.wait(20);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }

            firstSubStep = !firstSubStep;
        }
        return 0;
    }

    private void drawExtra() {
        if (textPtr < textArr.length) {
            switch (textArr[textPtr++]) {
                case 1:
                    vram.textImagePartRight(16 + 7, 16, Images.cDbSoft, 7);
                    break;
                case 2:
                    vram.textImagePartRight(16 + 6, 16, Images.cDbSoft, 6);
                    break;
                case 3:
                    vram.textImagePartRight(16 + 5, 16, Images.cDbSoft, 5);
                    break;
                case 4:
                    vram.textImagePartRight(16 + 4, 16, Images.cDbSoft, 4);
                    break;
                case 5:
                    vram.textImagePartRight(16 + 3, 16, Images.cDbSoft, 3);
                    break;
                case 6:
                    vram.textImagePartRight(16 + 2, 16, Images.cDbSoft, 2);
                    break;
                case 7:
                    vram.textImagePartRight(16 + 1, 16, Images.cDbSoft, 1);
                    break;
                case 8:
                    vram.textImagePartLeft(16, 16, Images.cDbSoft, 8);
                    break;
                case 9:
                    vram.textImagePartLeft(17, 20, Images.bySSKO, 1);
                    break;
                case 10:
                    vram.textImagePartLeft(17, 20, Images.bySSKO, 2);
                    break;
                case 11:
                    vram.textImagePartLeft(17, 20, Images.bySSKO, 3);
                    break;
                case 12:
                    vram.textImagePartLeft(17, 20, Images.bySSKO, 4);
                    break;
                case 13:
                    vram.textImagePartLeft(17, 20, Images.bySSKO, 5);
                    break;
                case 14:
                    vram.textImagePartLeft(17, 20, Images.bySSKO, 6);
                    break;
                case 15:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 1);
                    break;
                case 16:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 2);
                    break;
                case 17:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 3);
                    break;
                case 18:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 4);
                    break;
                case 19:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 5);
                    break;
                case 20:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 6);
                    break;
                case 21:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 7);
                    break;
                case 22:
                    vram.textImagePartLeft(17, 22, Images.cDbSoft, 8);
                    break;
            }
        }
    }

    private void moveBlueEnemy() {
        vram.emptyRectNoOfs(blueEnemy.x, blueEnemy.y, 2, 16);
        int act = blueEnemy.changeCoordinates(true);
        if (act <= -2) {
            specialActions(act);
        }
        vram.imageNoOfs(blueEnemy.x, blueEnemy.y, blueEnemy.image);
    }

    private void moveRedEnemy() {
        if (redEnemy != null && !redEnemy.finished) {
            vram.emptyRectNoOfs(redEnemy.x, redEnemy.y, redEnemy.image);
            int act = redEnemy.changeCoordinates(true);
            if (act <= -2) {
                specialActions(act);
            }
            vram.imageNoOfs(redEnemy.x, redEnemy.y, redEnemy.image);
        }
    }

    private void moveBlueBall() {
        vram.emptyRectNoOfs(blueBall.x, blueBall.y, 2, 16);
        int act = blueBall.changeCoordinates(false);
        if (act <= -2) {
            specialActions(act);
        }
        vram.imageNoOfs(blueBall.x, blueBall.y, blueBall.image);
    }

    private void moveChicken() {
        vram.emptyRectNoOfs(chicken.x, chicken.y, 2, 16);
        int act = chicken.changeCoordinates(true);
        if (act <= -2) {
            specialActions(act);
        }
        vram.imageNoOfs(chicken.x, chicken.y, chicken.image);
    }

    private void moveMushroom() {
        int x = mushroomArr[mushroomPtr];
        if (x == -1) {
            return;
        }
        if (x > 0) {
            vram.emptyRectNoOfs(x + 1, mushroomY, 1, 8);
            vram.imageNoOfs(x, mushroomY, Images.mushroom);
        }
        if (secondStep) {
            mushroomPtr++;
        }
    }

    private void specialActions(int act) {
        act = act & 0xFF;
        switch (act) {
            case 0x8A:
                chicken.image = Images.chickenLeftFire1;
                break;
            case 0x8B:
                chicken.image = Images.chickenLeftFire2;
                break;
            case 0x81:
            case 0x82:
            case 0x83:
            case 0x84:
            case 0x85:
            case 0x86:
                blueEnemy.image = BLUE_SLEEP[act - 0x80];
                break;
            case 0xE0:
                vram.emptyRectNoOfs(19, 14, 2, 8);
                vram.imageNoOfs(19, 15, Images.redEnemyCrash1);
                vram.printText(21, 15, "(x1)", Color.yellow);
                break;
            case 0xE1:
                vram.emptyRectNoOfs(19, 17, 2, 8);
                vram.imageNoOfs(19, 18, Images.blueEnemyCrash1);
                vram.printText(21, 18, "(x2)", Color.yellow);
                break;
            case 0xE2:
                vram.emptyRectNoOfs(19, 20, 2, 8);
                vram.imageNoOfs(19, 21, Images.redEnemyCrash1);
                vram.printText(21, 21, "(x3)", Color.yellow);
                break;
            case 0xE3:
                vram.emptyRectNoOfs(21, 15, 4, 8);
                break;
            case 0xE4:
                vram.emptyRectNoOfs(21, 18, 4, 8);
                break;
            case 0xE5:
                vram.emptyRectNoOfs(21, 21, 4, 8);
                break;
            case 0xE8:
                vram.emptyRectNoOfs(redEnemy.x, redEnemy.y, 2, 8);
                redEnemy.y++;
                redEnemy.image = Images.redEnemyCrash1;
                break;
            case 0xFE:
                vram.imageNoOfs(chicken.x, chicken.y, Images.chickenDown);
                vram.refresh();
                Main.wait(0x14);
                for (int i = 0; i < 3; i++) {
                    vram.imageNoOfs(chicken.x, chicken.y, Images.chickenJump1);
                    vram.refresh();
                    Main.wait(0x14);
                    vram.imageNoOfs(chicken.x, chicken.y, Images.chickenJump2);
                    vram.refresh();
                    Main.wait(0x5);
                    vram.imageNoOfs(chicken.x, chicken.y, Images.chickenDown);
                    vram.refresh();
                    Main.wait(0x0C);
                }
                chicken.image = Images.chickenDown;
                break;
        }
    }

    private int drawLogo(boolean blueStyle) {
        int addr = blueStyle ? 0x3326 : 0x3380;
        int r;
        for (int i = 1; i <= 9; i++) {
            drawLogoPart(addr, i);
            vram.refresh();
            r = Main.wait(20);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }
        }
        return 0;
    }

    private void drawLogoPart(int addr, int lines) {
        int y = 9 - lines;
        for (int line = 0; line < lines; line++) {
            int x = 0;
            for (int i = 0; i < 10; i++) {
                int b = memory[addr + line * 10 + i] & 0xFF;
                for (int j = 0; j < 4; j++) {
                    b <<= 2;
                    switch (b & 0x300) {
                        case 0x100:
                            vram.imageNoOfs(x, y, Images.redWall);
                            break;
                        case 0x200:
                            vram.imageNoOfs(x, y, Images.blueWall);
                            break;
                        case 0x300:
                            break;
                        default:
                            vram.emptyRectNoOfs(x, y, 1, 8);
                    }
                    x++;
                }
            }
            y++;
        }
    }

    private void drawWall(int y, int addr) {
        int x = 0;
        for (int i = 0; i < 10; i++) {
            int b = memory[addr + i] & 0xFF;
            for (int j = 0; j < 4; j++) {
                b <<= 2;
                switch (b & 0x300) {
                    case 0x100:
                        vram.imageNoOfs(x, y, Images.redWall);
                        break;
                    case 0x200:
                        vram.imageNoOfs(x, y, Images.blueWall);
                        break;
                    case 0x300:
                        break;
                    default:
                        vram.emptyRectNoOfs(x, y, 1, 8);
                }
                x++;
            }
        }
    }

    private class ElementStatus {

        int x;
        int y;
        byte[] arr;
        int arrPtr = 0;
        boolean finished = false;
        BufferedImage[] images;
        BufferedImage image;
        int lastMove;

        ElementStatus(int x, int y, byte[] arr, BufferedImage[] images) {
            this.x = x;
            this.y = y;
            this.arr = arr;
            this.images = images;
            this.image = images != null ? images[0] : null;
        }

        ElementStatus(int x, int y, byte[] arr, BufferedImage image) {
            this(x, y, arr, (BufferedImage[]) null);
            images = new BufferedImage[12];
            for (int i = 0; i < 12; i++) {
                images[i] = image;
            }
            this.image = image;
        }

        int changeCoordinates(boolean doubleSteps) {
            if (finished) {
                return -1;
            }

            lastMove = arrPtr > 0 ? arr[arrPtr - 1] : 0;
            int act = arr[arrPtr];
            if (act == -1) {
                finished = true;
            }
            if (act < 0) {
                arrPtr++;
                return act;
            }

            boolean move = !doubleSteps || !secondStep;
            if (!doubleSteps || secondStep) {
                arrPtr++;
            }
            switch (act) {
                case UP:
                    if (move) {
                        y--;
                    }
                    image = secondStep ? images[0] : (firstSubStep ? images[4] : images[8]);
                    break;
                case DOWN:
                    if (move) {
                        y++;
                    }
                    image = secondStep ? images[1] : (firstSubStep ? images[5] : images[9]);
                    break;
                case LEFT:
                    if (move) {
                        x--;
                    }
                    image = secondStep ? images[2] : (firstSubStep ? images[6] : images[10]);
                    break;
                case RIGHT:
                    if (move) {
                        x++;
                    }
                    image = secondStep ? images[3] : (firstSubStep ? images[7] : images[11]);
                    break;
            }
            return act;
        }
    }
}

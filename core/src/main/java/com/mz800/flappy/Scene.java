package com.mz800.flappy;

import com.mz800.flappy.awt.Color;
import com.mz800.flappy.awt.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import static com.mz800.flappy.Constants.*;
import static com.mz800.flappy.Device.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Scene {

    private static final String[] PS = {
        "shiba", "MegmI", "PenTA", "miki!", "sakra", "1,2,O", "??OkU", "OmoRI", "U-CaN", "QuoTe",
        "ayAko", "Uf,f,", "Chie?", "sAKE!", "Syogn", "bUSHi", "BakA!", "STONE", "Japan", "HArfE",
        "Sappr", "OhaYo", "Gohan", "Ramen", "Nemui", "Natsu", "Yuki!", "HeIwa", "Pice!", "MZ801",
        "Engla", "Rome!", "PaRie", "Letgo", "FrEnc", "AFewe", "GerMa", "Tokyo", "Prend", "OKU-H"
    };
    private static final int[] DELAYS = {
        // 19 1D 21 26 2C 33 3B 44 4E 59
//        65, 76, 87, 99, 126, 168, 196, 225, 258, 295
        8, 10, 11, 12, 16, 21, 25, 28, 32, 37
    };
    private final int num;
    private final byte[][] model = new byte[22][40];
    private final Chicken chicken;
    private final List<BlueEnemy> blueEnemies = new ArrayList<>();
    private final List<RedEnemy> redEnemies = new ArrayList<>();
    private final List<Ball> balls = new ArrayList<>();
    private final List<Mushroom> mushrooms = new ArrayList<>();
    static int gameSpeed = 5;
    private boolean blueBallFinished = false;
    int gameStep = 0;
    private int time = 1500;
    int mushroomsPicked = 0;
    private int extraCycles = 0;
    private int timeBlink = 0;

    private final VRAM vram;

    Scene(int number) {
        this(number, Device.vram);
    }

    Scene (int number, VRAM vram) {
        this.num = number;
        this.chicken = new Chicken(this);
        this.vram = vram;
    }

    byte[][] getModel() {
        return model;
    }

    Chicken getChicken() {
        return chicken;
    }

    VRAM getVRAM() {
        return vram;
    }

    void predrawScene(boolean preview, int lives, int score) {
        int max = memory[0xB000] & 0xFF;
        if (num > max) {
            return;
        }

        int addr = findLevel(num);
        for (int y = 0; y < 22; y++) {
            for (int x = 0; x < 40; x++) {
                model[y][x] = SPACE;
            }
        }
        vram.clear();
        drawBorder();
        drawHeader(lives, score);

        for (int i = 0; i < 10; i++) {
            int p = (memory[addr++] << 16) & 0xFF0000;
            p |= (memory[addr++] << 8) & 0xFF00;
            p |= (memory[addr++]) & 0xFF;
            drawWall(p, 2 * i + 1);
            drawWall(p, 2 * i + 2);
        }

        addr = placeObjects(Images.redBall, addr, RED_BALL);
        addr = placeObjects(Images.blueEnemyLeft2, addr, BLUE_ENEMY);
        addr = placeObjects(Images.redEnemyDown, addr, RED_ENEMY);
        addr = placeMushrooms(addr);
        addr = placeBlueBall(addr);
        placeBlueWall(addr);

        if (preview) {
            vram.image(1, 1, Images.chickenDown);
            String n = Integer.toString(num);
            while (n.length() < 3) {
                n = '0' + n;
            }
            vram.printText(13, 1, " [SCENE " + n + "] ", Color.yellow);
        }
    }

    void showScene(boolean preview) {
        predrawScene(preview, Main.lives, Main.previousScore+Main.score);
        vram.refresh();
    }

    private int findLevel(int x) {
        int a = 0xB001;
        for (int i = 1; i < x; i++) {
            a += 0x1E;
            for (int j = 0; j < 4; j++) {
                int no = memory[a++];
                a += 2 * no;
            }
            a += 4;
        }
        return a;
    }

    private int placeBlueBall(int addr) {
        int x = memory[addr++] & 0xFF;
        int y = memory[addr++] & 0xFF;
        vram.image(x, y, Images.blueBall);
        model[y][x] = BLUE_BALL;
        model[y][x + 1] = BLUE_BALL;
        model[y + 1][x] = BLUE_BALL;
        model[y + 1][x + 1] = BLUE_BALL;
        balls.add(new Ball(x, y, 0, true, this));
        return addr;
    }

    private int placeBlueWall(int addr) {
        int x = memory[addr++] & 0xFF;
        int y = memory[addr++] & 0xFF;
        vram.image(x, y, Images.blueWall);
        vram.image(x + 1, y, Images.blueWall);
        model[y][x] = BLUE_WALL;
        model[y][x + 1] = BLUE_WALL;
        return addr;
    }

    private int placeMushrooms(int addr) {
        int no = memory[addr++];
        for (int i = 0; i < no; i++) {
            int x = memory[addr++] & 0xFF;
            int y = memory[addr++] & 0xFF;
            vram.image(x, y, Images.mushroom);
            model[y][x] = MUSHROOM;
            mushrooms.add(new Mushroom(x, y, i, this));
        }
        return addr;
    }

    private int placeObjects(BufferedImage img, int addr, byte objType) {
        int no = memory[addr++];
        for (int i = 0; i < no; i++) {
            int x = memory[addr++] & 0xFF;
            int y = memory[addr++] & 0xFF;
            vram.image(x, y, img);
            switch (objType) {
                case RED_BALL:
                    balls.add(new Ball(x, y, i, false, this));
                    break;
                case BLUE_ENEMY:
                    blueEnemies.add(new BlueEnemy(x, y, i, this));
                    break;
                case RED_ENEMY:
                    redEnemies.add(new RedEnemy(x, y, i, this));
                    break;
            }
            byte sig = (byte) (objType | i);
            model[y][x] = sig;
            model[y][x + 1] = sig;
            model[y + 1][x] = sig;
            model[y + 1][x + 1] = sig;
        }
        return addr;
    }

    private void drawWall(int p, int y) {
        int mask = 0x800000;
        for (int i = 0; i < 19; i++) {
            if ((p & mask) != 0) {
                vram.image(2 * i + 1, y, Images.redWall);
                vram.image(2 * i + 2, y, Images.redWall);
                model[y][2 * i + 1] = WALL;
                model[y][2 * i + 2] = WALL;
            }
            mask >>= 1;
        }
    }

    private void drawBorder() {
        for (int i = 0; i < 40; i++) {
            vram.image(i, 0, Images.redWall);
            model[0][i] = WALL;
        }
        for (int i = 1; i <= 20; i++) {
            vram.image(0, i, Images.redWall);
            vram.image(39, i, Images.redWall);
            model[i][0] = WALL;
            model[i][39] = WALL;
        }
        for (int i = 0; i < 40; i++) {
            vram.image(i, 21, Images.redWall);
            model[21][i] = WALL;
        }
    }

    static void drawSimpleBorder() {
        for (int i = 0; i < 40; i++) {
            Device.vram.imageNoOfs(i, 0, Images.redWall);
        }
        for (int i = 1; i < 24; i++) {
            Device.vram.imageNoOfs(0, i, Images.redWall);
            Device.vram.imageNoOfs(39, i, Images.redWall);
        }
        for (int i = 0; i < 40; i++) {
            Device.vram.imageNoOfs(i, 24, Images.redWall);
        }
    }

    /*
    private void printModel() {
        for (int y = 0; y < 22; y++) {
            for (int x = 0; x < 40; x++) {
                String v = Integer.toHexString(model[y][x]);
                if (v.length() < 2) {
                    v = '0' + v;
                }
                System.out.print(v + ' ');
            }
            System.out.println();
        }
    }
    */

    // --------------------------------
    private void moveBlueEnemies1() {
        Iterator<BlueEnemy> iter = blueEnemies.iterator();
        while (iter.hasNext()) {
            BlueEnemy e = iter.next();
            e.move1();
            if (e.isFinished()) {
                iter.remove();
            }
        }
    }

    private void moveBlueEnemies2() {
        Iterator<BlueEnemy> iter = blueEnemies.iterator();
        while (iter.hasNext()) {
            BlueEnemy e = iter.next();
            e.move2();
            if (e.isFinished()) {
                iter.remove();
            }
        }
    }

    private void moveRedEnemies1() {
        Iterator<RedEnemy> iter = redEnemies.iterator();
        while (iter.hasNext()) {
            RedEnemy e = iter.next();
            e.move();
            if (e.isFinished()) {
                iter.remove();
            }
        }
    }

    private void moveChicken1() {
        chicken.move1();
    }

    private void moveChicken2() {
        chicken.move2();
    }

    Ball findRedBall(int num) {
        for (Ball b : balls) {
            if (b.number == num && !b.isBlue) {
                return b;
            }
        }
        return null;
    }

    Ball findBlueBall() {
        for (Ball b : balls) {
            if (b.isBlue) {
                return b;
            }
        }
        return null;
    }

    private void moveBalls() {
        sortBalls();
        Iterator<Ball> iter = balls.listIterator();
        while (iter.hasNext()) {
            Ball b = iter.next();
            b.move();
            if (b.isFinished()) {
                iter.remove();
            }
        }
        if (chicken.crashStatus != 0) {
            chicken.chickenEaten = 0x81;
            chicken.crashStatus = 0;
        }
        Ball bb = findBlueBall();
        if (model[bb.y + 2][bb.x] == BLUE_WALL && model[bb.y + 2][bb.x + 1] == BLUE_WALL) {
            blueBallFinished = true;
        }
    }

    private void sortBalls() {
        Collections.sort(balls, new Comparator<Ball>() {
            @Override
            public int compare(Ball b1, Ball b2) {
                return b2.y - b1.y;
            }
        });
    }

    BlueEnemy findBlueEnemy(int num) {
        for (BlueEnemy e : blueEnemies) {
            if (e.number == num) {
                return e;
            }
        }
        return null;
    }

    RedEnemy findRedEnemy(int num) {
        for (RedEnemy e : redEnemies) {
            if (e.number == num) {
                return e;
            }
        }
        return null;
    }

    private boolean checkGameFinished() {
        if (Main.cheating && keyboard.otherKey() == 106) {
            blueBallFinished = true;
        }
        if (chicken.chickenEaten == 0 && blueBallFinished) {
            music.stop();
            chicken.jumping();
            // compute score
            while (mushroomsPicked > 0) {
                Main.score += 10;
                mushroomsPicked--;
                drawMushroomsHeader();
                printScore(Main.score+Main.previousScore);
                vram.refresh();
                Main.wait(3);
            }
            while (time > 0) {
                time -= 13;
                if (time < 13) {
                    time = 0;
                }
                printTime();
                Main.score += 10;
                printScore(Main.score+Main.previousScore);
                vram.refresh();
                Main.wait(1);
            }
            Main.wait(0x1E);
            music.stop();
            vram.emptyRect(1, 1, 38, 20 * 8);
            chicken.returnToHome();
            return true;
        }
        return false;
    }

    int gameLoop() throws Exception {
        int wait = DELAYS[gameSpeed];
        keyboard.clear();
        while (true) {
            keyboard.userAction();
            if (keyboard.userAction == ESC) {
                return 0;
            }
            extraCyclesIfChickenEaten();
            moveChicken1();
            if (checkGameFinished()) {
                return 1;
            }
            gameStep = 1;
            moveBalls();
            moveBlueEnemies1();
            moveRedEnemies1();
            decreaseTime();
            vram.refresh();
            waitAndTestGameEnd();
            int r = Main.wait(wait);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }
            extraCyclesIfChickenEaten();
            moveChicken2();
            gameStep = 0;
            moveBalls();
            firing();
            if (checkGameFinished()) {
                return 1;
            }
            moveBlueEnemies2();
            moveRedEnemies1();
            vram.refresh();
            waitAndTestGameEnd();
            r = Main.wait(wait);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }
            // extra game cycles
            if (extraCycles != 0) {
                extraCycles--;
                if (extraCycles == 0) {
                    music.stop();
                    return 0;
                }
            }
        }
    }

    private void decreaseTime() {
        time--;
        if (time < 0) {
            time = 0;
        }
        printTime();
        // todo: extra counting of time cycles
    }

    private void extraCyclesIfChickenEaten() {
        if ((chicken.chickenEaten & 0x7F) == 1) {
            extraCycles = 0x18;
        }
    }

    private void waitAndTestGameEnd() {
        if (time <= 0) {
            chicken.setChickenEaten();
        }
    }

    private void drawHeader(int lives, int score) {
        for (int i = 0; i < 40; i++) {
            vram.imageNoOfs(i, 0, Images.redWall);
        }
        for (int i = 1; i < 3; i++) {
            vram.imageNoOfs(0, i, Images.redWall);
            vram.imageNoOfs(11, i, Images.redWall);
            vram.imageNoOfs(26, i, Images.redWall);
            vram.imageNoOfs(39, i, Images.redWall);
        }
        for (int i = 0; i < 40; i++) {
            vram.imageNoOfs(i, 3, Images.redWall);
        }
        vram.imageNoOfs(13, 1, Images.score);
        vram.imageNoOfs(28, 1, Images.time);
        vram.imageNoOfs(29, 2, Images.cDbSoft);
        int x = 1;
        for (int i = 0; i < lives; i++) {
            vram.imageNoOfs(x, 1, Images.chickenDown);
            x += 2;
        }
        drawMushroomsHeader();
        printScore(score);
        printTime();
    }

    void drawMushroomsHeader() {
        int x = 13;
        for (int i = 1; i <= 13; i++) {
            if (i <= mushroomsPicked) {
                vram.imageNoOfs(x, 2, Images.mushroom);
            } else {
                vram.emptyRectNoOfs(x, 2, 1, 8);
            }
            x++;
        }
    }

    void printScore(int score) {
        printNumber(19, 1, score, 6, Color.YELLOW);
    }

    private void printTime() {
        if (time > 0) {
            printNumber(33, 1, time, 5, Color.YELLOW);
        } else {
            timeBlink++;
            if ((timeBlink & 2) == 0) {
                printNumber(33, 1, time, 5, Color.YELLOW);
            }
            else {
                vram.emptyRectNoOfs(33, 1, 5, 8);
            }
        }
    }

    private void printNumber(int x, int y, int value, int digits, Color color) {
        String s = Integer.toString(value);
        while (s.length() < digits) {
            s = '0' + s;
        }
        vram.printText(x, y, s, color);
    }

    private void firing() {
        if (chicken.chickenEaten == 0 && keyboard.userAction == FIRE) {
            fireOneMushroom();
        }
        Iterator<Mushroom> iter = mushrooms.iterator();
        while (iter.hasNext()) {
            Mushroom m = iter.next();
            m.move();
            if (m.status == 3) {
                iter.remove();
            }
        }
    }

    private void fireOneMushroom() {
        for (Mushroom m : mushrooms) {
            if (m.status == 1) {
                mushroomsPicked--;
                drawMushroomsHeader();
                m.direction = keyboard.lastUserAction;
                switch (keyboard.lastUserAction) {
                    case UP:
                        m.x = chicken.x + 1;
                        m.y = chicken.y - 1;
                        m.flightLength = 6;
                        break;
                    case DOWN:
                        m.x = chicken.x;
                        m.y = chicken.y + 2;
                        m.flightLength = 0;
                        break;
                    case LEFT:
                        m.x = chicken.x - 1;
                        m.y = chicken.y;
                        m.flightLength = 0x0F;
                        break;
                    default: // right
                        m.x = chicken.x + 2;
                        m.y = chicken.y;
                        m.flightLength = 0x0F;
                        break;
                }
                m.status = 2;
                return;
            }
        }
    }

    void showSceneNumberScreen() {
        music.stop();
        predrawSceneNumberScreen(Main.lives, Main.score+Main.previousScore);
        vram.refresh();
        Main.wait(0xF0);
    }

    void predrawSceneNumberScreen(int lives, int score) {
        vram.clear();
        drawHeader(lives, score);
        for (int i = 0; i <= 21; i++) {
            for (int j = 0; j < 40; j++) {
                vram.image(j, i, Images.redWall);
            }
        }
        vram.imageNoOfs(16, 10, Images.sceneYellow);
        printNumber(22, 10, num, 3, Color.YELLOW);
    }

    int chickenOutOfHome(int lives) {
        int y = 0;
        for (int i = 0; i < 4; i++) {
            y++;
            vram.imageNoOfs(1, y, Images.chickenJump1);
            vram.refresh();
            int r = Main.wait(0x0C);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }
            vram.imageNoOfs(1, y, Images.chickenJump2);
            vram.refresh();
            r = Main.wait(0x0C);
            if (r != Main.NORMAL_WAIT) {
                return r;
            }
            vram.emptyRectNoOfs(1, y, 2, 16);
        }
        vram.image(1, 1, Images.chickenDown);
        model[1][1] = CHICKEN;
        model[1][2] = CHICKEN;
        model[2][1] = CHICKEN;
        model[2][2] = CHICKEN;
        vram.image(1, 0, Images.redWall);
        vram.image(2, 0, Images.redWall);
        vram.refresh();
        int r = Main.wait(0x28);
        if (r != Main.NORMAL_WAIT) {
            return r;
        }
        if (lives > 1) {
            xStep1(lives, 3);
            xStep2(lives, 2);
            xStep1(lives, 2);
            xStep2(lives, 1);
            xStep1(lives, 1);
            int x = 1;
            for (int i = 0; i < lives - 1; i++) {
                vram.imageNoOfs(x, 1, Images.chickenDown);
                vram.refresh();
                r = Main.wait(0x28);
                if (r != Main.NORMAL_WAIT) {
                    return r;
                }
                x += 2;
            }
        }
        return Main.NORMAL_WAIT;
    }

    private void xStep1(int lives, int x) {
        for (int i = 0; i < lives - 1; i++) {
            vram.imageNoOfs(x, 1, Images.chickenLeft);
            x += 2;
        }
        vram.refresh();
        Main.wait(0x1E);
    }

    private void xStep2(int lives, int x) {
        for (int i = 0; i < lives - 1; i++) {
            vram.imageNoOfs(x, 1, Images.chickenLeft1);
            x += 2;
        }
        vram.emptyRectNoOfs(x, 1, 1, 16);
        vram.refresh();
        Main.wait(0x0F);
    }

    String printPassword(int n) {
        // 5292
//; 0 ... black background only
//; 1 ... blue text on black background
//; 2 ... red text on black background
//; 3 ... yellow text on black background
//; 4 ... yellow background only
//; 5 ... red text on yellow background
//; 6 ... blue text on yellow background
//; 7 ... black text on yellow background
        drawBorder();
        vram.imageNoOfs(13, 9, Images.sceneRed);
        int from = n * 5 - 4;
        int to = n * 5;
        printNumber(19, 9, from, 3, Color.yellow);
        vram.printText(23, 9, "-", Color.RED);
        printNumber(25, 9, to, 3, Color.yellow);
        vram.printText(13, 11, "Key word", Color.RED);
        // 2266
        String pw = PS[n - 1];
        vram.printText(22, 11, pw, Color.YELLOW);
        vram.printText(13, 15, "Hit", Color.BLUE);
        vram.printText(17, 15, "Space key", Color.BLUE);
        vram.refresh();
        // wait for space key
        keyboard.clear();
        while (!keyboard.spaceKey()) {
            Main.wait(10);
        }
        return pw;
    }

    int gameOver() {
        keyboard.clear();
        vram.emptyRectNoOfs(10, 7, 20, 11 * 8);
        drawGameOverText();
        vram.printText(11, 13, "Replay...", Color.RED);
        vram.printText(20, 13, "Space key", Color.YELLOW);
        vram.printText(11, 16, "Menu.....", Color.RED);
        vram.printText(20, 16, "Enter key", Color.YELLOW);
        int x;
        for (int i = 0; i < 0x30; i++) {
            vram.emptyRectNoOfs(11, 8, 18, 16);
            vram.refresh();
            x = testKey();
            if (x != 0) {
                return x;
            }
            drawGameOverText();
            vram.refresh();
            x = testKey();
            if (x != 0) {
                return x;
            }
        }
        return INTRO;
    }

    private int testKey() {
        for (int i = 0; i < 10; i++) {
            if (keyboard.spaceKey()) {
                return GAME;
            }
            if (keyboard.enterKey()) {
                return SETUP;
            }
            Main.wait(5);
        }
        return 0;
    }

    private void drawGameOverText() {
        vram.imageNoOfs(11, 8, Images.letterG);
        vram.imageNoOfs(13, 8, Images.letterA);
        vram.imageNoOfs(15, 8, Images.letterM);
        vram.imageNoOfs(17, 8, Images.letterE);
        vram.imageNoOfs(21, 8, Images.letterO);
        vram.imageNoOfs(23, 8, Images.letterV);
        vram.imageNoOfs(25, 8, Images.letterE);
        vram.imageNoOfs(27, 8, Images.letterR);
    }

    static int checkPass(String s) {
        for (int i = 0, maxi = PS.length; i < maxi; i++) {
            if (PS[i].equals(s)) {
                return i;
            }
        }
        return -1;
    }

//    private static long debugTime = System.nanoTime();
//     static void printTime(String what) {
//        long now = System.nanoTime();
//        long diff = now - debugTime;
////        Log.d("Debug", what + " - It took " + diff);
//        Log.d("Debug", String.format("%s - It took %,d", what, diff));
//        debugTime = now;
//    }
}

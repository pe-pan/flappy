package com.mz800.flappy;

import static com.mz800.flappy.Constants.*;
import static com.mz800.flappy.Device.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Ball extends Moveable {

    private Chicken chicken;
    boolean isBlue;
    int status;
    private int killStatus;

    Ball(int x, int y, int num, boolean blue, Scene scene) {
        super(x, y, blue ? BLUE_BALL : RED_BALL, num, scene);
        this.isBlue = blue;
        this.chicken = scene.getChicken();
    }

    void move() {
        if (status != 0) {
            // ball crash
            if (status < 6) {
                status++;
                switch (status) {
                    case 1:
                        drawMe(Images.ballCrash1); // + fill model 2x2 with 0x70;
                        model[y][x] = WALL;
                        model[y][x + 1] = WALL;
                        model[y + 1][x] = WALL;
                        model[y + 1][x + 1] = WALL;
                        return;
                    case 2:
                        drawMe(Images.ballCrash2);
                        return;
                    case 3:
                        empty2x1(x, y);
                        vram.image(x, y + 1, Images.ballCrash3); // 2x8
                        return;
                    case 4:
                        vram.image(x, y + 1, Images.ballCrash4); // 2x8
                        return;
                    default:
                        empty2x1(x, y + 1);
                        finished = true;
                }
            }
        } else {
            int m1 = model[y + 2][x];
            int m2 = model[y + 2][x + 1];
            if (m1 >= RED_BALL || m2 >= RED_BALL) {
                killStatus = 0;
                return;
            }
            int m1x = m1 & 0x0F;
            int m2x = m2 & 0x0F;
            if (m1x != 0x0F && m2x != 0x0F) {
                if (m1 >= CHICKEN) {
                    int obj = m1 & 0xF0;
                    if (obj == CHICKEN) {
                        chicken.crashChicken();
                    }
                    if (obj == BLUE_ENEMY) {
                        crashBlueEnemy(m1);
                    }
                    if (obj == RED_ENEMY) {
                        crashRedEnemy(m1);
                    }
                }
                if (m1 != m2) {
                    if (m2 >= CHICKEN) {
                        int obj = m2 & 0xF0;
                        if (obj == CHICKEN) {
                            chicken.crashChicken();
                        }
                        if (obj == BLUE_ENEMY) {
                            crashBlueEnemy(m2);
                        }
                        if (obj == RED_ENEMY) {
                            crashRedEnemy(m2);
                        }
                    }
                }
            }
            if (m1 >= CHICKEN || m2 >= CHICKEN) {
                return;
            }
            empty2x1(x, y);
            y++;
            drawMe(isBlue ? Images.blueBall : Images.redBall);
        }
    }

    void crashBlueEnemy(int m) {
        BlueEnemy e = scene.findBlueEnemy(m & 0x0F);
        e.crashBlueEnemy();
        killStatus++;
        Main.score += killStatus * 30;
        scene.printScore(Main.score+Main.previousScore);
    }

    void crashRedEnemy(int m) {
        RedEnemy e = scene.findRedEnemy(m & 0x0F);
        e.crashRedEnemy(x);
        killStatus++;
        Main.score += killStatus * 50;
        scene.printScore(Main.score+Main.previousScore);
    }
}

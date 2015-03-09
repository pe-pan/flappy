package com.mz800.flappy;

import com.mz800.flappy.awt.BufferedImage;
import static com.mz800.flappy.Constants.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class RedEnemy extends Moveable {

    int delX, delY;
    private int selectedMove;
    private boolean stepStatus;
    boolean didMove;
    int sleepStatus;
    private int sleepSubStatus;
    private int mUp1, mUp2;
    private int mLeft1, mLeft2;
    private int mRight1, mRight2;
    private int mDown1, mDown2;
    private Chicken chicken;
    int hitX, hitY;
    private int cx, cy, cw, ch;

    RedEnemy(int x, int y, int num, Scene scene) {
        super(x, y, RED_ENEMY, num, scene);
        this.chicken = scene.getChicken();
    }

    void move() {
        if (y == 0) {
            if (x == 8) {
                // clear dead enemy
                x++;
                empty2x1(delX, delY + 1);
                finished = true;
            } else if (x < 8) {
                x++;
            }
        } else if (sleepStatus != 0) {
            drawSleeping();
        } else if (!didMove) {
            // try to find new direction
            mUp1 = model[y - 1][x];
            mUp2 = model[y - 1][x + 1];
            mLeft1 = model[y][x - 1];
            mLeft2 = model[y + 1][x - 1];
            mRight1 = model[y][x + 2];
            mRight2 = model[y + 1][x + 2];
            mDown1 = model[y + 2][x];
            mDown2 = model[y + 2][x + 1];
            if (selectedMove == 0) {
                setRandomMove();
            }
            if (selectedMove >= LEFT) { // left or right
                if (chicken.y == y) {
                    int diff = chicken.x - x;
                    if (diff < 0) {
                        diff = -diff;
                    }
                    if (diff >= 6) {
                        if (Math.random() < 0x10 / 256f) {
                            doMove(true);
                        } else {
                            leftOrRight();
                        }
                    } else {
                        if (Math.random() < 8 / 256f) {
                            doMove(true);
                        } else {
                            leftOrRight();
                        }
                    }
                } else {
                    upOrDown();
                }
            } else {
                // move up or down
                if (chicken.x == x) {
                    int diffY = chicken.y - y;
                    if (diffY < 0) {
                        diffY = -diffY;
                    }
                    if (diffY >= 6) {
                        if (Math.random() < 0x10 / 256) {
                            doMove(true);
                        } else {
                            upOrDown();
                        }
                    } else {
                        if (Math.random() < 8 / 256) {
                            doMove(true);
                        } else {
                            upOrDown();
                        }
                    }
                } else {
                    leftOrRight();
                }
            }
        } else {
            // continue last direction
            didMove = false;
            switch (selectedMove) {
                case UP:
                    empty2x1(x, y + 1);
                    y--;
                    drawMe(Images.redEnemyUp);
                    break;
                case DOWN:
                    empty2x1(x, y);
                    y++;
                    drawMe(Images.redEnemyDown);
                    break;
                case LEFT:
                    empty1x2(x + 1, y);
                    x--;
                    drawMe(Images.redEnemyLeft);
                    break;
                default: // RIGHT
                    empty1x2(x, y);
                    x++;
                    drawMe(Images.redEnemyRight);
            }
        }
    }

    private void leftOrRight() {
        if (chicken.x < x) {
            if (testMove(mLeft1, mLeft2)) {
                selectedMove = LEFT;
            }
            doMove(false);
        } else {
            if (testMove(mRight1, mRight2)) {
                selectedMove = RIGHT;
            }
            doMove(false);
        }
    }

    private void upOrDown() {
        if (chicken.y < y) {
            if (testMove(mUp1, mUp2)) {
                selectedMove = UP;
            }
            doMove(false);
        } else {
            if (testMove(mDown1, mDown2)) {
                selectedMove = DOWN;
            }
            doMove(false);
        }
    }

    private void doMove(boolean rnd) {
        if (rnd) {
            setRandomMove();
        }
        if (Math.random() < 0x0a / 256) {
            setRandomMove();
        }
        switch (selectedMove) {
            case UP:
                if (!testMove(mUp1, mUp2)) {
                    drawMe(Images.redEnemyUp);
                    selectedMove = 0;
                    didMove = false;
                } else {
                    stepStatus = !stepStatus;
                    drawMe2x3(x, y - 1, stepStatus ? Images.redEnemyUpS2 : Images.redEnemyUpS1);
                    didMove = true;
                }
                break;
            case DOWN:
                if (!testMove(mDown1, mDown2)) {
                    drawMe(Images.redEnemyDown);
                    selectedMove = 0;
                    didMove = false;
                } else {
                    stepStatus = !stepStatus;
                    drawMe2x3(x, y, stepStatus ? Images.redEnemyDownS2 : Images.redEnemyDownS1);
                    didMove = true;
                }
                break;
            case LEFT:
                if (!testMove(mLeft1, mLeft2)) {
                    drawMe(Images.redEnemyLeft);
                    selectedMove = 0;
                    didMove = false;
                } else {
                    drawMe3x2(x - 1, y, Images.redEnemyLeftS2);
                    didMove = true;
                }
                break;
            default:
                if (!testMove(mRight1, mRight2)) {
                    drawMe(Images.redEnemyRight);
                    selectedMove = 0;
                    didMove = false;
                } else {
                    drawMe3x2(x, y, Images.redEnemyRightS2);
                    didMove = true;
                }
        }
    }

    // TODO: refactor this
    private boolean testMove(int m1, int m2) {
        int f = m1 & 0xF0;
        boolean chick = (f == CHICKEN);
        boolean wall = (f > CHICKEN);
        f = m2 & 0xF0;
        chick |= (f == CHICKEN);
        wall |= (f > CHICKEN);
        if (wall) {
            return false;
        }
        if (!chick && !wall) {
            return true;
        }
        if (chicken.chickenEaten == 0) {
            chicken.setChickenEaten();
            return true;
        }
        return false;
    }

    private void setRandomMove() {
        int rnd = (int) (Math.random() * 1000);
        selectedMove = (rnd & 3) + 1;
    }

    private void drawMe2x3(int x, int y, BufferedImage img) {
        vram.image(x, y, img); // 2 x 24
        model[y][x] = sig;
        model[y][x + 1] = sig;
        model[y + 1][x] = sig;
        model[y + 1][x + 1] = sig;
        model[y + 2][x] = sig;
        model[y + 2][x + 1] = sig;
        cx = x;
        cy = y;
        cw = 2;
        ch = 3;
    }

    private void drawMe3x2(int x, int y, BufferedImage img) {
        vram.image(x, y, img); // 3 x 16
        model[y][x] = sig;
        model[y][x + 1] = sig;
        model[y][x + 2] = sig;
        model[y + 1][x] = sig;
        model[y + 1][x + 1] = sig;
        model[y + 1][x + 2] = sig;
        cx = x;
        cy = y;
        cw = 3;
        ch = 2;
    }

    private void drawSleeping() {
        switch (sleepStatus) {
            case 1:
                if (didMove) {
                    // call 41e3
                    vram.emptyRect(cx, cy, cw, ch * 8);
                    for (int i = 0; i < ch; i++) {
                        for (int j = 0; j < cw; j++) {
                            model[cy + i][cx + j] = 0;
                        }
                    }
                }
                selectedMove = 0;
                didMove = false;
                // call 422f
                drawMe(Images.redEnemySleep1);
                sleepStatus++;
                return;
            case 2:
                drawMe(Images.redEnemySleep2);
                sleepStatus++;
                return;
            case 3:
                drawMe(Images.redEnemySleep3);
                sleepStatus++;
                return;
            case 4:
                drawMe(Images.redEnemySleep4);
                sleepStatus++;
                return;
            case 5:
                drawMe(Images.redEnemyDown);
                sleepStatus++;
                return;
        }
        if (sleepStatus < 0x18) {
            sleepStatus++;
            return;
        }
        if (sleepStatus == 0x18) {
            sleepSubStatus = 0;
            drawMe(Images.redEnemySleep6);
            sleepStatus++;
            return;
        }
        if (sleepStatus < 0x92) {
            if (sleepSubStatus == 0x0c) {
                sleepSubStatus++;
                drawMe(Images.redEnemySleep5);
                return;
            }
            if (sleepSubStatus == 0x18) {
                sleepSubStatus = 0;
                drawMe(Images.redEnemySleep6);
                return;
            }
            sleepSubStatus++;
            sleepStatus++;
            return;
        }
        if (sleepStatus == 0x92) {
            sleepSubStatus = 0;
            drawMe(Images.redEnemyDown);
            sleepStatus++;
            return;
        }
        if (sleepStatus < 0xA0) {
            sleepStatus++;
            return;
        }
        sleepStatus = 0;
    }

    void crashRedEnemy(int ballX) {
        if (y == 0) {
            return;
        }
        if (sleepStatus < 2 && didMove) {
            int tmpX, tmpY;
            switch (selectedMove) {
                case UP:
                    tmpY = y + 1;
                    y--;
                    empty2x1(x, tmpY);
                    break;
                case DOWN:
                    tmpY = y + 2;
                    empty2x1(x, tmpY);
                    break;
                case LEFT:
                    if (ballX < x) {
                        tmpX = x + 1;
                        x--;
                    } else {
                        tmpX = x - 1;
                    }
                    empty1x2(tmpX, y);
                    break;
                default:
                    if (x + 1 < ballX) {
                        tmpX = x;
                        x++;
                    } else {
                        tmpX = x + 2;
                    }
                    empty1x2(tmpX, y);
                    break;
            }
            selectedMove = 0;
        }
        empty2x1(x, y);
        model[y + 1][x] = 0x4F;
        model[y + 1][x + 1] = 0x4F;
        vram.image(x, y + 1, Images.redEnemyCrash1); // 2x8
        delX = x;
        delY = y;
        x = 0;
        y = 0;
    }
}

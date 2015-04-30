package com.mz800.flappy;

import static com.mz800.flappy.Constants.*;
import static com.mz800.flappy.Device.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Mushroom extends Moveable {

    private static final int LAY_FLY = 0;
    private static final int PICKED = 1;
    private static final int FIRST_FLY = 2;
    private static final int NOT_AVAILABLE = 3;
    int direction;
    int status;
    int flightLength;
    private int origX, origY;

    Mushroom(int x, int y, int num, Scene scene) {
        super(x, y, MUSHROOM, num, scene);
        this.status = LAY_FLY;
    }

    void move() {
        if (status == PICKED || status == NOT_AVAILABLE) {
            return;
        }
        origX = x;
        origY = y;
        int mCurr = model[y][x];
        int mCurrObj = mCurr & 0xF0;
        if (status == FIRST_FLY) {
            // flying
            status = LAY_FLY;
            switch (mCurrObj) {
                case BLUE_ENEMY:
                    hitBlueEnemy(mCurr, mCurr);
                    return;
                case RED_ENEMY:
                    hitRedEnemy(mCurr, mCurr);
                    return;
                case SPACE:
                case MUSHROOM:
                    drawMe();
                    return;
                default:
                    hitChicken(mCurr);
            }
        } else {
            // laying
            if (mCurrObj == CHICKEN) {
                hitChicken(mCurr);
                return;
            }
            if (mCurrObj >= RED_BALL) {
                status = NOT_AVAILABLE;
                return;
            }
            if (flightLength != 0) {
                if (mCurrObj == RED_ENEMY) {
                    hitRedEnemy(mCurr, mCurr);
                    return;
                }
                if (mCurrObj == BLUE_ENEMY) {
                    hitBlueEnemy(mCurr, mCurr);
                    return;
                }
                flightLength--;
                if (flightLength > 0) {
                    switch (direction) {
                        case UP:
                            y--;
                            break;
                        case LEFT:
                            x--;
                            break;
                        default: // RIGHT
                            x++;
                            break;
                    }
                    int m = model[y][x];
                    int mObj = m & 0xF0;
                    if (mObj >= RED_BALL) {
                        x = origX;
                        y = origY;
                        status = LAY_FLY;
                        flightLength = 0;
                    }
                    else {
                        switch (mObj) {
                            case CHICKEN:
                                hitChicken(mCurrObj);
                                return;
                            case BLUE_ENEMY:
                                hitBlueEnemy(mCurrObj, m);
                                return;
                            case RED_ENEMY:
                                hitRedEnemy(mCurrObj, m);
                                return;
                            default:
                                clearMe(origX, origY);
                                drawMe();
                                return;
                        }
                    }
                }
            }
            // falls down
            y++;
            int mCurr2 = model[y][x];
            int mCurrObj2 = mCurr2 & 0xF0;
            if (mCurrObj2 >= RED_BALL) {
                x = origX;
                y = origY;
                if (model[y][x] == 0) {
                    drawMe();
                }
                return;
            }
            if (mCurrObj2 == CHICKEN) {
                hitChicken(mCurrObj);
                return;
            }
            if (mCurrObj2 == BLUE_ENEMY) {
                hitBlueEnemy(mCurrObj, mCurr2);
                return;
            }
            if (mCurrObj2 == RED_ENEMY) {
                hitRedEnemy(mCurrObj, mCurr2);
                return;
            }
            // 458e
            // ???
            // 45ad
            if (mCurr2 == 0 || mCurrObj2 == MUSHROOM) {
                clearMe(origX, origY);
                drawMe();
            }
        }

    }

    private void hitChicken(int mOrig) {
        if ((mOrig & 0xF0) == MUSHROOM) {
            clearMe(origX, origY);
        }
        status = PICKED;
        scene.mushroomsPicked++;
        scene.drawMushroomsHeader();
    }

    private void hitBlueEnemy(int mOrig, int mCurr) {
        if ((mOrig & 0xF0) == MUSHROOM) {
            clearMe(origX, origY);
        }
        status = NOT_AVAILABLE;
        BlueEnemy be = scene.findBlueEnemy(mCurr & 0x0F);
        be.direction = 2;
    }

    private void hitRedEnemy(int mOrig, int mCurr) {
        if ((mOrig & 0xF0) == MUSHROOM) {
            clearMe(origX, origY);
        }
        status = NOT_AVAILABLE;
        RedEnemy re = scene.findRedEnemy(mCurr & 0x0F);
        if (re.sleepStatus >= 2) {
            re.didMove = false;
        }
        re.sleepStatus = 1;
        re.hitX = x;
        re.hitY = y;
    }

    void clearMe(int x, int y) {
        model[y][x] = 0;
        vram.emptyRect(x, y, 1, 8);
    }

    void drawMe() {
        model[y][x] = sig;
        vram.image(x, y, Images.mushroom);
    }
}

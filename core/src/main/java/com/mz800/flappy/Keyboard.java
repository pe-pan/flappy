package com.mz800.flappy;

import static com.mz800.flappy.Constants.*;

/**
 * Keyboard handler.
 * @author Petr Slechta
 */
class Keyboard {

    private static final Keyboard me = new Keyboard();
    private static final boolean DEBUG = false;

    static Keyboard getInstance() {
        return me;
    }
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean space = false;
    private boolean esc = false;
    private boolean enter = false;
    private boolean up2 = false;
    private boolean down2 = false;
    private boolean left2 = false;
    private boolean right2 = false;
    private boolean space2 = false;
    private boolean esc2 = false;
    private boolean enter2 = false;
    private int otherKey = 0;
    private int keyPressed = 0;
    int userAction;
    int lastUserAction;

    private Keyboard() {
    }

    void keyDown(int key) {
        switch (key) {
            case 38:
                up = true;
                up2 = true;
                break;
            case 40:
                down = true;
                down2 = true;
                break;
            case 37:
                left = true;
                left2 = true;
                break;
            case 39:
                right = true;
                right2 = true;
                break;
            case 27:
                esc = true;
                esc2 = true;
                break;
            case 32:
                space = true;
                space2 = true;
                break;
            case 10:
                enter = true;
                enter2 = true;
                break;
            default:
                otherKey = key;
        }
        if (DEBUG) {
            printStatus();
        }
    }

    void keyUp(int key) {
        switch (key) {
            case 38: // up
                up = false;
                break;
            case 40: // down
                down = false;
                break;
            case 37: // left
                left = false;
                break;
            case 39: // right
                right = false;
                break;
            case 27: // esc
                esc = false;
                break;
            case 32: // space
                space = false;
                break;
            case 10:
                enter = false;
                break;
            default:
                otherKey = 0;
        }
        if (DEBUG) {
            printStatus();
        }
    }

    void userAction() {
        if (space || space2) {
            userAction = FIRE;
        } else if (up) {
            userAction = UP;
            lastUserAction = UP;
        } else if (down) {
            userAction = DOWN;
            lastUserAction = DOWN;
        } else if (left) {
            userAction = LEFT;
            lastUserAction = LEFT;
        } else if (right) {
            userAction = RIGHT;
            lastUserAction = RIGHT;
        } else if (esc || esc2) {
            userAction = ESC;
        } else if (userAction == 0) {
            if (space2) {
                userAction = FIRE;
            } else if (up2) {
                userAction = UP;
                lastUserAction = UP;
            } else if (down2) {
                userAction = DOWN;
                lastUserAction = DOWN;
            } else if (left2) {
                userAction = LEFT;
                lastUserAction = LEFT;
            } else if (right2) {
                userAction = RIGHT;
                lastUserAction = RIGHT;
            } else if (esc2) {
                userAction = ESC;
            } else {
                userAction = 0;
            }
        } else {
            userAction = 0;
        }
        space2 = false;
        up2 = false;
        down2 = false;
        left2 = false;
        right2 = false;
        esc2 = false;
        enter2 = false;
    }

    void keyPressed(int key) {
        keyPressed = key;
        if (DEBUG) {
            printStatus();
        }
    }

    boolean spaceKey() {
        return space || space2;
    }

    boolean enterKey() {
        return enter || enter2;
    }

    boolean escKey() {
        return esc || esc2;
    }

    int otherKey() {
        return otherKey;
    }

    void clearKeyPressed() {
        keyPressed = 0;
    }

    int getKeyPressed() {
        int tmp = keyPressed;
        keyPressed = 0;
        return tmp;
    }

    void clear() {
        space = false;
        up = false;
        down = false;
        left = false;
        right = false;
        esc = false;
        enter = false;
        space = false;
        up2 = false;
        down2 = false;
        left2 = false;
        right2 = false;
        esc2 = false;
        enter2 = false;
        space2 = false;
        userAction = 0;
    }

    private void printStatus() {
        System.out.println("Keyboard: "
                + up + " " + down + " " + left + " " + right + " / " + space + " " + esc + " " + enter + " // "
                + up2 + " " + down2 + " " + left2 + " " + right2 + " / " + space2 + " " + esc2 + " " + enter2 + " // "
                + otherKey + " " + keyPressed);
    }
}

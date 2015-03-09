package com.mz800.flappy;

import com.mz800.flappy.awt.BufferedImage;
import static com.mz800.flappy.Constants.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class BlueEnemy extends Moveable {
    
    private static final int BLUE_ENEMY_LEFT = 0;
    private static final int BLUE_ENEMY_RIGHT = 1;
    int direction; // LEFT, RIGHT, ...
    int delX, delY;
    private Chicken chicken;
    
    BlueEnemy(int x, int y, int num, Scene scene) {
        super(x, y, BLUE_ENEMY, num, scene);
        this.chicken = scene.getChicken();
    }
    
    private void tryLeft() {
        int m1 = model[y][x - 1];
        int m2 = model[y + 1][x - 1];
        if (m1 >= BLUE_ENEMY) {
            direction = BLUE_ENEMY_RIGHT; // cannot go
            return;
        }
        if (m1 >= CHICKEN) { // chicken
            if (m2 >= BLUE_ENEMY) {
                direction = BLUE_ENEMY_RIGHT; // cannot go
            } else {
                chicken.setChickenEaten();
            }
            return;
        }
        if (m2 >= BLUE_ENEMY) {
            direction = BLUE_ENEMY_RIGHT; // cannot go
            return;
        }
        if (m2 >= CHICKEN) {
            chicken.setChickenEaten();
            return;
        }

        // move left
        direction = BLUE_ENEMY_LEFT;
        empty1x2(x + 1, y);
        x--;
        drawMe(Images.blueEnemyLeft1);
    }
    
    private void tryRight() {
        int m1 = model[y][x + 2];
        int m2 = model[y + 1][x + 2];
        if (m1 >= BLUE_ENEMY) {
            direction = BLUE_ENEMY_LEFT; // cannot go
            return;
        }
        if (m1 >= CHICKEN) {
            if (m2 >= BLUE_ENEMY) {
                direction = BLUE_ENEMY_LEFT; // cannot go
            } else {
                chicken.setChickenEaten();
            }
            return;
        }
        if (m2 >= BLUE_ENEMY) {
            direction = BLUE_ENEMY_LEFT; // cannot go
            return;
        }
        if (m2 >= CHICKEN) {
            chicken.setChickenEaten();
            return;
        }

        // move right
        direction = BLUE_ENEMY_RIGHT;
        empty1x2(x, y);
        x++;
        drawMe(Images.blueEnemyRight1);
    }
    
    private void normalMove() {
        double left = direction == BLUE_ENEMY_LEFT ? 0.666 : 0.334;
        if (Math.random() <= left) {
            this.tryLeft();
        } else {
            this.tryRight();
        }
    }
    
    void move1() {
        if (y != 0) {
            // active_enemy
            if (direction >= 2) {
                drawEnemyBasedOnStatus(direction);
            } else if (y == chicken.y) {
                int diff = chicken.x - x;
                if (diff < 0) {
                    diff = -diff;
                    if (diff < 6) {
                        tryLeft();
                    } else if (diff >= 11) {
                        normalMove();
                    } else if (Math.random() <= 0.8125) {
                        tryLeft();
                    } else {
                        tryRight();
                    }
                } else {
                    if (diff < 6) {
                        tryRight();
                    } else if (diff >= 11) {
                        normalMove();
                    } else if (Math.random() <= 0.8125) {
                        tryRight();
                    } else {
                        tryLeft();
                    }
                }
            } else {
                normalMove();
            }
        } else { // y = 0
            if (x == 8) { // clean half enemy
                x++;
                empty2x1(delX, delY + 1);
                finished = true;
            } else if (x < 8) { // wait
                x++;
            }
        }
    }
    
    void move2() {
        if (y != 0) {
            // active_enemy
            if (direction >= 2) {
                drawEnemyBasedOnStatus(direction);
            } else if (direction == 0) {
                vram.image(x, y, Images.blueEnemyLeft2);
            } else {
                vram.image(x, y, Images.blueEnemyRight2);
            }
        } else { // y = 0
            if (x == 8) { // clean half enemy
                x++;
                empty2x1(delX, delY + 1);
                finished = true;
            } else if (x < 8) { // wait
                x++;
            }
        }
    }
    
    private void drawEnemyBasedOnStatus(int n) { // 4f37
        int n2 = n - 6;
        if (n2 < 0) {
            switch (n2 & 0xFF) {
                case 0xFC:
                    drawMe(Images.blueEnemySleep1);
                    break;
                case 0xFD:
                    drawMe(Images.blueEnemySleep2);
                    break;
                case 0xFE:
                    drawMe(Images.blueEnemySleep3);
                    break;
                case 0xFF:
                default:
                    drawMe(Images.blueEnemySleep4);
                    break;
            }
            direction++;
            return;
        }
        n2 = (n2 >> 4) & 0x0F;
        if (n2 < 1) {
            drawMe(Images.blueEnemyLeft2);
            direction++;
        } else if (n2 > 10) {
            n = n & 0xFF;
            if (n < 0xAE) {
                drawMe(Images.blueEnemySleep5);
                direction++;
            } else if (n < 0xC8) {
                drawMe(Images.blueEnemyLeft2);
                direction++;
            } else {
                drawMe(Images.blueEnemyLeft2);
                direction = 0;
            }
        } else {
            n2 = n2 >> 1;
            BufferedImage img = (n2 & 1) == 0 ? Images.blueEnemySleep5 : Images.blueEnemySleep6;
            drawMe(img);
            direction++;
        }
    }
    
    void crashBlueEnemy() {
        if (y != 0) {
            delX = x;
            delY = y;
            direction = 0;
            empty2x1(x, y);
            model[y + 1][x] = 0x3F;
            model[y + 1][x + 1] = 0x3F;
            vram.image(x, y + 1, Images.blueEnemyCrash1); // 2x8
            x = 1;
            y = 0;
        }
    }
}

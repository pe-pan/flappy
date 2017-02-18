package net.panuska.tlappy;

import net.panuska.tlappy.awt.BufferedImage;
import static net.panuska.tlappy.Constants.*;
import static net.panuska.tlappy.Device.*;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Chicken extends Moveable {

    private static final BufferedImage[] CHICKEN_NORMAL = {
        Images.chickenUp, Images.chickenDown, Images.chickenLeft, Images.chickenRight
    };
    private static final BufferedImage[] CHICKEN_FIRE_1 = {
        Images.chickenUpFire1, Images.chickenDownFire1, Images.chickenLeftFire1, Images.chickenRightFire1
    };
    private static final BufferedImage[] CHICKEN_FIRE_2 = {
        Images.chickenUpFire2, Images.chickenDownFire2, Images.chickenLeftFire2, Images.chickenRightFire2
    };
    private final Keyboard keyboard = Keyboard.getInstance();
    int chickenEaten;
    private int m1, m2, tmp;
    private boolean stepStatus;
    int crashStatus;
    private int actTmp;
    private boolean demoStep;

    Chicken(Scene scene) {
        super(1, 1, CHICKEN, 0, scene);
        keyboard.lastUserAction = DOWN;
    }

    BufferedImage selectImage(BufferedImage[] arr, int no) {
        return (no < 1 || no > arr.length) ? arr[arr.length - 1] : arr[no - 1];
    }

    void setChickenEaten() {
        if (chickenEaten == 0) {
            chickenEaten = 1;
        }
    }

    private void move1up() {
        m1 = model[y - 1][x];
        m2 = model[y - 1][x + 1];
        if (m1 >= WALL) {
            keyboard.userAction = 6;
            return;
        }
        if (m1 >= RED_BALL) {
            if (m2 >= WALL) {
                keyboard.userAction = 6;
            } else if (m2 >= RED_BALL) {
                if (m1 == m2) {
                    keyboard.userAction = 6;
                } else {
                    tmp = model[y][x - 1];
                    if (tmp >= RED_BALL) {
                        setRedBallFlag(m1);
                    }
                    tmp = model[y][x + 2];
                    if (tmp >= RED_BALL) {
                        setRedBallFlag(m2);
                    }
                    keyboard.userAction = 6;
                }
            } else if (m2 >= CHICKEN) {
                if (m1 > BLUE_BALL) {
                    keyboard.userAction = 6;
                } else {
                    tmp = model[y][x - 1];
                    if (tmp < RED_BALL) {
                        keyboard.userAction = 6;
                    } else {
                        setRedBallFlag(m1);
                        setChickenEaten();
                    }
                }
            } else {
                tmp = model[y][x - 1];
                if (tmp >= RED_BALL) {
                    setRedBallFlag(m1);
                }
                keyboard.userAction = 6;
            }
            return;
        }
        if (m1 >= CHICKEN) {
            if (m2 >= WALL) {
                keyboard.userAction = 6;
            } else if (m2 >= RED_BALL) {
                if (m2 >= BLUE_BALL) {
                    keyboard.userAction = 6;
                } else {
                    tmp = model[y][x + 2];
                    if (tmp < RED_BALL) {
                        keyboard.userAction = 6;
                    } else {
                        setRedBallFlag(m2);
                        setChickenEaten();
                    }
                }
            } else {
                setChickenEaten();
            }
            return;
        }
        // m1 <- empty
        if (m2 >= WALL) {
            keyboard.userAction = 6;
        } else if (m2 >= RED_BALL) {
            tmp = model[y][x + 2];
            if (tmp >= RED_BALL) {
                setRedBallFlag(m2);
            }
            keyboard.userAction = 6;
        } else if (m2 >= CHICKEN) {
            setChickenEaten();
        } else {
            model[y - 1][x] = CHICKEN;
            model[y - 1][x + 1] = CHICKEN;
            model[y + 1][x] = 0;
            model[y + 1][x + 1] = 0;
            stepStatus = !stepStatus;
            vram.image(x, y, stepStatus ? Images.chickenUp1 : Images.chickenUp2);
            y--;
        }
    }

    private void move1down() {
        m1 = model[y + 2][x];
        m2 = model[y + 2][x + 1];
        if (m1 >= WALL) {
            keyboard.userAction = 7;
            return;
        }
        if (m1 >= BLUE_BALL) {
            if (m2 >= RED_BALL || m2 < BLUE_BALL) {
                testBallFlag(y + 4, x + 1, m2);
            }
            keyboard.userAction = 7;
            return;
        }
        if (m1 >= RED_BALL) {
            if (m2 >= WALL) {
                // do nothing
            } else if (m2 >= BLUE_BALL) {
                testBallFlag(y + 4, x - 1, m2);
            } else if (m2 >= RED_BALL) {
                if (m1 == m2) {
                    testBallFlag(y + 4, x, m1);
                } else {
                    testBallFlag(y + 4, x + 1, m2);
                    testBallFlag(y + 4, x - 1, m1);
                }
            } else if (m2 >= CHICKEN) {
                testBallFlag(y + 4, x - 1, m1);
                setChickenEaten();
            } else {
                testBallFlag(y + 4, x - 1, m1);
            }
            keyboard.userAction = 7;
            return;
        }
        if (m1 >= CHICKEN) {
            if (m2 >= BLUE_BALL) {
                // nothing
            } else if (m2 >= RED_BALL) {
                testBallFlag(y + 4, x + 1, m2);
                setChickenEaten();
            } else {
                setChickenEaten();
            }
            keyboard.userAction = 7;
            return;
        }
        if (m2 >= BLUE_BALL) {
            keyboard.userAction = 7;
            return;
        }
        if (m2 >= RED_BALL) {
            testBallFlag(y + 4, x + 1, m2);
            keyboard.userAction = 7;
            return;
        }
        if (m2 >= CHICKEN) {
            setChickenEaten();
            return;
        }
        empty2x1(x, y);
        y++;
        stepStatus = !stepStatus;
        drawMe(stepStatus ? Images.chickenDown1 : Images.chickenDown2);
    }

    private void testBallFlag(int y, int x, int ball) {
        if (model[y][x] >= RED_BALL) {
            setRedBallFlag(ball);
        } else if (model[y][x + 1] >= RED_BALL) {
            setRedBallFlag(ball);
        }
    }

    private boolean testBallFlag2(int y, int x, int ball) {
        if (model[y][x] >= CHICKEN) {
            setRedBallFlag(ball);
            return true;
        } else if (model[y + 1][x] >= CHICKEN) {
            setRedBallFlag(ball);
            return true;
        }
        return false;
    }

    private void move1left() {
        m1 = model[y][x - 1];
        m2 = model[y + 1][x - 1];
        if (m1 >= WALL) {
            keyboard.userAction = 8;
            return;
        }
        if (m1 >= RED_BALL) {
            if (m2 >= WALL || m2 < RED_BALL) {
                keyboard.userAction = 8;
            } else if (m1 == m2) {
                if (!testBallFlag2(y, x - 3, m1)) {
                    // move ball
                    boolean blue = m1 >= BLUE_BALL;
                    Ball b = blue ? scene.findBlueBall() : scene.findRedBall(m1 & 0x0F);
                    empty1x2(b.x + 1, b.y);
                    b.x--;
                    b.drawMe(blue ? Images.blueBall : Images.redBall);
                    empty1x2(x + 1, y);
                    x--;
                    drawMe(Images.chickenLeft1);
                }
            }
            return;
        }
        if (m1 >= CHICKEN) {
            setChickenEaten();
            return;
        }
        if (m2 >= RED_BALL) {
            keyboard.userAction = 8;
            return;
        }
        if (m2 >= CHICKEN) {
            setChickenEaten();
            return;
        }
        empty1x2(x + 1, y);
        x--;
        drawMe(Images.chickenLeft1);
    }

    private void move1right() {
        m1 = model[y][x + 2];
        m2 = model[y + 1][x + 2];
        if (m1 >= WALL) {
            keyboard.userAction = 9;
            return;
        }
        if (m1 >= RED_BALL) {
            if (m2 >= WALL || m2 < RED_BALL) {
                keyboard.userAction = 9;
            } else if (m1 == m2) {
                if (!testBallFlag2(y, x + 4, m1)) {
                    // move ball
                    boolean blue = m1 >= BLUE_BALL;
                    Ball b = blue ? scene.findBlueBall() : scene.findRedBall(m1 & 0x0F);
                    empty1x2(b.x, b.y);
                    b.x++;
                    b.drawMe(blue ? Images.blueBall : Images.redBall);
                    empty1x2(x, y);
                    x++;
                    drawMe(Images.chickenRight1);
                }
            }
            return;
        }
        if (m1 >= CHICKEN) {
            if (m2 >= RED_BALL) {
                keyboard.userAction = 9;
            } else {
                setChickenEaten();
            }
            return;
        }
        if (m2 >= RED_BALL) {
            keyboard.userAction = 9;
            return;
        }
        if (m2 >= CHICKEN) {
            setChickenEaten();
            return;
        }
        empty1x2(x, y);
        x++;
        drawMe(Images.chickenRight1);
    }

    void move1() {
        if ((chickenEaten & 0x80) != 0) {
            // dead by ball
            tmp = chickenEaten & 0x7F;
            if (tmp == 0xE) {
                model[y][x] = 0;
                model[y][x + 1] = 0;
                vram.emptyRect(x, y, 2, 8);
            }
            if (tmp <= 0xE) {
                chickenEaten++;
            }
            return;
        }
        if (chickenEaten != 0) {
            // eaten
            if (chickenEaten >= 0x14) {
                vram.image(x, y, Images.chickenEaten1); // 2x8
                chickenEaten++;
            } else {
                if (chickenEaten == 1) {
                    if (keyboard.userAction == UP) {
                        // only VRAM update + 40 ... why?
                    }
                    vram.emptyRect(x, y, 2, 8);
                    model[y][x] = 0;
                    model[y][x + 1] = 0;
                    y++;
                }
                BufferedImage img = ((chickenEaten & 1) == 0) ? Images.chickenEaten1 : Images.chickenEaten2;
                vram.image(x, y, img); // 2x8
                chickenEaten++;
            }
            return;
        }
        if (keyboard.userAction == 0) {
            drawMe(selectImage(CHICKEN_NORMAL, keyboard.lastUserAction));
            return;
        }

        switch (keyboard.userAction) {
            case UP:
                move1up();
                break;
            case DOWN:
                move1down();
                break;
            case LEFT:
                move1left();
                break;
            case RIGHT:
                move1right();
                break;
            default:
                drawMe(selectImage(CHICKEN_FIRE_2, keyboard.lastUserAction));
                break;
        }
    }

    private void setRedBallFlag(int m) {
        if (m < BLUE_BALL) { // 0x5x
            Ball ball = scene.findRedBall(m & 0x0F);
            if (ball.status == 0) {
                ball.status = 1;
            }
        }
    }

    void move2() {
        if ((chickenEaten & 0x80) != 0) {
            int s = chickenEaten & 0x7f;
            if (s < 0x0e) {
                chickenEaten++;
            } else if (s == 0x0E) {
                empty2x1(x, y);
                chickenEaten++;
            }
            return;
        }

        if (chickenEaten != 0) {
            if (chickenEaten > 0x14) {
                drawMe2x1(Images.chickenEaten2);
                chickenEaten++;
                return;
            }
            if (chickenEaten == 1) {
                if (keyboard.userAction == UP) {    // bug fix: half of Flappy remained visible when eaten by Ghost
                    vram.emptyRect(x, y+2, 2, 8);
                    // only VRAM update + 40 ... why?
                }
                empty2x1(x, y);
                y++;
            }
            drawMe2x1((chickenEaten & 1) == 0 ? Images.chickenEaten1 : Images.chickenEaten2); // 2x8
            chickenEaten++;
            return;
        }

        switch (keyboard.userAction) {
            case 0:
                break;
            case UP:
                vram.emptyRect(x, y + 2, 2, 8);
                drawMe(Images.chickenUp); // vram updated differentway than coordinates - jen pro smer nahoru - proc?
                break;
            case DOWN:
                drawMe(Images.chickenDown);
                break;
            case LEFT:
                drawMe(Images.chickenLeft);
                break;
            case RIGHT:
                drawMe(Images.chickenRight);
                break;
            case FIRE:
                drawMe(selectImage(CHICKEN_FIRE_1, keyboard.lastUserAction));
                break;
            case 6:
                drawMe(Images.chickenUp);
                break;
            case 7:
                drawMe(Images.chickenDown);
                break;
            case 8:
                drawMe(Images.chickenLeft);
                break;
            default:
                drawMe(Images.chickenRight);
        }
    }

    void crashChicken() {
        if (chickenEaten > 0x80) {
            return;
        }
        if ((chickenEaten & 0x7F) != 0) {
            drawMe2x1(Images.chickenCrash1); //2x8
            setChickenEaten();
        } else {
            empty2x1(x, y);
            y++;
            model[y][x] = 0x2F;
            model[y][x + 1] = 0x2F;

            if (keyboard.userAction == UP || keyboard.userAction == DOWN) {
                if (scene.gameStep != 0) {
                    empty2x1(x, y + 1);
                }
            }
            drawMe2x1(Images.chickenCrash1); // 2x8
            setChickenEaten();
            crashStatus = 1;
        }
    }

    void jumping() {
        drawMe(Images.chickenDown);
        vram.refresh();
        Main.wait(0x14);
        for (int i = 0; i < 3; i++) {
            drawMe(Images.chickenJump1);
            vram.refresh();
            Main.wait(0x14);
            drawMe(Images.chickenJump2);
            vram.refresh();
            Main.wait(0x5);
            drawMe(Images.chickenDown);
            vram.refresh();
            Main.wait(0x0C);
        }
        Main.wait(10);
    }

    void returnToHome() {
        int destX = Main.lives * 2 - 1;
        while (true) {
            vram.image(x, y, Images.chickenGoHome);
            vram.refresh();
            Main.wait(1);
            vram.emptyRect(x, y, 1, 8);
            if (destX == x) {
                break;
            }
            if (destX < x) {
                x--;
            } else {
                x++;
            }
        }
        while (y > 1) {
            vram.image(x, y, Images.chickenGoHome);
            vram.refresh();
            Main.wait(1);
            vram.emptyRect(x, y, 1, 8);
            y--;
        }
        vram.imageNoOfs(destX, 1, Images.chickenJump1);
        vram.refresh();
        Main.wait(0x12);
        vram.imageNoOfs(destX, 1, Images.chickenJump2);
        vram.refresh();
        Main.wait(4);
        vram.imageNoOfs(destX, 1, Images.chickenDown);
        vram.refresh();
    }

    // ---------------
    static class DemoStepInfo {

        byte[] steps;
        int pStep;
        int cx;
        int cy;
    }

    void demoDoStep2(DemoStepInfo par) {
        int a = par.steps[par.pStep] & 0x7F;
        if (a > 6) {
            return;
        }
        par.pStep++;
        if (a == 0) {
            return;
        }
        if (a == FIRE) {
            vram.imageNoOfs(par.cx, par.cy, selectImage(CHICKEN_FIRE_1, actTmp));
        } else {
            if (a == UP) {
                vram.emptyRectNoOfs(par.cx, par.cy + 1, 2, 8);
                par.cy--;
            }
            vram.imageNoOfs(par.cx, par.cy, selectImage(CHICKEN_NORMAL, a));
        }
    }

    void demoDoStep1(DemoStepInfo par) {
        int a = par.steps[par.pStep];
        if (a > 6 || a == 0) {
            return;
        }
        if (a == FIRE) {
            vram.imageNoOfs(par.cx, par.cy, selectImage(CHICKEN_FIRE_2, actTmp));
            return;
        }
        actTmp = a;
        demoStep = !demoStep;
        switch (actTmp) {
            case UP:
                vram.imageNoOfs(par.cx, par.cy, demoStep ? Images.chickenUp1 : Images.chickenUp2);
                return;
            case DOWN:
                vram.emptyRectNoOfs(par.cx, par.cy, 2, 8);
                par.cy++;
                vram.imageNoOfs(par.cx, par.cy, demoStep ? Images.chickenDown1 : Images.chickenDown2);
                return;
            case LEFT:
                vram.emptyRectNoOfs(par.cx + 1, par.cy, 1, 16);
                par.cx--;
                vram.imageNoOfs(par.cx, par.cy, Images.chickenLeft1);
                return;
            default:
                vram.emptyRectNoOfs(par.cx, par.cy, 1, 16);
                par.cx++;
                vram.imageNoOfs(par.cx, par.cy, Images.chickenRight1);
        }
    }
}

package com.mz800.flappy;

import com.mz800.flappy.awt.BufferedImage;
import com.mz800.flappy.awt.Color;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Images {

    static BufferedImage redWall, blueWall;
    static BufferedImage redBall, blueBall;
    static BufferedImage mushroom;
    static BufferedImage chickenUp, chickenUp1, chickenUp2;
    static BufferedImage chickenDown, chickenDown1, chickenDown2;
    static BufferedImage chickenLeft, chickenLeft1;
    static BufferedImage chickenRight, chickenRight1;
    static BufferedImage chickenEaten1, chickenEaten2;
    static BufferedImage chickenUpFire1, chickenUpFire2;
    static BufferedImage chickenDownFire1, chickenDownFire2;
    static BufferedImage chickenLeftFire1, chickenLeftFire2;
    static BufferedImage chickenRightFire1, chickenRightFire2;
    static BufferedImage blueEnemyLeft1, blueEnemyLeft2;
    static BufferedImage blueEnemyRight1, blueEnemyRight2;
    static BufferedImage redEnemyUp, redEnemyUpS1, redEnemyUpS2;
    static BufferedImage redEnemyDown, redEnemyDownS1, redEnemyDownS2;
    static BufferedImage redEnemyLeft, redEnemyLeftS2;
    static BufferedImage redEnemyRight, redEnemyRightS2;
    static BufferedImage ballCrash1, ballCrash2, ballCrash3, ballCrash4;
    static BufferedImage chickenCrash1;
    static BufferedImage blueEnemyCrash1;
    static BufferedImage redEnemyCrash1;
    static BufferedImage chickenJump1, chickenJump2;
    static BufferedImage blueEnemySleep1, blueEnemySleep2, blueEnemySleep3, blueEnemySleep4, blueEnemySleep5, blueEnemySleep6;
    static BufferedImage redEnemySleep1, redEnemySleep2, redEnemySleep3, redEnemySleep4, redEnemySleep5, redEnemySleep6;
    static BufferedImage chickenGoHome;
    static BufferedImage letterG, letterA, letterM, letterE, letterO, letterV, letterR;
    static BufferedImage arrUp, arrLeft, arrRight, arrDown, space1, space2, space3;
    static BufferedImage cDbSoft, bySSKO;
    static BufferedImage score, time, sceneRed, sceneYellow;

    public static void load() {
        byte[] memory = Device.memory;
        redWall = ImageUtils.createImage2x1x1(memory, 0x5c5d);
        blueWall = ImageUtils.createImage2x1x1(memory, 0x5c8d);
        redBall = ImageUtils.createImage2x2x2(memory, 0x5c9d);
        blueBall = ImageUtils.createImage2x2x2(memory, 0x5d9d);
        mushroom = ImageUtils.createImage2x1x1(memory, 0x6acd);
        blueEnemyLeft1 = ImageUtils.createImage2x2x2(memory, 0x638d);
        blueEnemyRight1 = ImageUtils.createImage2x2x2(memory, 0x640d);
        blueEnemyLeft2 = ImageUtils.createImage2x2x2(memory, 0x634d);
        blueEnemyRight2 = ImageUtils.createImage2x2x2(memory, 0x63cd);
        redEnemyUp = ImageUtils.createImage2x2x2(memory, 0x66ed);
        redEnemyDown = ImageUtils.createImage2x2x2(memory, 0x65ed);
        redEnemyLeft = ImageUtils.createImage2x2x2(memory, 0x688d);
        redEnemyRight = ImageUtils.createImage2x2x2(memory, 0x67ed);
        redEnemyLeftS2 = ImageUtils.createImage2x3x2(memory, 0x68cd);
        redEnemyRightS2 = ImageUtils.createImage2x3x2(memory, 0x682d);
        redEnemyUpS1 = ImageUtils.createImage2x2x3(memory, 0x678d);
        redEnemyUpS2 = ImageUtils.createImage2x2x3(memory, 0x672d);
        redEnemyDownS1 = ImageUtils.createImage2x2x3(memory, 0x668d);
        redEnemyDownS2 = ImageUtils.createImage2x2x3(memory, 0x662d);
        chickenUp = ImageUtils.createImage2x2x2(memory, 0x5f1d);
        chickenUp1 = ImageUtils.createImage2x2x2(memory, 0x5f5d);
        chickenUp2 = ImageUtils.createImage2x2x2(memory, 0x5f9d);
        chickenDown = ImageUtils.createImage2x2x2(memory, 0x5ddd);
        chickenDown1 = ImageUtils.createImage2x2x2(memory, 0x5e1d);
        chickenDown2 = ImageUtils.createImage2x2x2(memory, 0x5e5d);
        chickenLeft = ImageUtils.createImage2x2x2(memory, 0x605d);
        chickenLeft1 = ImageUtils.createImage2x2x2(memory, 0x609d);
        chickenRight = ImageUtils.createImage2x2x2(memory, 0x615d);
        chickenRight1 = ImageUtils.createImage2x2x2(memory, 0x619d);
        chickenEaten1 = ImageUtils.createImage2x2x1(memory, 0x62dd);
        chickenEaten2 = ImageUtils.createImage2x2x1(memory, 0x62fd);
        chickenUpFire1 = ImageUtils.createImage2x2x2(memory, 0x601d);
        chickenDownFire1 = ImageUtils.createImage2x2x2(memory, 0x5edd);
        chickenLeftFire1 = ImageUtils.createImage2x2x2(memory, 0x611d);
        chickenRightFire1 = ImageUtils.createImage2x2x2(memory, 0x621d);
        chickenUpFire2 = ImageUtils.createImage2x2x2(memory, 0x5fdd);
        chickenDownFire2 = ImageUtils.createImage2x2x2(memory, 0x5e9d);
        chickenLeftFire2 = ImageUtils.createImage2x2x2(memory, 0x60dd);
        chickenRightFire2 = ImageUtils.createImage2x2x2(memory, 0x61dd);
        chickenCrash1 = ImageUtils.createImage2x2x1(memory, 0x631d);
        ballCrash1 = ImageUtils.createImage2x2x2(memory, 0x5cdd);
        ballCrash2 = ImageUtils.createImage2x2x2(memory, 0x5d1d);
        ballCrash3 = ImageUtils.createImage2x2x1(memory, 0x5d5d);
        ballCrash4 = ImageUtils.createImage2x2x1(memory, 0x5d7d);
        blueEnemyCrash1 = ImageUtils.createImage2x2x1(memory, 0x65cd);
        redEnemyCrash1 = ImageUtils.createImage2x2x1(memory, 0x6aad);
        chickenJump1 = ImageUtils.createImage2x2x2(memory, 0x625d);
        chickenJump2 = ImageUtils.createImage2x2x2(memory, 0x629d);
        blueEnemySleep1 = ImageUtils.createImage2x2x2(memory, 0x644d);
        blueEnemySleep2 = ImageUtils.createImage2x2x2(memory, 0x648d);
        blueEnemySleep3 = ImageUtils.createImage2x2x2(memory, 0x64cd);
        blueEnemySleep4 = ImageUtils.createImage2x2x2(memory, 0x650d);
        blueEnemySleep5 = ImageUtils.createImage2x2x2(memory, 0x658d);
        blueEnemySleep6 = ImageUtils.createImage2x2x2(memory, 0x654d);
        redEnemySleep1 = ImageUtils.createImage2x2x2(memory, 0x692d);
        redEnemySleep2 = ImageUtils.createImage2x2x2(memory, 0x696d);
        redEnemySleep3 = ImageUtils.createImage2x2x2(memory, 0x69ad);
        redEnemySleep4 = ImageUtils.createImage2x2x2(memory, 0x69ed);
        redEnemySleep5 = ImageUtils.createImage2x2x2(memory, 0x6a2d);
        redEnemySleep6 = ImageUtils.createImage2x2x2(memory, 0x6a6d);
        chickenGoHome = ImageUtils.createImage2x1x1(memory, 0x633d);
        letterG = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x7172);
        letterA = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x71B2);
        letterM = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x71F2);
        letterE = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x7232);
        letterO = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x7272);
        letterV = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x72B2);
        letterR = ImageUtils.createLetterImage(2, 16, Color.blue, memory, 0x72F2);
        arrUp = ImageUtils.createImageMirror2x2x2(memory, 0x6FB2);
        arrLeft = ImageUtils.createImageMirror2x2x2(memory, 0x7032);
        arrRight = ImageUtils.createImageMirror2x2x2(memory, 0x7072);
        arrDown = ImageUtils.createImageMirror2x2x2(memory, 0x6FF2);
        space1 = ImageUtils.createImageMirror2x2x2(memory, 0x70B2);
        space2 = ImageUtils.createImageMirror2x2x2(memory, 0x70F2);
        space3 = ImageUtils.createImageMirror2x2x2(memory, 0x7132);
        cDbSoft = ImageUtils.createTextImage(memory, 0x6e28, 8, Color.yellow, Color.BLACK);
        bySSKO = ImageUtils.createTextImage(memory, 0x6F21, 6, Color.red, Color.BLACK, 8);
        score = ImageUtils.createTextImage(memory, 0x6DDD + 1, 5, Color.red, Color.BLACK);
        time = ImageUtils.createTextImage(memory, 0x6E06 + 1, 4, Color.red, Color.BLACK);
        sceneRed = ImageUtils.createTextImage(memory, 0x6f81 + 1, 6, Color.red, Color.BLACK);
        sceneYellow = ImageUtils.createTextImage(memory, 0x6f81 + 1, 6, Color.yellow, Color.BLACK);
    }

}

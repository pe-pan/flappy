package net.panuska.tlappy;

import android.content.res.Resources;
import android.util.Log;

import net.panuska.tlappy.core.R;
import net.panuska.tlappy.awt.BufferedImage;

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
    static BufferedImage redEnemyLeftS2Demo, redEnemyRightS2Demo;
    static BufferedImage redEnemyDownS1Demo, redEnemyDownS2Demo;
    static BufferedImage ballCrash1, ballCrash2, ballCrash3, ballCrash4;
    static BufferedImage chickenCrash1;
    static BufferedImage blueEnemyCrash1;
    static BufferedImage redEnemyCrash1;
    static BufferedImage chickenJump1, chickenJump2;
    static BufferedImage blueEnemySleep1, blueEnemySleep2, blueEnemySleep3, blueEnemySleep4, blueEnemySleep5, blueEnemySleep6;
    static BufferedImage redEnemySleep1, redEnemySleep2, redEnemySleep3, redEnemySleep4, redEnemySleep5, redEnemySleep6;
    static BufferedImage chickenGoHome;
    static BufferedImage letterG, letterA, letterM, letterE, letterO, letterV, letterR;
    static BufferedImage arrUp, arrLeft, arrRight, arrDown, space;
    static BufferedImage cDbSoft, bySSKO;
    static BufferedImage score, time, sceneRed, sceneYellow;
    static BufferedImage noEnemySleep1, noEnemySleep2, noEnemySleep3, noEnemySleep4;

    private static final String TAG = Images.class.getSimpleName();

    public static void load(Resources r) {
        Log.d(TAG, "Loading images");
        redWall = ImageUtils.loadDrawable(r, R.drawable.redwall);
        blueWall = ImageUtils.loadDrawable(r, R.drawable.bluewall);
        redBall = ImageUtils.loadDrawable(r, R.drawable.redball);
        blueBall = ImageUtils.loadDrawable(r, R.drawable.blueball);
        mushroom = ImageUtils.loadDrawable(r, R.drawable.mushroom);
        blueEnemyLeft1 = ImageUtils.loadDrawable(r, R.drawable.blueenemyleft1);
        blueEnemyRight1 = blueEnemyLeft1.getMirroredImage();
        blueEnemyLeft2 = ImageUtils.loadDrawable(r, R.drawable.blueenemyleft2);
        blueEnemyRight2 = blueEnemyLeft2.getMirroredImage();
        redEnemyUp = ImageUtils.loadDrawable(r, R.drawable.redenemyup);
        redEnemyDown = ImageUtils.loadDrawable(r, R.drawable.redenemydown);
        redEnemyLeft = ImageUtils.loadDrawable(r, R.drawable.redenemyleft);
        redEnemyRight = redEnemyLeft.getMirroredImage();
        redEnemyLeftS2 = ImageUtils.loadDrawable(r, R.drawable.redenemylefts2);
        redEnemyLeftS2Demo = redEnemyLeftS2.getSubimage(4, 0);
        redEnemyRightS2 = redEnemyLeftS2.getMirroredImage();
        redEnemyRightS2Demo = redEnemyLeftS2Demo.getMirroredImage();
        redEnemyUpS1 = ImageUtils.loadDrawable(r, R.drawable.redenemyups1);
        redEnemyUpS2 = redEnemyUpS1.getMirroredImage();
        redEnemyDownS1 = ImageUtils.loadDrawable(r, R.drawable.redenemydowns1);
        redEnemyDownS2 = redEnemyDownS1.getMirroredImage();
        redEnemyDownS1Demo = redEnemyDownS1.getSubimage(0, 5);
        redEnemyDownS2Demo = redEnemyDownS1Demo.getMirroredImage();
        chickenUp = ImageUtils.loadDrawable(r, R.drawable.chickenup);
        chickenUp1 = ImageUtils.loadDrawable(r, R.drawable.chickenup1);
        chickenUp2 = chickenUp1.getMirroredImage();
        chickenDown = ImageUtils.loadDrawable(r, R.drawable.chickendown);
        chickenDown1 = ImageUtils.loadDrawable(r, R.drawable.chickendown1);
        chickenDown2 = chickenDown1.getMirroredImage();
        chickenLeft = ImageUtils.loadDrawable(r, R.drawable.chickenleft);
        chickenLeft1 = ImageUtils.loadDrawable(r, R.drawable.chickenleft1);
        chickenRight = chickenLeft.getMirroredImage();
        chickenRight1 = chickenLeft1.getMirroredImage();
        chickenEaten1 = ImageUtils.loadDrawable(r, R.drawable.chickeneaten1);
        chickenEaten2 = chickenEaten1.getMirroredImage();
        chickenUpFire1 = ImageUtils.loadDrawable(r, R.drawable.chickenupfire1);
        chickenDownFire1 = ImageUtils.loadDrawable(r, R.drawable.chickendownfire1);
        chickenLeftFire1 = ImageUtils.loadDrawable(r, R.drawable.chickenleftfire1);
        chickenRightFire1 = chickenLeftFire1.getMirroredImage();
        chickenUpFire2 = ImageUtils.loadDrawable(r, R.drawable.chickenupfire2);
        chickenDownFire2 = ImageUtils.loadDrawable(r, R.drawable.chickendownfire2);
        chickenLeftFire2 = ImageUtils.loadDrawable(r, R.drawable.chickenleftfire2);
        chickenRightFire2 = chickenLeftFire2.getMirroredImage();
        chickenCrash1 = ImageUtils.loadDrawable(r, R.drawable.chickencrash1);
        ballCrash1 = ImageUtils.loadDrawable(r, R.drawable.ballcrash1);
        ballCrash2 = ImageUtils.loadDrawable(r, R.drawable.ballcrash2);
        ballCrash3 = ImageUtils.loadDrawable(r, R.drawable.ballcrash3);
        ballCrash4 = ImageUtils.loadDrawable(r, R.drawable.ballcrash4);
        blueEnemyCrash1 = ImageUtils.loadDrawable(r, R.drawable.blueenemycrash1);
        redEnemyCrash1 = ImageUtils.loadDrawable(r, R.drawable.redenemycrash1);
        chickenJump1 = ImageUtils.loadDrawable(r, R.drawable.chickenjump1);
        chickenJump2 = ImageUtils.loadDrawable(r, R.drawable.chickenjump2);
        blueEnemySleep1 = ImageUtils.loadDrawable(r, R.drawable.blueenemysleep1);
        blueEnemySleep2 = ImageUtils.loadDrawable(r, R.drawable.blueenemysleep2);
        blueEnemySleep3 = ImageUtils.loadDrawable(r, R.drawable.blueenemysleep3);
        blueEnemySleep4 = ImageUtils.loadDrawable(r, R.drawable.blueenemysleep4);
        blueEnemySleep5 = ImageUtils.loadDrawable(r, R.drawable.blueenemysleep5);
        blueEnemySleep6 = ImageUtils.loadDrawable(r, R.drawable.blueenemysleep6);
        redEnemySleep1 = ImageUtils.loadDrawable(r, R.drawable.redenemysleep1);
        redEnemySleep2 = ImageUtils.loadDrawable(r, R.drawable.redenemysleep2);
        redEnemySleep3 = ImageUtils.loadDrawable(r, R.drawable.redenemysleep3);
        redEnemySleep4 = ImageUtils.loadDrawable(r, R.drawable.redenemysleep4);
        redEnemySleep5 = ImageUtils.loadDrawable(r, R.drawable.redenemysleep5);
        redEnemySleep6 = ImageUtils.loadDrawable(r, R.drawable.redenemysleep6);
        chickenGoHome = ImageUtils.loadDrawable(r, R.drawable.chickengohome);
        letterG = ImageUtils.loadDrawable(r, R.drawable.letterg);
        letterA = ImageUtils.loadDrawable(r, R.drawable.lettera);
        letterM = ImageUtils.loadDrawable(r, R.drawable.letterm);
        letterE = ImageUtils.loadDrawable(r, R.drawable.lettere);
        letterO = ImageUtils.loadDrawable(r, R.drawable.lettero);
        letterV = ImageUtils.loadDrawable(r, R.drawable.letterv);
        letterR = ImageUtils.loadDrawable(r, R.drawable.letterr);
        arrUp = ImageUtils.loadDrawable(r, R.drawable.arrup);
        arrRight = arrUp.getRotatedImage();
        arrDown = arrRight.getRotatedImage();
        arrLeft = arrDown.getRotatedImage();
        space = ImageUtils.loadDrawable(r, R.drawable.space);
        cDbSoft = ImageUtils.loadDrawable(r, R.drawable.cdbsoft);
        bySSKO = ImageUtils.loadDrawable(r, R.drawable.byssko);
        score = ImageUtils.loadDrawable(r, R.drawable.score);
        time = ImageUtils.loadDrawable(r, R.drawable.time);
        sceneRed = ImageUtils.loadDrawable(r, R.drawable.scenered);
        sceneYellow = ImageUtils.loadDrawable(r, R.drawable.sceneyellow);

        // out of redEnemySleep make noEnemySleep (the same picture but instead of the redEnemy, use transparent color)
        noEnemySleep1 = ImageUtils.loadDrawable(r, R.drawable.noenemysleep1);
        noEnemySleep2 = ImageUtils.loadDrawable(r, R.drawable.noenemysleep2);
        noEnemySleep3 = ImageUtils.loadDrawable(r, R.drawable.noenemysleep3);
        noEnemySleep4 = ImageUtils.loadDrawable(r, R.drawable.noenemysleep4);
    }

}

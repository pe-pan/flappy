package com.mz800.flappy.test;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.mz800.flappy.BestScore;
import com.mz800.flappy.FlappyActivity;
import com.mz800.flappy.backend.sceneService.SceneService;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import java.io.IOException;
import java.util.Arrays;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class TestActivity extends FlappyActivity {
    private static final String TAG = TestActivity.class.getSimpleName();

    public static final int WAIT_PERIOD = 5;

    public static final String FIRST_PLAYER_NAME = "Petrik";
    public static final String SECOND_PLAYER_NAME = "Pavlik";
    public static final String FIRST_PLAYER_NAME_2 = "Petricek";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        view = new SurfaceView(this);
    }

    private void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            Log.d(TAG, "Interrupted", e);
        }
    }

    private void putCode(int code) {
        bestScoreService.putTestCode(code);
    }

    private void waitForCode(int expectedCode) {
        for(;;) {
            int code = bestScoreService.getTestCode();
            if (code == expectedCode) return;
            sleep(WAIT_PERIOD);
        }
    }

    private void A1_setPlayerName() {
        Log.i(TAG, "Storing player name: "+FIRST_PLAYER_NAME);
        storePlayerName(FIRST_PLAYER_NAME);
        Log.i(TAG, "Changing player name: " + FIRST_PLAYER_NAME_2);
        storePlayerName(FIRST_PLAYER_NAME_2);
        putCode(1);
    }

    private void B1_assertNoPlayerKnownYet_setPlayerName() {
        waitForCode(1);
        bestScoreService.updateBestScoresAndPlayers(1);
        sleep(WAIT_PERIOD);
        Assert.assertEquals("There should be no players yet!", 0, bestScoreService.getPlayersCache().size());
        Log.i(TAG, "Setting player name: "+SECOND_PLAYER_NAME);
        storePlayerName(SECOND_PLAYER_NAME);
        Log.i(TAG, "Loading scores from cache");
        BestScore[] bs = bestScoreService.getBestScoresFromCache(1);
        Assert.assertEquals(0, bs.length);
        putCode(2);
    }

    private void A2_addBestScore() {
        waitForCode(2);
        Log.i(TAG, "Adding best score with 4 lives");
        bestScoreService.addBestScore(1, new BestScore(1000, 4, 1, 100, retrievePlayerId()));
        putCode(3);
    }

    private void B2_assertBestScoresPlayersAreKnown_addBestScore() {
        waitForCode(3);
        Log.i(TAG, "Updating best score for scene 1");
        bestScoreService.updateBestScoresAndPlayers(1);
        sleep(WAIT_PERIOD);
        Log.i(TAG, "Getting best score for scene 1");
        BestScore[] bs = bestScoreService.getBestScoresFromCache(1);
        Assert.assertEquals("There should be just one player!", 1, bs.length);

        Assert.assertEquals("The name does not match", FIRST_PLAYER_NAME_2, bs[0].getPlayerName());

        Log.i(TAG, "Adding best score with 5 lives");
        bestScoreService.addBestScore(1, new BestScore(1000, 5, 2, 101, retrievePlayerId()));
        putCode(5);
    }

    private void A3_assertBestScoresPlayersAreKnown() {
        waitForCode(5);
        Log.i(TAG, "Updating best score for scene 1");
        bestScoreService.updateBestScoresAndPlayers(1);
        sleep(WAIT_PERIOD);
        Log.i(TAG, "Getting best score for scene 1");
        BestScore[] bs = bestScoreService.getBestScoresFromCache(1);
        Assert.assertEquals("There should be 2 scores now", 2, bs.length);
        Assert.assertEquals(SECOND_PLAYER_NAME, bs[0].getPlayerName());
        Assert.assertEquals(FIRST_PLAYER_NAME_2, bs[1].getPlayerName());

        Log.i(TAG, "Adding best scores for scene 2");
        addUniqueBestScore(2, new BestScore(1000, 3, 3, 99, "2"));
        addUniqueBestScore(2, new BestScore(1000, 3, 3, 100, "1"));
        addUniqueBestScore(2, new BestScore(1000, 3, 2, 99, "3"));
        addUniqueBestScore(2, new BestScore(1000, 4, 2, 99, "4"));
        addUniqueBestScore(2, new BestScore(1001, 4, 2, 99, "5"));
        addUniqueBestScore(2, new BestScore(1002, 4, 2, 99, "10"));
        addUniqueBestScore(2, new BestScore(1002, 3, 2, 99, "9"));
        addUniqueBestScore(2, new BestScore(1002, 3, 3, 99, "8"));
        addUniqueBestScore(2, new BestScore(1002, 3, 3, 100, "7"));
        addUniqueBestScore(2, new BestScore(1001, 5, 1, 100, "6"));
        putCode(6);
    }

    private void B3_assertBestScoresAreKnown() {
        waitForCode(6);

        Log.i(TAG, "Updating best scores and players for scene 2");
        bestScoreService.updateBestScoresAndPlayers(2);
        sleep(WAIT_PERIOD);
        BestScore[] bs = bestScoreService.getBestScoresFromCache(2);
        Log.i(TAG, Arrays.toString(bs));
        Assert.assertEquals("Expected only 10 scores", 10, bs.length);

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals("Scores in wrong order: "+i, bs[i].getPlayerId(), (10-i)+"");
        }
        putCode(7);
    }

    private void addUniqueBestScore(int scene, BestScore bs) {
        BestScore[] scores = bestScoreService.getBestScoresFromCache(2);
        int howMany = scores.length;
        bestScoreService.addBestScore(scene, bs);
        waitForScores(2, howMany + 1);
    }

    private void waitForScores(int scene, int howMany) {
        BestScore[] scores;
        do {
            bestScoreService.updateBestScoresAndPlayers(scene);
            sleep(WAIT_PERIOD);
            scores = bestScoreService.getBestScoresFromCache(2);
            Log.i(TAG, "Loaded "+scores.length+" scores");
        } while (scores.length < howMany);
    }

    public void init() {
        bestScoreService.clearPlayersCache();
        bestScoreService.clearSceneCache(1);
        bestScoreService.clearSceneCache(2);
        storeHasRecord(false);

        try {
            SceneService.TestOverQuotaExceptionMethod m = bestScoreService.remoteService.testOverQuotaExceptionMethod((long) 0);
            m.execute();
        } catch (GoogleJsonResponseException quotaException) {
            if (quotaException.getDetails().getMessage().startsWith("com.google.apphosting.api.ApiProxy$OverQuotaException")) {
                Log.e(TAG, "Quota error");
            }
        } catch (IOException e) {
            Log.e(TAG, "exception", e);
        }
    }

    public void testA(View v) {
        init();
        Log.i(TAG, "Test A started!");

        try {
            A1_setPlayerName();
            A2_addBestScore();
            A3_assertBestScoresPlayersAreKnown();
        } catch(AssertionFailedError e) {
            Log.e(TAG, "Assertion failed; try again", e);
        }
    }

    public void testB(View v) {
        init();
        Log.i(TAG, "Test B started!");

        try {
            B1_assertNoPlayerKnownYet_setPlayerName();
            B2_assertBestScoresPlayersAreKnown_addBestScore();
            B3_assertBestScoresAreKnown();
        } catch(AssertionFailedError e) {
            Log.e(TAG, "Assertion failed; try again", e);
        }
    }
}

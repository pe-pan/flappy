package com.mz800.flappy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class SelectSceneActivity extends FlappyActivity {
    private static final String TAG = SelectSceneActivity.class.getSimpleName();
    private SurfaceView view;
    private int currentShift;
    private int minShift;
    private int maxShift;
    private int sceneWidth;
    private int sceneHeight;
    private int sceneSpace;
    private FingerUpGestureDetector detector;
    private int openScenes;

    static final float START_FLING_INHIBITION = 0.05f;
    static final float FLING_INHIBITION = 0.95f;
    static final float FLING_LIMIT = 1f;
    static final int NUM_VISIBLE_SCENES = 2;  // how many scenes visible in the screen

    static int getSceneWidth(int sceneSpace) {
        return (Device.displayWidth - (NUM_VISIBLE_SCENES - 1) * sceneSpace) / NUM_VISIBLE_SCENES;
    }

    static int getSceneHeight(int sceneWidth) {
        return Constants.SCREEN_HEIGHT * sceneWidth / Constants.SCREEN_WIDTH;
    }

    static int getMinShift(int sceneWidth) {
        return (sceneWidth - Device.displayWidth) / 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectscene);
        view = (SurfaceView) findViewById(R.id.sceneList);

        sceneSpace = getResources().getInteger(R.integer.sceneSpace);
        sceneWidth = getSceneWidth(sceneSpace);
        sceneHeight = getSceneHeight(sceneWidth);
        minShift = getMinShift(sceneWidth);
        openScenes = retrieveOpenScenes();
        maxShift = (openScenes + 1) * (sceneWidth + sceneSpace) - (Device.displayWidth + sceneWidth) / 2 - sceneSpace;
        currentShift = retrieveCurrentShift(minShift);

        detector = new FingerUpGestureDetector(this, new FingerUpGestureDetector.SimpleOnGestureListener() {
            private AsyncTask<Void, Void, Void> flingScrolling;

            @Override
            public synchronized boolean onFling(MotionEvent e1, MotionEvent e2, final float velocityX, float velocityY) {
                Log.d(TAG, "fling " + velocityX);

                cancelFlingScrolling();
                flingScrolling = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        float slowingVelocityX = velocityX * START_FLING_INHIBITION;
                        while (!isCancelled() && Math.abs(slowingVelocityX) > FLING_LIMIT) {
                            shiftScenes(-slowingVelocityX);
                            adjustMinMaxShiftPosition();
                            slowingVelocityX = slowingVelocityX * FLING_INHIBITION;
                        }
                        synchronized(SelectSceneActivity.this) {
                            flingScrolling = null;
                        }
                        return null;
                    }
                }.execute();
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "Single tap");
                int x = (int) e.getRawX();
                int y = (int) e.getRawY();
                if (y > (Device.displayHeight - sceneHeight) / 2 && y < (Device.displayHeight + sceneHeight) / 2 && currentShift + x > 0) {
                    int scNo = (currentShift + x) / (sceneWidth + sceneSpace);
                    Log.d(TAG, "Scene no " + (scNo + 1));
                    if (!canBeSelected(scNo)) {
                        Toast.makeText(SelectSceneActivity.this, getString(R.string.notThisSceneYet), Toast.LENGTH_LONG).show();
                        return true;
                    }
                    if (scNo >= 0 && scNo <= openScenes) {
                        startActivity(new Intent(SelectSceneActivity.this, FullscreenActivity.class).putExtra(FullscreenActivity.SCENE_NUMBER, scNo + 1));
                        storeSceneNumber(scNo);
                        finish();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public synchronized void onShowPress(MotionEvent e) {
                Log.d(TAG, "Finger down");
                cancelFlingScrolling();
            }

            @Override
            public synchronized boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "scroll " + distanceX);
                cancelFlingScrolling();
                shiftScenes(distanceX);
                return true;
            }

            @Override
            public boolean onFingerUp(MotionEvent e) {
                Log.d(TAG, "Finger up");
                adjustMinMaxShiftPosition();
                return true;
            }

            void shiftScenes(float distanceX) {
                currentShift += distanceX;
                drawScenes();
            }

            boolean adjustShiftEvent;
            void adjustMinMaxShiftPosition() {
                if (currentShift < minShift) {
                    cancelFlingScrolling();
                    adjustShiftEvent = true;
                    while (adjustShiftEvent && currentShift < minShift) {
                        currentShift += (minShift - currentShift) / 10 + 1;
                        drawScenes();
                    }
                }
                if (currentShift > maxShift) {
                    cancelFlingScrolling();
                    adjustShiftEvent = true;
                    while (adjustShiftEvent && currentShift > maxShift) {
                        currentShift -= (currentShift - maxShift) / 10 + 1;
                        drawScenes();
                    }
                }
            }

            private void cancelFlingScrolling() {
                synchronized (SelectSceneActivity.this) {
                    if (flingScrolling != null) {
                        Log.d(TAG, "Cancelling fling");
                        flingScrolling.cancel(true);
                    }
                }
                adjustShiftEvent = false;
            }
        });

        predrawScenes();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (detector.onTouchEvent(event)) return true;
        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            oldShift = currentShift-1; // make sure to draw the first time
            drawScenes();
        }
    }

    private int oldShift;
    private void drawScenes() {
        if (currentShift == oldShift) return;    // no change, nothing to draw
        int firstVisibleScene = currentShift / (sceneWidth + sceneSpace);
        int remnant = currentShift % (sceneWidth + sceneSpace);
        Canvas c = view.getHolder().lockCanvas();
        if (c != null) {
            c.drawColor(android.R.color.black, PorterDuff.Mode.CLEAR);

            int y = (Device.displayHeight - sceneHeight) / 2;
            if (currentShift > oldShift) {
                for (int i = 0; i < NUM_VISIBLE_SCENES + 1; i++) {
                    drawBitmap(c, firstVisibleScene+i, (sceneWidth + sceneSpace) * i - remnant, y);
                }
            } else {
                for (int i = NUM_VISIBLE_SCENES; i >= 0; i--) {
                    drawBitmap(c, firstVisibleScene+i, (sceneWidth + sceneSpace) * i - remnant, y);
                }
            }

            view.getHolder().unlockCanvasAndPost(c);
        }
        oldShift = currentShift;
    }

    private void drawBitmap(Canvas c, int num, int x, int y) {
        Bitmap b = getSceneBitmap(num);
        if (b == null) return;
        c.drawBitmap(b, x, y, null);
    }

    int firstPredrawnScene;
    private static final int NUM_PREDRAWN_SCENES = NUM_VISIBLE_SCENES + 1;
    Bitmap predrawnScenes[];

    private Bitmap getSceneBitmap(int num) {
        if (num < 0 || num >= openScenes+NUM_VISIBLE_SCENES+2) {
            return null;
        }
        if (num < firstPredrawnScene) {
            Log.d(TAG, "Decreasing ");
            System.arraycopy(predrawnScenes, 0, predrawnScenes, 1, NUM_PREDRAWN_SCENES - 1);

            predrawnScenes[0] = drawSceneBitmap(num);
            firstPredrawnScene--;
        } else {
            if (num >= firstPredrawnScene + NUM_PREDRAWN_SCENES) {
                Log.d(TAG, "Increasing ");
                System.arraycopy(predrawnScenes, 1, predrawnScenes, 0, NUM_PREDRAWN_SCENES - 1);

                predrawnScenes[NUM_PREDRAWN_SCENES - 1] = drawSceneBitmap(num);
                firstPredrawnScene++;
            }
        }
        return predrawnScenes[num - firstPredrawnScene];
    }

    private void predrawScenes() {
        firstPredrawnScene = currentShift / (sceneWidth + sceneSpace);
        predrawnScenes = new Bitmap[NUM_PREDRAWN_SCENES];
        for (int i = 0; i < predrawnScenes.length; i++) {
            predrawnScenes[i] = drawSceneBitmap(firstPredrawnScene + i);
        }
    }

    private Bitmap drawSceneBitmap(int num) {
        Log.d(TAG, "Drawing scaled scene " + num);
        Scene scene = new Scene(num+1, new VRAM());
        ScreenScore score = ScreenScore.screenScores[num];
        int lives = num % 5 == 0 ? 5 : ScreenScore.screenScores[num-1].myLives;  // take number of lives from previous scene
        if (num <= openScenes) {
            scene.predrawScene(true, lives, score.myBestScore);
        } else {
            scene.predrawSceneNumberScreen(lives, score.myBestScore);
        }
        Bitmap b = scene.getVRAM().getImage().getBitmap();
        if (!canBeSelected(num)) {
            b = updateSaturation(b, 0.5f);
        }
        return Bitmap.createScaledBitmap(b, sceneWidth, sceneHeight, true);
    }

    private boolean canBeSelected(int scNo) {
        if (Main.cheating) return true;
        return scNo <= openScenes - (openScenes % 5);
    }

    // copied from http://android-er.blogspot.com/2013/09/adjust-saturation-of-bitmap-with.html
    private Bitmap updateSaturation(Bitmap src, float settingSat) {

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(settingSat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvasResult.drawBitmap(src, 0, 0, paint);

        return bitmapResult;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, IntroActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeScrollShift(currentShift);
    }
}

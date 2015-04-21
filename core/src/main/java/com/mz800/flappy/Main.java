package com.mz800.flappy;

import android.util.Log;
import android.view.SurfaceView;

import com.mz800.core.R;
import com.mz800.flappy.awt.Color;
import static com.mz800.flappy.Constants.*;
import static com.mz800.flappy.Device.*;
import java.io.InputStream;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
public class Main {
    private static final String TAG = Main.class.getSimpleName();

    static byte[] memory = new byte[65536];
    static int score;
    static int lives;
    static boolean verbose = false;
    static boolean cheating = false;
    private String password = "     ";
    private final FinalScreen finalScr;

    public static Main getInstance(SurfaceView view) throws Exception {
        Log.d(TAG, "Init");
        return new Main(view);
    }

    private Main(SurfaceView view) throws Exception {
        InputStream is = view.getResources().openRawResource(R.raw.flappy);
        int read = 0;
        while (read < 65536) {
            int res = is.read(memory, read, 65536 - read);
            if (res < 0) {
                break;
            }
            read += res;
            if (verbose) {
                System.out.println("read: " + res + " total: " + read);
            }
        }
        is.close();

        vram.createWindow(view);
        finalScr = new FinalScreen();
    }

    void game() throws Exception {
        keyboard.clearKeyPressed();
        keyboard.clear();
        Main.resetState(NORMAL_WAIT);
        int scNo = 1;
        int scNoRestart = 1;
        int sel = GAME;
        while (true) {
            while (sel < 0) {
                switch (sel) {
                    case GAME:
                        sel = 0;
                        score = 0;
                        lives = 5;
                        scNo = scNoRestart;
                        break;
                    case SETUP:
                        sel = setup();
                        if (sel > 0) {
                            scNoRestart = sel;
                            sel = GAME;
                        }
                        break;
                }
            }
            Scene s = new Scene(scNo);
            s.showSceneNumberScreen();
            s.showScene(false);
            int res = s.chickenOutOfHome(lives);
            if (res == EXIT_GAME) {
                return;
            }
            if (res == LOOSE_LIFE) {
                Main.setState(NORMAL_WAIT);
                lives--;
                if (lives == 0) {
                    sel = s.gameOver();
                }
                continue;
            }
            music.start();
            res = s.gameLoop();
            if (res == EXIT_GAME) {
                return;
            } else if (res == LOOSE_LIFE || res == NORMAL_WAIT) {
                Main.setState(NORMAL_WAIT);
                lives--;
                if (lives == 0) {
                    sel = s.gameOver();
                }
            } else {
                if (scNo % 5 == 0) {
                    scNoRestart = scNo + 1;
                    password = s.printPassword(scNo / 5);
                }
                scNo++;
                if (scNo > 200) {
                    finalScr.finalCertificate();
                    scNoRestart = 1;
                    sel = INTRO;
                }
            }
        }
    }

    private int setup() {
        keyboard.clear();
        music.stop();
        vram.clear();
        Scene.drawSimpleBorder();
        vram.printText(16, 5, "M E N U", Color.BLUE);
        vram.printText(8, 7, "F1", Color.yellow);
        vram.printText(10, 7, ":", Color.BLUE);
        vram.printText(11, 7, "Speed control (0-9)", Color.RED);
        vram.printText(31, 7, Integer.toString(Scene.gameSpeed), Color.yellow);
        vram.printText(8, 9, "F2", Color.yellow);
        vram.printText(10, 9, ":", Color.BLUE);
        vram.printText(11, 9, "Key word input", Color.RED);
        vram.printText(26, 9, password, Color.yellow);
        int y = 13;
        if (cheating) {
            vram.printText(8, 11, "F3", Color.yellow);
            vram.printText(10, 11, ":", Color.BLUE);
            vram.printText(11, 11, "Browse scenes", Color.RED);
            y = 15;
        }
        vram.printText(7, y, "Hit", Color.RED);
        vram.printText(11, y, "Space key", Color.YELLOW);
        vram.printText(21, y, "to Game start", Color.RED);
        vram.refresh();
        while (true) {
            if (keyboard.escKey()) {
                return INTRO;
            }
            if (keyboard.spaceKey()) {
                return GAME;
            }
            if (keyboard.otherKey() == 112) {
                setSpeed();
            } else if (keyboard.otherKey() == 113) {
                int level = setPassword();
                if (level >= 0) {
                    return level;
                }
            } else if (cheating && keyboard.otherKey() == 114) {
                int level = browseScenes();
                return level > 0 ? level : SETUP;
            }
            wait(10);
        }
    }

    private void setSpeed() {
        keyboard.clear();
        vram.printText(31, 7, Integer.toString(Scene.gameSpeed), Color.BLACK, Color.YELLOW);
        vram.refresh();
        while (true) {
            int key = keyboard.getKeyPressed();
            if (key >= '0' && key <= '9') {
                Scene.gameSpeed = key - '0';
                break;
            }
            wait(10);
        }
        vram.printText(31, 7, Integer.toString(Scene.gameSpeed), Color.YELLOW);
        vram.refresh();
    }

    private int setPassword() {
        int key;
        char[] pass = password.toCharArray();
        int pos = 0;
        while (true) {
            for (int i = 0; i < 5; i++) {
                if (i == pos) {
                    vram.printText(26 + i, 9, "" + pass[i], Color.BLACK, Color.yellow);
                } else {
                    vram.printText(26 + i, 9, "" + pass[i], Color.yellow);
                }
            }
            vram.refresh();
            key = 0;
            keyboard.clearKeyPressed();
            while (key == 0) {
                key = keyboard.getKeyPressed();
                wait(3);
            }
            if (key == 10) {
                break;
            }
            if (key == 8) {
                pos--;
                if (pos < 0) {
                    pos = 0;
                }
            } else {
                pass[pos++] = (char) key;
                if (pos > 4) {
                    pos = 4;
                }
            }
            if (key == 10) {
                break;
            }
        }
        password = new String(pass);
        vram.printText(26, 9, password, Color.yellow);
        vram.refresh();

        int level = Scene.checkPass(password);
        if (level < 0) {
            return -1;
        }
        // 9,21 cisla variant
        level = level * 5 + 1;
        pos = 0;
        keyboard.clear();
        while (true) {
            int x = 9;
            for (int i = 0; i < 5; i++) {
                String s = Integer.toString(level + i);
                while (s.length() < 3) {
                    s = '0' + s;
                }
                if (i == pos) {
                    vram.printText(x, 21, s, Color.BLACK, Color.yellow);
                } else {
                    vram.printText(x, 21, s, Color.yellow);
                }
                x += 5;
            }
            vram.refresh();
            keyboard.userAction();
            switch (keyboard.userAction) {
                case LEFT:
                    if (--pos < 0) {
                        pos = 0;
                    }
                    break;
                case RIGHT:
                    if (++pos > 4) {
                        pos = 4;
                    }
                    break;
                case FIRE:
                    return level + pos;
            }
            wait(10);
        }
    }

    private int browseScenes() {
        int scNo = 1;
        while (true) {
            Scene s = new Scene(scNo);
            s.showScene(true);
            boolean selected = false;
            keyboard.clear();
            while (!selected) {
                keyboard.userAction();
                switch (keyboard.userAction) {
                    case RIGHT:
                        scNo++;
                        if (scNo > 200) {
                            scNo = 1;
                        }
                        selected = true;
                        break;
                    case LEFT:
                        scNo--;
                        if (scNo < 1) {
                            scNo = 200;
                        }
                        selected = true;
                        break;
                    case FIRE:
                        return scNo;
                    case ESC:
                        return 0;
                }
                wait(20);
            }
        }
    }

    public static final int NORMAL_WAIT = 0;
    public static final int EXIT_GAME = -1;
    public static final int LOOSE_LIFE = -2;
    public static final int KEEP_WAITING = -3;

    private static int gameState = NORMAL_WAIT;

    static synchronized int wait(int t) {
        try {
            if (gameState == NORMAL_WAIT) {
                Main.class.wait(t * 6);
            }
            while (gameState == KEEP_WAITING) {
                Main.class.wait();
            }
            return gameState;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return EXIT_GAME;
        }
    }

    static synchronized boolean isWaiting() {
        return gameState == NORMAL_WAIT || gameState == KEEP_WAITING;
    }


    static synchronized void setState(int state) {
        gameState = state;
        Main.class.notify();
    }

    static synchronized void resetState(int state) {
        if (gameState == KEEP_WAITING) return;
        setState(state);
    }

}

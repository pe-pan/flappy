package mz800.flappy;

import java.awt.Color;
import static mz800.flappy.Constants.*;
import java.io.InputStream;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
public class Main {

    static byte[] memory = new byte[65536];
    static int score;
    static int lives;
    static boolean verbose = false;
    static boolean cheating = false;
    private String password = "     ";
    private final Keyboard keyboard;
    private final VRAM vram;
    private final Music music;
    private final FinalScreen finalScr;

    public static void main(String[] args) throws Exception {
        for (String s : args) {
            if ("-v".equals(s)) {
                verbose = true;
            }
        }
        Main me = new Main();
        me.firstScreen();
        me.game();
        System.exit(0);
    }

    private Main() throws Exception {
        InputStream is = getClass().getResourceAsStream("flappy.mzf");
        if (is == null) {
            if (verbose) {
                System.out.println("MZF resource - second try");
            }
            is = getClass().getResourceAsStream("/flappy/flappy.mzf");
            if (is == null) {
                System.out.println("Input stream is null");
            }
        }
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

        keyboard = Keyboard.getInstance();
        vram = VRAM.getInstance();
        music = Music.getInstance();
        vram.createWindow(false);
        finalScr = new FinalScreen();
    }

    private void game() throws Exception {
        keyboard.clearKeyPressed();
        keyboard.clear();
        int scNo = 1;
        int scNoRestart = 1;
        int sel = INTRO;
        while (true) {
            while (sel < 0) {
                switch (sel) {
                    case INTRO:
                        Intro intro = new Intro();
                        sel = intro.intro();
                        break;
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
            s.chickenOutOfHome(lives);
            music.start();
            int res = s.gameLoop();
            if (res == 0) {
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

    private void firstScreen() {
        vram.clear();
        tit(1, "xxx x   xxx xxx xxx x x");
        tit(2, "x   x   x x x x x x x x");
        tit(3, "xx  x   x x xxx xxx xxx");
        tit(4, "x   x   xxx x   x    x");
        tit(5, "x   xxx x x x   x    x");
        vram.printText(7, 7, "Original game created for", Color.RED);
        vram.printText(9, 8, "SHARP MZ-800", Color.WHITE);
        vram.printText(22, 8, "computer", Color.RED);
        vram.printText(3, 10, "Java version by Petr Slechta, 2014", Color.RED);
        vram.printText(12, 14, "M", Color.yellow);
        vram.printText(14, 14, "-", Color.BLUE);
        vram.printText(16, 14, "music:", Color.RED);
        vram.printText(23, 14, "on", Color.yellow);
        vram.printText(12, 15, "S", Color.yellow);
        vram.printText(14, 15, "-", Color.BLUE);
        vram.printText(16, 15, "screen:", Color.RED);
        vram.printText(24, 15, "full", Color.yellow);
        vram.printText(8, 16, "space", Color.yellow);
        vram.printText(14, 16, "-", Color.BLUE);
        vram.printText(16, 16, "start game", Color.RED);
        vram.printText(7, 20, "http://www.8bit-times.eu", Color.WHITE);
        vram.printText(2, 22, "Let's celebrate 30 years of the best", Color.RED);
        vram.printText(3, 23, "logical game for 8-bit computers!", Color.RED);
        vram.printText(35, 0, "ver.B", Color.DARK_GRAY);
        vram.refresh();

        boolean musicOn = true;
        boolean fullScr = true;

        keyboard.clearKeyPressed();
        while (true) {
            switch (keyboard.getKeyPressed()) {
                case 'M':
                case 'm':
                    musicOn = !musicOn;
                    vram.printText(23, 14, musicOn ? "on " : "off", Color.yellow);
                    vram.refresh();
                    break;
                case 'S':
                case 's':
                    fullScr = !fullScr;
                    vram.printText(24, 15, fullScr ? "full  " : "window", Color.yellow);
                    vram.refresh();
                    break;
                case '9':
                    cheating = true;
                    vram.printText(30, 0, "CHEAT MODE", Color.yellow);
                    vram.refresh();
                    break;
                case ' ':
                    if (!musicOn) {
                        music.disableMusic();
                    }
                    if (fullScr) {
                        vram.createWindow(true);
                    }
                    vram.clear();
                    vram.refresh();
                    wait(50);
                    return;
            }
            wait(10);
        }
    }

    private void tit(int y, String s) {
        for (int i = 0, maxi = s.length(); i < maxi; i++) {
            if (s.charAt(i) == 'x') {
                vram.imageNoOfs(8 + i, y, Images.blueWall);
            }
        }
    }

    static void wait(int t) {
        try {
            Thread.sleep(t * 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

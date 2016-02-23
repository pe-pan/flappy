package com.mz800.flappy.score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.mz800.flappy.BestScore;
import com.mz800.flappy.backend.sceneService.SceneService;
import com.mz800.flappy.backend.sceneService.model.Player;
import com.mz800.flappy.backend.sceneService.model.SceneRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
public class BestScoreService {
    private static final String TAG = BestScoreService.class.getSimpleName();

    private static final String BEST_SCORE_SCENE_FILE_CACHE = "best_score_scene_";
    private static final String PLAYERS_FILE_CACHE = "players_cache";
    private static final Executor SERIAL_EXECUTOR = new SerialExecutor();
    private final Map<Integer, BestScore[]> scoreCache; // best scores are sorted from the best to the worse
    private final Map<String, String> playersCache; // <playerId, playerName>
    private long newestPlayerTime = 0;              // time of player that was synchronized last time
    private static final int BEST_SCORE_FILE_VERSION = 1;
    private final PersistentQueue queue;
    public static final int PLAYERS_FILE_VERSION = 1;
    static SceneService remoteService;
    private Context cxt;

    public BestScoreService(Context cxt) {
        this.cxt = cxt;
        scoreCache = new HashMap<>();
        playersCache = new HashMap<>();

        loadPlayersCache();

        SceneService.Builder sceneServiceBuilder = new SceneService.Builder(new NetHttpTransport(), getJsonFactory(), null);

        remoteService = sceneServiceBuilder.build();

        PersistentQueue.GetPlayersMessage.registerCallback(new PersistentQueue.GetPlayersMessage.Callback() {
            @Override
            public void handleSendResult(PersistentQueue.GetPlayersMessage message, List<Player> players) {
                Log.d(TAG, "Async handling result of send; putting " + players.size() + " players into cache");
                for (Player player : players) {
                    synchronized (playersCache) {
                        playersCache.put(player.getId(), player.getName());
                        if (player.getTime() > newestPlayerTime) {
                            newestPlayerTime = player.getTime();
                        }
                    }
                }
                savePlayersCache();
            }
        });

        PersistentQueue.TopScoreMessage.registerCallback(new PersistentQueue.TopScoreMessage.Callback() {
            @Override
            public void handleSendResult(PersistentQueue.TopScoreMessage message, List<SceneRecord> topScores) {
                //todo implement caching
                // it should check if the results are not the same as the last one
                // and do not update anything

                int scene = message.getSceneNo();
                Log.d(TAG, "Async handling result of send; getting top score for scene: " + scene);
                BestScore[] bestScores = new BestScore[topScores.size()];
                Iterator<SceneRecord> iterator = topScores.iterator();
                for (int i = 0; i < bestScores.length; i++) {
                    SceneRecord sceneRecord = iterator.next();
                    int score = sceneRecord.getScore();
                    int lives = sceneRecord.getLives();
                    int attempts = sceneRecord.getAttempts();
                    long date = sceneRecord.getDate().getValue();
                    String playerId = sceneRecord.getPlayerId();
                    bestScores[i] = new BestScore(score, lives, attempts, date, playerId);
                    sceneRecord.getSceneNo();
                }
                synchronized (scoreCache) {
                    scoreCache.put(scene, bestScores);  // this will remove and destroy the original cached bestScores; if any
                }
                saveScoreCache(scene);
            }
        });

        queue = new PersistentQueue(cxt);
        new Thread(queue).start();
    }

    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory getJsonFactory() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // only for honeycomb and newer versions
            return new AndroidJsonFactory();
        } else {
            return new GsonFactory();
        }
    }

    public void updateBestScoresAndPlayers(int scene) {
        updateBestScores(scene);
        updatePlayers();
    }

    public BestScore[] getBestScoresFromCache(int scene) {
        synchronized (scoreCache) {
            BestScore[] scores = getOrLoadScoreCache(scene);
            for (BestScore score : scores) {
                synchronized (playersCache) {
                    String playerName = playersCache.get(score.getPlayerId());
                    if (playerName != null) {
                        score.setPlayerName(playerName);
                    }
                }
            }
            return scores;
        }
    }

    public void addBestScore(int scene, BestScore bestScore) {
        BestScore[] scores = getOrLoadScoreCache(scene);
        int i;
        for (i = 0; i < scores.length; i++) {
            BestScore score = scores[i];
            if (bestScore.isBetter(score)) {
                scores[i] = bestScore;
                Log.d(TAG, "Score being put on " + (i + 1) + ". top position: " + bestScore);
                persistScores(scene, bestScore, scores);
                return;
            }
        }
        if (scores.length == BestScore.TOP_VISIBLE_SCORES) {
            Log.d(TAG, "Score not better than " + BestScore.TOP_VISIBLE_SCORES + " previous ones");
            return;
        }
        Log.d(TAG, "Adding new score on " + (i + 1) + ". top postion");
        BestScore[] newScores = new BestScore[scores.length + 1];
        System.arraycopy(scores, 0, newScores, 0, scores.length);
        newScores[i] = bestScore;

        persistScores(scene, bestScore, newScores);
    }

    private void persistScores(int scene, BestScore bestScore, BestScore[] newScores) {
        synchronized (scoreCache) {
            scoreCache.put(scene, newScores);
        }
        saveScoreCache(scene);
        addRecord(scene, bestScore);
    }

    /**
     * Returns an empty array of BestScore in case nothing was loaded.
     *
     * @param scene
     * @return
     */
    private BestScore[] loadScoreCache(int scene) {
        Log.d(TAG, "Loading score cache for scene " + scene);
        try {
            File f = new File(cxt.getCacheDir(), BEST_SCORE_SCENE_FILE_CACHE + scene);
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
            int version = stream.readInt();
            if (version != BEST_SCORE_FILE_VERSION) {
                Log.e(TAG, "Cached best scores file do not contain the right version; expected " + BEST_SCORE_FILE_VERSION + "; given: " + version + "; deleting scoreCache");
                stream.close();
                f.delete();
                return new BestScore[0];
            }
            int numScores = stream.readInt();
            Log.d(TAG, "Number of cached scores: " + numScores);
            BestScore[] bestScores = new BestScore[numScores]; // do not reuse data structures; the list can be in use right now
            for (int i = 0; i < numScores; i++) {
                BestScore bs = new BestScore(stream);
                bestScores[i] = bs;
            }
            synchronized (scoreCache) {
                scoreCache.put(scene, bestScores);
            }
            stream.close();
            return bestScores;
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Exception when loading score cache details");
            return new BestScore[0];
        }
    }

    private void saveScoreCache(int scene) {
        Log.d(TAG, "Saving score cache for scene " + scene);
        File f = new File(cxt.getCacheDir(), BEST_SCORE_SCENE_FILE_CACHE + scene);
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
            BestScore[] bestScores;
            synchronized (scoreCache) {
                bestScores = scoreCache.get(scene);
            }
            if (bestScores == null) {
                Log.d(TAG, "Nothing to save");
                return;
            }
            int numScores = bestScores.length;
            Log.d(TAG, "Saving " + numScores + " best scores from cache");
            stream.writeInt(BEST_SCORE_FILE_VERSION);  // writing version
            stream.writeInt(numScores);
            for (BestScore bs : bestScores) {
                bs.writeExternal(stream);
            }
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception when saving score cache details");
        }
    }

    private void loadPlayersCache() {
        Log.d(TAG, "Loading players cache");
        try {
            File f = new File(cxt.getCacheDir(), PLAYERS_FILE_CACHE);
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
            int version = stream.readInt();
            if (version != PLAYERS_FILE_VERSION) {
                Log.e(TAG, "Cached players file do not contain the right version; expected " + PLAYERS_FILE_VERSION + "; given: " + version + "; deleting players cache");
                stream.close();
                f.delete();
                return;
            }
            int numPlayers = stream.readInt();
            Log.d(TAG, "Number of cached players: " + numPlayers);
            for (int i = 0; i < numPlayers; i++) {
                String playerId = (String) stream.readObject();
                String playerName = (String) stream.readObject();
                synchronized (playersCache) {
                    playersCache.put(playerId, playerName);
                }
            }
            newestPlayerTime = stream.readLong();
            stream.close();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "Exception when loading players cache");
        }
    }

    private void savePlayersCache() {
        Log.d(TAG, "Saving players cache");
        File f = new File(cxt.getCacheDir(), PLAYERS_FILE_CACHE);
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(f));
            synchronized (playersCache) {
                Set<Map.Entry<String, String>> s = playersCache.entrySet();
                int numEntries = s.size();
                if (numEntries == 0) {
                    Log.d(TAG, "Nothing to save");
                    return;
                }
                Log.d(TAG, "Saving " + numEntries + " entries of players cache");
                stream.writeInt(PLAYERS_FILE_VERSION);
                stream.writeInt(numEntries);
                for (Map.Entry<String, String> e : s) {
                    stream.writeObject(e.getKey());
                    stream.writeObject(e.getValue());
                }
                stream.writeLong(newestPlayerTime);
            }
            stream.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception when saving best score details");
        }
    }

    /**
     * Loads the cache from file if not loaded yet.
     *
     * @param scene
     * @return
     */
    private BestScore[] getOrLoadScoreCache(int scene) {
        BestScore[] bs;
        synchronized (scoreCache) {
            bs = scoreCache.get(scene);
        }
        if (bs == null) {
            bs = loadScoreCache(scene);
        }
        return bs;
    }

    public void updateBestScores(final int scene) {
        executeTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                queue.topScore(scene);
                return null;
            }
        });
    }

    //todo this must be done in asynctask as it's being synchronized over shared cache folder
    //access to this folder is locked for whole the period of sending a message to the google data store service
    //as this op can take couple of seconds (in case the service is not accessible or the internet is off)
    //this must be run in async task
    //todo try to remove this synchronization and don't use async task
    public void updatePlayers() {
        executeTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                queue.getPlayers(newestPlayerTime);
                return null;
            }
        });
    }

    private void addRecord(final int sceneNo, final BestScore bestScore) {
        executeTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                queue.addRecord(sceneNo, bestScore);
                return null;
            }

        });
    }

    public void putPlayer(final String id, final String name) {
        executeTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                queue.putPlayer(id, name);
                return null;
            }
        });
    }

    //todo copied from AsyncTask
    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        @SuppressLint("NewApi")
        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(mActive); //this is always run on Build.VERSION_CODES.HONEYCOMB or later
            }
        }
    }

    private void executeTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(SERIAL_EXECUTOR);
        } else {
            task.execute();
        }
    }
}

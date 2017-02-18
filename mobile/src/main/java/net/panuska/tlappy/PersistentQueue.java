package net.panuska.tlappy;

import android.content.Context;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.mz800.flappy.backend.sceneService.SceneService;
import com.mz800.flappy.backend.sceneService.model.Player;
import com.mz800.flappy.backend.sceneService.model.PlayerCollection;
import com.mz800.flappy.backend.sceneService.model.SceneRecord;
import com.mz800.flappy.backend.sceneService.model.SceneRecordCollection;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.List;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2016.
 */
class PersistentQueue implements Runnable {
    private static final String TAG = PersistentQueue.class.getSimpleName();
    public static final String MESSAGE_QUEUE_FOLDER = "message.queue";
    public static final int MESSAGE_VERSION = 1;
    public static final int RETRY_TIMEOUT = 5 * 60 * 1000; // 5 mins

    private final File messageQueueFolder;

    PersistentQueue(Context cxt) {
        messageQueueFolder = new File(cxt.getCacheDir(), MESSAGE_QUEUE_FOLDER);
        messageQueueFolder.mkdir();
    }

    void addRecord(final int sceneNo, final BestScore bestScore) {
        _persistMessageAndNotify(new AddRecordMessage(sceneNo, bestScore));
    }

    void putPlayer(final String id, final String name) {
        _persistMessageAndNotify(new PutPlayerMessage(id, name));
    }

    void getPlayers(final long time) {
        _persistMessageAndNotify(new GetPlayersMessage(time));
    }

    void topScore(final int sceneNo) {
        _persistMessageAndNotify(new TopScoreMessage(sceneNo));
    }

    private void _persistMessageAndNotify(Message message) {
        synchronized (messageQueueFolder) {
            try {
                message.persist(messageQueueFolder);
                Log.d(TAG, "Notifying process queue thread");
                messageQueueFolder.notify();  // notify only if message write succeeds
            } catch (Exception e) {
                Log.e(TAG, "Cannot write into " + MESSAGE_QUEUE_FOLDER + " while message " + this);
            }
        }
    }

    /**
     * Waits for a message and processes it.
     */
    @Override
    public void run() {
        try {
            for (; ; ) {
                synchronized (messageQueueFolder) {
                    String[] messages = messageQueueFolder.list(); //todo should we sort the list? http://stackoverflow.com/questions/203030/best-way-to-list-files-in-java-sorted-by-date-modified
                    while (messages == null || messages.length == 0) {  // no messages in the queue
                        Log.d(TAG, "No messages in the queueu; going to sleep until wakeup");
                        messageQueueFolder.wait();                         // and wait until something will be available
                        Log.d(TAG, "Woken up!");
                        messages = messageQueueFolder.list();
                    }
                    Log.d(TAG, "There is/are " + messages.length + " messages in the queue");
                    File f = new File(messageQueueFolder, messages[0]);

                    Message message;
                    try {
                        message = Message.getMessage(f);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception when creating a message from file " + f.getName(), e);
                        f.delete();
                        message = null;
                    }

                    if (message != null) {
                        try {
                            message.send();
                            if (!f.delete()) {  // message will get deleted only if the send succeeds
                                Log.e(TAG, "Message not removed! Will be sent again! " + f.getName());
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "Message " + message + " was not sent", e);
                            Log.d(TAG, "Going to sleep for " + RETRY_TIMEOUT / (60 * 1000) + " mins");
                            messageQueueFolder.wait(RETRY_TIMEOUT); // wait a minute to try
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            Log.d(TAG, "Finishing queue");
        }
    }

    public abstract static class Message implements Externalizable {
        private static final long serialVersionUID = 1938307499224094756L;

        // for externalizable
        public Message() {
        }

        static Message getMessage(File f) throws IOException, ClassNotFoundException {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f)); // take the very first message
            try {
                return (Message) stream.readObject();
            } finally {
                stream.close();
            }
        }

        /**
         * Gets the file name where this file is to be saved. It should give the same name for messages of "same type",
         * so in case there is an older (not sent yet) message of the same type, this older one will get
         * rewritten with newer content.
         *
         * @return
         */
        abstract protected String getFileName();

        abstract Object send() throws IOException;

        @Override
        public void writeExternal(ObjectOutput output) throws IOException {
            output.writeInt(MESSAGE_VERSION);
        }

        @Override
        public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
            input.readInt();  // reading the message version
        }

        void persist(File messageQueueFolder) throws IOException {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(new File(messageQueueFolder, getFileName()))); //giving the same name will overwrite existing file -> which is correct; it contains old data anyway
            stream.writeObject(this);
            stream.close();
        }
    }

    public static class AddRecordMessage extends PersistentQueue.Message {
        private static final long serialVersionUID = 8935180125766161418L;
        private int sceneNo;
        private BestScore bestScore;

        public AddRecordMessage(int sceneNo, BestScore bestScore) {
            this.sceneNo = sceneNo;
            this.bestScore = bestScore;
        }

        // for externalizable
        public AddRecordMessage() {
        }

        @Override
        protected String getFileName() {
            return "add_record_scene_" + sceneNo;
        }

        Object send() throws IOException {
            Log.d(TAG, "Sending message Add Record for scene " + sceneNo);
            SceneRecord record = new SceneRecord();
            SceneService.AddRecord addRecord = null;
            record.set("id", sceneNo + bestScore.getPlayerId());
            record.set("sceneNo", sceneNo);
            record.set("playerId", bestScore.getPlayerId());
            record.set("score", bestScore.getScore());
            record.set("lives", bestScore.getLives());
            record.set("attempts", bestScore.getAttempts());
            record.set("date", new DateTime(System.currentTimeMillis()));

            addRecord = BestScoreService.remoteService.addRecord(record);
            Object o = addRecord.execute();
            //todo evaluate o
            Log.d(TAG, addRecord.toString());
            return o;
        }

        @Override
        public String toString() {
            return "AddRecordMessage{" +
                    "sceneNo=" + sceneNo +
                    ", bestScore=" + bestScore +
                    '}';
        }

        @Override
        public void writeExternal(ObjectOutput output) throws IOException {
            super.writeExternal(output);
            output.writeInt(sceneNo);
            output.writeObject(bestScore);
        }

        @Override
        public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
            super.readExternal(input);
            sceneNo = input.readInt();
            bestScore = (BestScore) input.readObject();
        }
    }

    public static class PutPlayerMessage extends PersistentQueue.Message {
        private static final long serialVersionUID = 4489301461173510444L;
        private String id;
        private String name;

        public PutPlayerMessage(String id, String name) {
            this.id = id;
            this.name = name;
        }

        // for externalizable
        public PutPlayerMessage() {
        }

        @Override
        protected String getFileName() {
            return "put_player";
        }

        Object send() throws IOException {
            Log.d(TAG, "Sending message Put Player for player " + name);
            Player player = new Player();
            SceneService.PutPlayer putPlayer = null;
            player.set("id", id);
            player.set("name", name);

            putPlayer = BestScoreService.remoteService.putPlayer(player);
            Object o = putPlayer.execute();
            //todo evaluate o
            Log.d(TAG, putPlayer.toString());
            return o;
        }

        @Override
        public String toString() {
            return "PutPlayerMessage{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public void writeExternal(ObjectOutput output) throws IOException {
            super.writeExternal(output);
            output.writeObject(id);
            output.writeObject(name);
        }

        @Override
        public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
            super.readExternal(input);
            id = (String) input.readObject();
            name = (String) input.readObject();
        }
    }

    public static class GetPlayersMessage extends Message {
        private static final long serialVersionUID = -4146701661718242293L;

        interface Callback {
            void handleSendResult(GetPlayersMessage message, List<Player> player);
        }

        static Callback callback = null;

        static void registerCallback(Callback callback) {
            GetPlayersMessage.callback = callback;
        }

        private long time;

        public GetPlayersMessage(long time) {
            this.time = time;
        }

        //for externalizable
        public GetPlayersMessage() {
        }

        @Override
        protected String getFileName() {
            return "get_players";
        }

        @Override
        Object send() throws IOException {
            Log.d(TAG, "Sending message Get Players for player newer than " + new Date(time));
            SceneService.GetPlayersSince getPlayersSince = BestScoreService.remoteService.getPlayersSince(time);
            PlayerCollection players = getPlayersSince.execute();
            Log.d(TAG, getPlayersSince.toString());
            if (callback != null && players != null && players.getItems() != null && players.getItems().size() > 0) {
                callback.handleSendResult(this, players.getItems());
            }
            return players;
        }

        @Override
        public String toString() {
            return "GetPlayersMessage{" +
                    "time=" + time +
                    '}';
        }

        @Override
        public void writeExternal(ObjectOutput output) throws IOException {
            super.writeExternal(output);
            output.writeLong(time);
        }

        @Override
        public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
            super.readExternal(input);
            time = input.readLong();
        }
    }

    public static class TopScoreMessage extends Message {
        private static final long serialVersionUID = 5025471842867533877L;

        interface Callback {
            void handleSendResult(TopScoreMessage message, List<SceneRecord> records);
        }

        static Callback callback = null;

        static void registerCallback(Callback callback) {
            TopScoreMessage.callback = callback;
        }

        private int sceneNo;

        public TopScoreMessage(int sceneNo) {
            this.sceneNo = sceneNo;
        }

        //for externalizable
        public TopScoreMessage() {
        }

        public int getSceneNo() {
            return sceneNo;
        }

        @Override
        protected String getFileName() {
            return "top_score_" + sceneNo;
        }

        @Override
        Object send() throws IOException {
            Log.d(TAG, "Sending message Top Score for scene " + sceneNo);
            SceneService.Top top = BestScoreService.remoteService.top(sceneNo, BestScore.TOP_VISIBLE_SCORES);
            SceneRecordCollection result = top.execute();
            Log.d(TAG, top.toString());
            if (callback != null && result != null && result.getItems() != null && result.getItems().size() > 0) {
                callback.handleSendResult(this, result.getItems());
            }
            return result;
        }

        @Override
        public String toString() {
            return "TopScoreMessage{" +
                    "sceneNo=" + sceneNo +
                    '}';
        }

        @Override
        public void writeExternal(ObjectOutput output) throws IOException {
            super.writeExternal(output);
            output.writeInt(sceneNo);
        }

        @Override
        public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
            super.readExternal(input);
            sceneNo = input.readInt();
        }
    }
}

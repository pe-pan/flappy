package mz800.flappy;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Flappy:
 *   Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 *   Java version by Petr Slechta, 2014.
 */
class Music {

    private static final short SHIFT = 12;
    private static final short G_ = 55 + SHIFT;
    private static final short A_ = 57 + SHIFT;
    private static final short H_ = 59 + SHIFT;
    private static final short C = 60 + SHIFT;
    private static final short D = 62 + SHIFT;
    private static final short E = 64 + SHIFT;
    private static final short F = 65 + SHIFT;
    private static final short FIS = 66 + SHIFT;
    private static final short G = 67 + SHIFT;
    private static final short A = 69 + SHIFT;
    private static final short H = 71 + SHIFT;
    private static final short _C = 72 + SHIFT;
    private static final short _D = 74 + SHIFT;
    private static final short _E = 76 + SHIFT;
    private static final short _G = 79 + SHIFT;
    private static final short R = 1;
    private static final short L2 = 40;
    private static final short L2D = 60;
    private static final short L4 = 20;
    private static final short L4D = 30;
    private static final short L8 = 10;
    private static final short INSTRUMENT = 6;
    private static final short NOTES[][] = {
        {
            INSTRUMENT,
            R, L2, _E, L8, R, L8,
            C, L4, R, L4, E, L8, R, L8,
            C, L4, R, L4, E, L8, R, L8,
            G, L4, G, L8, E, L8, C, L8, E, L8,
            D, L4D, R, L8, F, L8, R, L8,
            H_, L4, R, L4, _D, L8, R, L8,
            G_, L4, R, L4, _D, L8, R, L8,
            F, L4, F, L8, D, L8, H_, L8, _D, L8,
            C, L4D, R, L4D,
            A, L2D,
            R, L2, R, L4,
            R, L8, A, L8, F, L8, A, L8, F, L8, A, L8,
            _D, L8, C, L8, H_, L8, _D, L8, C, L8, A_, L8,
            G, L4, E, L4, C, L4,
            R, L4, E, L4, C, L4,
            R, L4, E, L4, C, L4,
            G, L8, FIS, L8, A, L8, G, L8, F, L8, E, L8,
            D, L4, H_, L8, _G, L8, F, L4,
            D, L4, H_, L8, _G, L8, F, L4,
            D, L4, H_, L8, _G, L8, F, L4,
            H, L8, A, L8, G, L8, F, L8, E, L8, D, L8,
            C, L4, R, L4, H_, L4,
            C, L4, R, L4, H_, L4,
            C, L4, R, L2
        },
        {
            INSTRUMENT,
            R, L2, R, L4,
            C, L4, E, L4, E, L4,
            C, L4, E, L4, E, L4,
            C, L4, E, L4, E, L4,
            G_, L4, _D, L4, D, L4,
            G_, L4, _D, L4, D, L4,
            G_, L4, _D, L4, D, L4,
            G_, L4, H, L4, H, L4,
            _C, L4, E, L4, R, L4,
            C, L4, F, L4, F, L4,
            C, L4, F, L4, F, L4,
            C, L4, F, L4, F, L4,
            C, L4, F, L4, F, L4,
            C, L4, E, L4, E, L4,
            C, L4, E, L4, E, L4,
            C, L4, E, L4, E, L4,
            C, L4, E, L4, E, L4,
            G_, L4, _D, L4, D, L4,
            G_, L4, _D, L4, D, L4,
            G_, L4, _D, L4, D, L4,
            G_, L4, H, L4, H, L4,
            _C, L4, R, L4, G_, L4,
            C, L4, R, L4, G_, L4,
            C, L4, R, L2
        },
        {
            INSTRUMENT,
            R, L2, R, L4,
            R, L4, G, L4, G, L4,
            R, L4, G, L4, G, L4,
            R, L4, G, L4, G, L4,
            R, L4, F, L4, F, L4,
            R, L4, F, L4, F, L4,
            R, L4, F, L4, F, L4,
            R, L4, F, L4, F, L4,
            R, L4, G, L4, R, L4,
            R, L4, A, L4, A, L4,
            R, L4, A, L4, A, L4,
            R, L4, A, L4, A, L4,
            R, L4, A, L4, A, L4,
            R, L4, G, L4, G, L4,
            R, L4, G, L4, G, L4,
            R, L4, G, L4, G, L4,
            R, L4, G, L4, G, L4,
            R, L4, F, L4, F, L4,
            R, L4, F, L4, F, L4,
            R, L4, F, L4, F, L4,
            R, L4, F, L4, F, L4,
            E, L4, R, L4, D, L4,
            E, L4, R, L4, D, L4,
            E, L4, R, L2
        }
    };
    private static final Music me = new Music();

    static Music getInstance() {
        return me;
    }
    private boolean soundOn = false;
    private Sequencer sequencer = null;
    private Sequence song;

    private Music() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            song = new Sequence(Sequence.PPQ, 25);
            initSequence(song, NOTES);
            sequencer.setSequence(song);
            sequencer.setLoopStartPoint(0);
            sequencer.setLoopEndPoint(sequencer.getTickLength());
            sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            soundOn = true;
        } catch (Exception e) {
            e.printStackTrace();
            soundOn = false;
        }
    }

    void disableMusic() {
        soundOn = false;
    }
    
    void start() {
        if (soundOn) {
            try {
                sequencer.stop();
                sequencer.setTickPosition(0);
                sequencer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void stop() {
        if (soundOn) {
            try {
                sequencer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initSequence(Sequence seq, short[][] data) throws Exception {
        // new track
        Track t = seq.createTrack();

        // no of parts
        ShortMessage msg;
        int parts = data.length;
        int[] index = new int[parts];
        long[] wait = new long[parts];

        for (int i = 0, maxi = data.length; i < maxi; i++) {
            // set program (instrument)
            msg = new ShortMessage();
            msg.setMessage(ShortMessage.PROGRAM_CHANGE, i, data[i][0], 0);
            t.add(new MidiEvent(msg, 0));
            // set first note of this part
            index[i] = 1;
            if (data[i][1] != R) {
                msg = new ShortMessage();
                msg.setMessage(ShortMessage.NOTE_ON, i, data[i][1], i == 0 ? 127 : 100);
                t.add(new MidiEvent(msg, 0));
            }
            wait[i] = data[i][2];
        }

        // go through all parts and play notes
        long tick = 0;
        while (true) {
            tick++;
            for (int i = 0; i < parts; i++) {
                // can we play next note?
                if (tick == wait[i]) {
                    int ind = index[i];
                    // stop previous note
                    if (data[i][ind] != R) {
                        msg = new ShortMessage();
                        msg.setMessage(ShortMessage.NOTE_OFF, i, data[i][ind], 0);
                        t.add(new MidiEvent(msg, tick));
                    }
                    // new note?
                    ind += 2;
                    if (ind >= data[i].length) {
                        wait[i] = Long.MAX_VALUE;
                    } else {
                        if (data[i][ind] != R) {
                            msg = new ShortMessage();
                            msg.setMessage(ShortMessage.NOTE_ON, i, data[i][ind], i == 0 ? 127 : 100);
                            t.add(new MidiEvent(msg, tick));
                        }
                        wait[i] = tick + data[i][ind + 1];
                    }
                    index[i] = ind;
                }
            }
            // test if all parts finished
            boolean finishedAll = true;
            for (int i = 0; i < parts; i++) {
                if (wait[i] != Long.MAX_VALUE) {
                    finishedAll = false;
                    break;
                }
            }
            if (finishedAll) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        Music me = Music.getInstance();
        me.start();
    }
}

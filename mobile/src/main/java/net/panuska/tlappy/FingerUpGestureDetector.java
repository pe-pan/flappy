package net.panuska.tlappy;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Flappy:
 * Original game created by dB-SOFT in 1984 for SHARP MZ-800 computer.
 * Java version by Petr Slechta, 2014.
 * Android version by Petr Panuska, 2015.
 */
public class FingerUpGestureDetector extends GestureDetector {
    FingerUpGestureDetector.OnGestureListener fListener;
    public FingerUpGestureDetector(Context context, OnGestureListener listener) {
        super(context, listener);
        fListener = listener;
    }

    public FingerUpGestureDetector(Context context, GestureDetector.OnGestureListener listener, OnGestureListener fListener) {
        super(context, listener);
        this.fListener = fListener;
    }

    public FingerUpGestureDetector(Context context, GestureDetector.OnGestureListener listener, Handler handler, OnGestureListener fListener) {
        super(context, listener, handler);
        this.fListener = fListener;
    }

    public FingerUpGestureDetector(Context context, GestureDetector.OnGestureListener listener, Handler handler, boolean unused, OnGestureListener fListener) {
        super(context, listener, handler, unused);
        this.fListener = fListener;
    }

    public interface OnGestureListener extends GestureDetector.OnGestureListener {
        boolean onFingerUp(MotionEvent e);
    }

    public static class SimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener implements FingerUpGestureDetector.OnGestureListener {
        @Override
        public boolean onFingerUp(MotionEvent e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (super.onTouchEvent(ev)) return true;
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            return fListener.onFingerUp(ev);
        }
        return false;
    }
}

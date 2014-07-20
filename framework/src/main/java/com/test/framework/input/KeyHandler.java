package com.test.framework.input;


import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class KeyHandler implements View.OnKeyListener {

    private final boolean[] pressedKeys;

    private List<KeyEvent> keyEventsBuffer;

    private List<KeyEvent> keyEvents;

    public KeyHandler() {
        // this code is based on the implementation detail of KeyEvent
        // that the codes are sequential from 0, use a simple array for performance
        // 256 is a safe bet (for now)
        pressedKeys = new boolean[256];
        keyEventsBuffer = new ArrayList<KeyEvent>();
        keyEvents = new ArrayList<KeyEvent>();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        synchronized (this) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    pressedKeys[keyCode] = true;
                    break;

                case KeyEvent.ACTION_UP:
                    pressedKeys[keyCode] = false;
                    break;

                default:
                    return false;
            }
            keyEventsBuffer.add(event);
        }
        return true;
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys[keyCode];
    }

    // don't change the array or its member in any way, don't cache
    // just take, process and forget
    // otherwise, you might break things, it is only for performance
    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            List<KeyEvent> events = keyEventsBuffer;
            keyEvents.clear();
            keyEventsBuffer = keyEvents;
            keyEvents = events;
            return events;
        }
    }
}

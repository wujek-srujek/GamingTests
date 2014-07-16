package com.test.mrnom.framework.input;


import android.view.MotionEvent;
import android.view.View;

import com.test.mrnom.framework.Pool;

import java.util.ArrayList;
import java.util.List;


public class TouchHandler implements View.OnTouchListener {

    private final float scaleX;

    private final float scaleY;

    private final int maxTouches;

    private final boolean[] isTouched;

    private final float[] touchX;

    private final float[] touchY;

    private final int[] id;

    private List<TouchEvent> touchEvents;

    private List<TouchEvent> touchEventsBuffer;

    private final Pool<TouchEvent> pool;

    public TouchHandler(int maxTouches, float scaleX, float scaleY) {
        this.maxTouches = maxTouches;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        isTouched = new boolean[maxTouches];
        touchX = new float[maxTouches];
        touchY = new float[maxTouches];
        id = new int[maxTouches];
        for (int i = 0; i < maxTouches; ++i) {
            // isTouched is filled with false by default
            touchX[i] = -1F;
            touchY[i] = -1F;
            id[i] = -1;
        }

        touchEvents = new ArrayList<TouchEvent>();
        touchEventsBuffer = new ArrayList<TouchEvent>();
        pool = new Pool<TouchEvent>(maxTouches * 5) {

            @Override
            protected TouchEvent newObject() {
                return new TouchEvent();
            }
        };
    }

    // the elements in the array move around depending on pointers
    // for example, the touch at index 0 might actually be any touch id
    // touche positions in the array depend on the indices in the event
    // if there are fewer indices than they were in the previous event
    // the first array elements will be moved to the beginning of the
    // array ('compacting'), and the rest of the array will be reset
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int pointerCount = event.getPointerCount();
        synchronized (this) {
            for (int i = 0; i < maxTouches; ++i) {
                // this will reset any previously reported touches whose
                // pointers don't exist any more for this event
                if (i >= pointerCount) {
                    isTouched[i] = false;
                    touchX[i] = -1F;
                    touchY[i] = -1F;
                    id[i] = -1;
                    continue;
                }
                int action = event.getActionMasked();
                if (action != MotionEvent.ACTION_MOVE && i != event.getActionIndex()) {
                    // MotionEvent docs say: for touches, all events happen for one pointer at a time
                    // movements happen in groups,so:
                    // if the current event is other than MOVE, process for current event only
                    // process movements for all indices
                    continue;
                }
                int pointer = event.getPointerId(i);

                TouchEvent touchEvent = pool.getObject();
                touchEvent.pointer = pointer;
                touchEvent.x = event.getX(i) * scaleX;
                touchEvent.y = event.getY(i) * scaleY;

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent.type = TouchEvent.Type.DOWN;
                        isTouched[i] = true;
                        touchX[i] = touchEvent.x;
                        touchY[i] = touchEvent.y;
                        id[i] = pointer;
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        touchEvent.type = TouchEvent.Type.UP;
                        isTouched[i] = false;
                        touchX[i] = -1.F;
                        touchY[i] = -1.F;
                        id[i] = -1;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        touchEvent.type = TouchEvent.Type.MOVE;
                        isTouched[i] = true;
                        touchX[i] = touchEvent.x;
                        touchY[i] = touchEvent.y;
                        id[i] = pointer;
                        break;
                }
                touchEventsBuffer.add(touchEvent);
            }
            return true;
        }
    }

    public boolean isTouchedDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            return index >= 0 && isTouched[index];
        }
    }

    public float getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            return index < 0 ? -1F : touchX[index];
        }
    }

    public float getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            return index < 0 ? -1F : touchY[index];
        }
    }

    // don't change the array or its member in any way, don't cache
    // just take, process and forget
    // otherwise, you might break things, it is only for performance
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                pool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            List<TouchEvent> tmp = touchEventsBuffer;
            touchEventsBuffer = touchEvents;
            touchEvents = tmp;
            return touchEvents;
        }
    }

    // returns the index for the specified pointer or −1
    private int getIndex(int pointer) {
        for (int i = 0; i < maxTouches; ++i) {
            if (id[i] == pointer) {
                return i;
            }
        }
        return -1;
    }
}

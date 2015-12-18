package nl.brusque.iou.promise.eventdispatcher.events;

import nl.brusque.iou.promise.eventdispatcher.IEvent;

public class FulfillEvent implements IEvent {
    private final Object _o;

    public FulfillEvent(Object o) {
        _o = o;
    }

    public Object getValue() {
        return _o;
    }
}

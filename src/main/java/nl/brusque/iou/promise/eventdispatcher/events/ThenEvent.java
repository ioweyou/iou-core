package nl.brusque.iou.promise.eventdispatcher.events;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IRejectable;
import nl.brusque.iou.promise.AbstractPromise;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.promise.eventdispatcher.IEvent;

public class ThenEvent<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEvent {
    public final Object onFulfilled;
    public final Object onRejected;
    public final AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise;

    public ThenEvent(Object onFulfilled, Object onRejected, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        this.onFulfilled = onFulfilled;
        this.onRejected  = onRejected;
        this.nextPromise = nextPromise;
    }
}

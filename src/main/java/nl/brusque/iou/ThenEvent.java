package nl.brusque.iou;

class ThenEvent<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> implements IEvent {
    public final Object onFulfilled;
    public final Object onRejected;
    public final AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise;

    public ThenEvent(Object onFulfilled, Object onRejected, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        this.onFulfilled = onFulfilled;
        this.onRejected  = onRejected;
        this.nextPromise = nextPromise;
    }
}

package nl.brusque.iou;

class ThenEventValue<TResult extends AbstractPromise<TResult>> {
    private final Object onFulfilled;
    private final Object onRejected;
    private final TResult nextPromise;

    public ThenEventValue(Object onFulfilled, Object onRejected, TResult nextPromise) {
        this.onFulfilled = onFulfilled;
        this.onRejected  = onRejected;
        this.nextPromise = nextPromise;
    }

    public Object getFulfillable() {
        return onFulfilled;
    }

    public Object getRejectable() {
        return onRejected;
    }

    public TResult getPromise() {
        return nextPromise;
    }
}

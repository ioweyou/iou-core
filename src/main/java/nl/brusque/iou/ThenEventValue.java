package nl.brusque.iou;

final class ThenEventValue<TFulfill, TOutput> {
    private final IThenCallable<TFulfill, TOutput> onFulfilled;
    private final IThenCallable<Object, TOutput> onRejected;
    private final AbstractPromise nextPromise;

    public ThenEventValue(IThenCallable<TFulfill, TOutput> onFulfilled, IThenCallable<Object, TOutput> onRejected, AbstractPromise nextPromise) {
        this.onFulfilled = onFulfilled;
        this.onRejected  = onRejected;
        this.nextPromise = nextPromise;
    }

    public IThenCallable<TFulfill, TOutput> getFulfillable() {
        return onFulfilled;
    }

    public IThenCallable<Object, TOutput> getRejectable() {
        return onRejected;
    }

    public AbstractPromise getPromise() {
        return nextPromise;
    }
}

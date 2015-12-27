package nl.brusque.iou;

final class ThenEventValue<TInput> {
    private final Object onFulfilled;
    private final Object onRejected;
    private final AbstractPromise<TInput> nextPromise;

    public ThenEventValue(Object onFulfilled, Object onRejected, AbstractPromise<TInput> nextPromise) {
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

    public AbstractPromise<TInput> getPromise() {
        return nextPromise;
    }
}

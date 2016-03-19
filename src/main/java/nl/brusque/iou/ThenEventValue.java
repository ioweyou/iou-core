package nl.brusque.iou;

final class ThenEventValue<TFulfill, TOutput> {
    private final IThenCallable<TFulfill, TOutput> _onFulfilled;
    private final IThenCallable<Object, TOutput> _onRejected;
    private final AbstractPromise _nextPromise;

    ThenEventValue(IThenCallable<TFulfill, TOutput> onFulfilled, IThenCallable<Object, TOutput> onRejected, AbstractPromise nextPromise) {
        _onFulfilled = onFulfilled;
        _onRejected  = onRejected;
        _nextPromise = nextPromise;
    }

    IThenCallable<TFulfill, TOutput> getFulfillable() {
        return _onFulfilled;
    }

    IThenCallable<Object, TOutput> getRejectable() {
        return _onRejected;
    }

    AbstractPromise getPromise() {
        return _nextPromise;
    }
}

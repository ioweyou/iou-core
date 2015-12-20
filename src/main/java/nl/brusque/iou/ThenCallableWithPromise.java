package nl.brusque.iou;

class ThenCallableWithPromise<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable, TCallable extends IThenCallable> {
    private final AbstractPromise<TResult, TFulfillable, TRejectable> _promise;
    private final TCallable _thenCallable;

    public ThenCallableWithPromise(TCallable fulfillable, AbstractPromise<TResult, TFulfillable, TRejectable> promise) {
        _promise      = promise;
        _thenCallable = fulfillable;
    }

    public AbstractPromise<TResult, TFulfillable, TRejectable> getPromise() {
        return _promise;
    }

    public TCallable getCallable() {
        return _thenCallable;
    }
}

package nl.brusque.iou;

class Resolvable<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> {
    private final AbstractPromise<TResult, TFulfillable, TRejectable> _promise;
    private final TFulfillable _fulfillable;
    private final TRejectable _rejectable;

    public Resolvable(TFulfillable fulfillable, TRejectable rejectable, AbstractPromise<TResult, TFulfillable, TRejectable> promise) {
        _promise     = promise;
        _fulfillable = fulfillable;
        _rejectable  = rejectable;
    }

    public AbstractPromise<TResult, TFulfillable, TRejectable> getPromise() {
        return _promise;
    }

    public TFulfillable getFulfillable() {
        return _fulfillable;
    }

    public TRejectable getRejectable() {
        return _rejectable;
    }
}

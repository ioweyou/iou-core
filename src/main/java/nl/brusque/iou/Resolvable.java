package nl.brusque.iou;

class Resolvable<TResult extends AbstractPromise<TResult>, TFulfill, RFulfill, TReject, RReject> {
    private final AbstractPromise<TResult> _promise;
    private final IThenCallable<TFulfill, RFulfill> _fulfillable;
    private final IThenCallable<TReject, RReject> _rejectable;

    public  Resolvable(IThenCallable<TFulfill, RFulfill> fulfillable, IThenCallable<TReject, RReject> rejectable, TResult promise) {
        _promise     = promise;
        _fulfillable = fulfillable;
        _rejectable  = rejectable;
    }

    public AbstractPromise<TResult> getPromise() {
        return _promise;
    }

    public IThenCallable<TFulfill, RFulfill> getFulfillable() {
        return _fulfillable;
    }

    public IThenCallable<TReject, RReject> getRejectable() {
        return _rejectable;
    }
}

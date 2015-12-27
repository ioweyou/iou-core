package nl.brusque.iou;

final class Resolvable<TInput, TFulfill, RFulfill, TReject, RReject> {
    private final AbstractPromise<TInput> _promise;
    private final IThenCallable<TFulfill, RFulfill> _fulfillable;
    private final IThenCallable<TReject, RReject> _rejectable;

    public Resolvable(IThenCallable<TFulfill, RFulfill> fulfillable, IThenCallable<TReject, RReject> rejectable, AbstractPromise<TInput> promise) {
        _promise     = promise;
        _fulfillable = fulfillable;
        _rejectable  = rejectable;
    }

    public AbstractPromise<TInput> getPromise() {
        return _promise;
    }

    public IThenCallable<TFulfill, RFulfill> getFulfillable() {
        return _fulfillable;
    }

    public IThenCallable<TReject, RReject> getRejectable() {
        return _rejectable;
    }
}

package nl.brusque.iou;

final class Resolvable<TFulfill, TOutput> {
    private final AbstractPromise<TOutput> _promise;
    private final IThenCallable<TFulfill, TOutput> _fulfillable;
    private final IThenCallable<Object, TOutput> _rejectable;

    public Resolvable(IThenCallable<TFulfill, TOutput> fulfillable, IThenCallable<Object, TOutput> rejectable, AbstractPromise<TOutput> promise) {
        _promise     = promise;
        _fulfillable = fulfillable;
        _rejectable  = rejectable;
    }

    public AbstractPromise<TOutput> getPromise() {
        return _promise;
    }

    public IThenCallable<TFulfill, TOutput> getFulfillable() {
        return _fulfillable;
    }

    public IThenCallable<Object, TOutput> getRejectable() {
        return _rejectable;
    }

    public void resolve(TFulfill value) throws Exception {
        // This unchecked call might throw an exception, this is a good thing. The promise will be
        // rejected with the exception as reason.
        if (_fulfillable==null) {
            _promise.resolve((TOutput)value);

            return;
        }

        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
        TOutput x = _fulfillable.apply(value);

        _promise.resolve(x);
    }

    public void reject(Object reason) throws Exception {
        if (_rejectable == null) {
            _promise.reject(reason);

            return;
        }

        TOutput result = _rejectable.apply(reason);

        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
        _promise.resolve(result);
    }
}

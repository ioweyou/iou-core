package nl.brusque.iou;

final class Resolvable<TFulfill, TOutput> {
    private final AbstractPromise<TOutput> _nextPromise;
    private final IThenCallable<TFulfill, TOutput> _fulfillable;
    private final IThenCallable<Object, TOutput> _rejectable;

    public Resolvable(IThenCallable<TFulfill, TOutput> fulfillable, IThenCallable<Object, TOutput> rejectable, AbstractPromise<TOutput> nextPromise) {
        _nextPromise = nextPromise;
        _fulfillable = fulfillable;
        _rejectable  = rejectable;
    }

    public AbstractPromise<TOutput> getPromise() {
        return _nextPromise;
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
            _nextPromise.resolve((TOutput)value);

            return;
        }

        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
        TOutput x = _fulfillable.apply(value);

        _nextPromise.resolve(x);
    }

    public void reject(Object reason) throws Exception {
        if (_rejectable == null) {
            _nextPromise.reject(reason);

            return;
        }

        TOutput result = _rejectable.apply(reason);

        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
        _nextPromise.resolve(result);
    }
}

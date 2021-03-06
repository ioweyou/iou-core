package nl.brusque.iou;


final class Resolvable<TFulfill, TOutput> {
    private final AbstractPromise<TOutput> _nextPromise;
    private final IThenCallable<TFulfill, TOutput> _fulfillable;
    private final IThenCallable<Object, TOutput> _rejectable;
    private final AbstractThenCallableStrategy<TFulfill, TOutput> _thenCaller;

    // FIXME Trainwrecks: _nextPromise.get().<something>

    Resolvable(IThenCallable<TFulfill, TOutput> fulfillable, IThenCallable<Object, TOutput> rejectable, AbstractPromise<TOutput> nextPromise, AbstractThenCallableStrategy<TFulfill, TOutput> thenCaller) {
        _nextPromise = nextPromise;
        _fulfillable = fulfillable;
        _rejectable  = rejectable;
        _thenCaller  = thenCaller;
    }

    AbstractPromise<TOutput> getPromise() {
        return _nextPromise;
    }

    void resolve(TFulfill value) throws Exception {
        // This unchecked call might throw an exception, this is a good thing. The promise will be
        // rejected with the exception as reason.
        if (_fulfillable==null) {
            _nextPromise.resolve((TOutput)value);

            return;
        }

        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
        TOutput x = _thenCaller.resolve(_fulfillable, value);

        _nextPromise.resolve(x);
    }

    <TAnything> void reject(TAnything reason) throws Exception {
        if (_rejectable == null) {
            // FIXME Trainwrecks
            if (_nextPromise == null) {
                return;
            }

            _nextPromise.reject(reason);

            return;
        }

        TOutput result = _thenCaller.reject(_rejectable, reason);

        // FIXME Trainwrecks
        if (_nextPromise == null) {
            return;
        }
        // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
        _nextPromise.resolve(result);
    }
}

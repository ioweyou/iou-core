package nl.brusque.iou;

public abstract class AbstractPromise<TResult extends AbstractPromise<TResult>> implements IThenable<TResult> {
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    protected AbstractPromise(IThenCaller fulfiller, IThenCaller rejector) {
        _promiseResolverEventHandler =
                new PromiseResolverEventHandler<>(
                    new EventDispatcher(),
                    fulfiller,
                    rejector);
    }

    protected abstract TResult create();

    final AbstractPromise<TResult> resolve(final Object o) {
        return _promiseResolverEventHandler.resolveWithValue(this, o);
    }

    final AbstractPromise<TResult> reject(final Object o) {
        return _promiseResolverEventHandler.rejectWithValue(this, o);
    }

    @Override
    public final TResult then() {
        return then(null, null);
    }

    @Override
    public final TResult then(Object onFulfilled) {
        return then(onFulfilled, null);
    }

    @Override
    public final TResult then(Object onFulfilled, Object onRejected) {
        TResult nextPromise = create();
        _promiseResolverEventHandler.addThenable(onFulfilled, onRejected, nextPromise);

        return nextPromise;
    }
}

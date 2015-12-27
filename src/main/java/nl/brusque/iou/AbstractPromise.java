package nl.brusque.iou;

public abstract class AbstractPromise<TInput> implements IThenable<TInput> {
    private final PromiseResolverEventHandler _promiseResolverEventHandler;

    protected AbstractPromise(AbstractThenCaller fulfiller, AbstractThenCaller rejector) {
        _promiseResolverEventHandler =
                new PromiseResolverEventHandler(
                    fulfiller,
                    rejector);
    }

    protected abstract <TOutput> AbstractPromise<TOutput> create();

    final AbstractPromise<TInput> resolve(final TInput o) {
        return _promiseResolverEventHandler.resolveWithValue(this, o);
    }

    final AbstractPromise<TInput> reject(final TInput o) {
        return _promiseResolverEventHandler.rejectWithValue(this, o);
    }

    @Override
    public final <TOutput> IThenable<TOutput> then() {
        return then((TInput) null, (TInput) null);
    }

    @Override
    public final <TOutput> IThenable<TOutput> then(TInput onFulfilled) {
        return then(onFulfilled, (TInput)null);
    }

    @Override
    public final <TOutput> IThenable<TOutput> then(TInput onFulfilled, TInput onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    @Override
    public final <TOutput, TAnything> AbstractPromise<TOutput> then(TAnything onFulfilled, IThenCallable<TInput, TOutput> onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    @Override
    public final <TOutput, TAnything> AbstractPromise<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled, TAnything onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    @Override
    public final <TOutput> AbstractPromise<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled) {
        return then(onFulfilled, null);
    }

    @Override
    public final <TOutput> AbstractPromise<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled, IThenCallable<TInput, TOutput> onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    private <TOutput, TFulfilled, TRejected> AbstractPromise<TOutput> addThenable(TFulfilled onFulfilled, TRejected onRejected) {
        AbstractPromise<TOutput> nextPromise = create();
        _promiseResolverEventHandler.addThenable(onFulfilled, onRejected, nextPromise);

        return nextPromise;
    }
}

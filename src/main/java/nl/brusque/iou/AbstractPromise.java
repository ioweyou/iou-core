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
        _promiseResolverEventHandler.resolveWithValue(this, o);

        return this;
    }

    final <TAnything> AbstractPromise<TInput> reject(final TAnything reason) {
        _promiseResolverEventHandler.rejectWithReason(this, reason);

        return this;
    }

    @Override
    public final <TOutput> IThenable<TOutput> then() {
        return then((IThenCallable<TInput, TOutput>) null, null);
    }

    @Override
    public final <TOutput> AbstractPromise<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled) {
        return addThenable(onFulfilled, null);
    }

    @Override
    public final <TOutput, TAnythingInput> AbstractPromise<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled, IThenCallable<TAnythingInput, TOutput> onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    private <TOutput, TFulfilled, TRejected> AbstractPromise<TOutput> addThenable(TFulfilled onFulfilled, TRejected onRejected) {
        AbstractPromise<TOutput> nextPromise = create();
        _promiseResolverEventHandler.addThenable(onFulfilled, onRejected, nextPromise);

        return nextPromise;
    }
}

package nl.brusque.iou;

public abstract class AbstractPromise<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> implements IThenable<TResult> {
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    protected AbstractPromise(Class<TFulfillable> fulfillableClass, Class<TRejectable> rejectableClass) {
        this(fulfillableClass, rejectableClass, null, null, new DefaultThenCaller(), new DefaultThenCaller());
    }

    protected AbstractPromise(Class<TFulfillable> fulfillableClass, Class<TRejectable> rejectableClass, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable>.PromiseThenCallable promiseResolverFulfillableClass, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable>.PromiseRejectable promiseResolverRejectableClass, DefaultThenCaller fulfiller, DefaultThenCaller rejector) {
        _promiseResolverEventHandler =
                new PromiseResolverEventHandler<>(
                    new EventDispatcher(),
                    fulfillableClass,
                    rejectableClass,
                    promiseResolverFulfillableClass,
                    promiseResolverRejectableClass,
                    fulfiller,
                    rejector);
    }

    protected abstract TResult create();

    final AbstractPromise<TResult, TFulfillable, TRejectable> resolve(final Object o) {
        return _promiseResolverEventHandler.resolveWithValue(this, o);
    }

    final AbstractPromise<TResult, TFulfillable, TRejectable> reject(final Object o) {
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

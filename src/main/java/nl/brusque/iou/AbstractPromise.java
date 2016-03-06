package nl.brusque.iou;

public abstract class AbstractPromise<TFulfill> implements IThenable<TFulfill> {
    private final PromiseEventHandler<TFulfill> _promiseEventHandler;

    protected AbstractPromise() {
        _promiseEventHandler = new PromiseEventHandler<>(this);
    }

    protected abstract <TAnythingFulfill> AbstractPromise<TAnythingFulfill> create();

    final void resolve(final TFulfill o) {
        _promiseEventHandler.resolve(o);
    }

    final void reject(final Object reason) {
        _promiseEventHandler.reject(reason);
    }

    @Override
    public final <TAnythingOutput> AbstractPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled) {
        return addThenable(onFulfilled, null);
    }

    @Override
    public final <TAnythingOutput> AbstractPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    private <TAnythingOutput> AbstractPromise<TAnythingOutput> addThenable(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
        AbstractPromise<TAnythingOutput> nextPromise = create();
        _promiseEventHandler.addThenable(onFulfilled, onRejected, nextPromise);

        return nextPromise;
    }
}

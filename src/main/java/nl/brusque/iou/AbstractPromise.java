package nl.brusque.iou;

public abstract class AbstractPromise<TFulfill> implements IThenable<TFulfill> {
    private final PromiseEventHandler<TFulfill> _promiseEventHandler;
    private final PromiseState<TFulfill> _promiseState;

    protected AbstractPromise() {
        ResolvableManager<TFulfill> resolvableManager = new ResolvableManager<>();

        _promiseState = new PromiseState<>(
                this,
                new Fulfiller<>(resolvableManager),
                new Rejector<>(resolvableManager));

        _promiseEventHandler = new PromiseEventHandler<>(_promiseState, resolvableManager);
    }

    protected abstract <TAnythingFulfill> AbstractPromise<TAnythingFulfill> create();

    final void resolve(final TFulfill o) {
        _promiseEventHandler.resolve(this, o);
    }

    final <TAnything> void reject(final TAnything reason) {
        _promiseEventHandler.reject(this, reason);
    }

    @Override
    public <TAnythingOutput> AbstractPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled) {
        return addThenable(onFulfilled, null);
    }

    @Override
    public <TAnythingOutput> AbstractPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
        return addThenable(onFulfilled, onRejected);
    }

    private <TAnythingOutput> AbstractPromise<TAnythingOutput> addThenable(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
        AbstractPromise<TAnythingOutput> nextPromise = create();
        _promiseEventHandler.addThenable(this, onFulfilled, onRejected, nextPromise);

        return nextPromise;
    }

    void shareStateWith(PromiseState state) throws Exception {
        state.registerPromiseState(this, _promiseState);
    }
}

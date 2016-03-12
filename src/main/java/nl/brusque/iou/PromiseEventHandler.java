package nl.brusque.iou;

final class PromiseEventHandler<TFulfill> {
    private final EventDispatcher _eventDispatcher  = new EventDispatcher();
    private final PromiseState<TFulfill> _promiseState;

    public PromiseEventHandler(final AbstractPromise<TFulfill> promise) {
        ResolvableManager<TFulfill> resolvableManager = new ResolvableManager<>();

        _promiseState =
                new PromiseState<>(
                    new Fulfiller<>(resolvableManager),
                    new Rejector<>(resolvableManager));

        _eventDispatcher.addListener(ThenEvent.class, new ThenEventListener<>(_promiseState, resolvableManager, this));
        _eventDispatcher.addListener(ResolveEvent.class, new ResolveEventListener<>(promise, _promiseState));
        _eventDispatcher.addListener(RejectEvent.class, new RejectEventListener<>(_promiseState));
    }

    final synchronized <TAnythingOutput> void addThenable(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected, AbstractPromise nextPromise) {
        _eventDispatcher.queue(new ThenEvent<>(new ThenEventValue<>(onFulfilled, onRejected, nextPromise)));
    }

    final synchronized void resolve(final TFulfill o) {
        _eventDispatcher.queue(new ResolveEvent<>(new ResolveEventValue<>(o)));
    }

    final synchronized <TAnything> void reject(final TAnything reason) {
        _eventDispatcher.queue(new RejectEvent<>(new RejectEventValue<>(reason)));
    }

    @Deprecated
    public PromiseState<TFulfill> leakState() {
        return _promiseState;
    }
}

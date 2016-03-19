package nl.brusque.iou;

final class PromiseEventHandler<TFulfill> {
    private final EventDispatcher _eventDispatcher  = new EventDispatcher();

    PromiseEventHandler(PromiseState<TFulfill> promiseState, ResolvableManager<TFulfill> resolvableManager) {
        _eventDispatcher.addListener(ThenEvent.class, new ThenEventListener<>(promiseState, resolvableManager));
        _eventDispatcher.addListener(ResolveEvent.class, new ResolveEventListener<>(promiseState));
        _eventDispatcher.addListener(RejectEvent.class, new RejectEventListener<>(promiseState));
    }

    final synchronized <TAnythingOutput> void addThenable(AbstractPromise<TFulfill> promise, IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected, AbstractPromise nextPromise) {
        _eventDispatcher.queue(new ThenEvent<>(promise, new ThenEventValue<>(onFulfilled, onRejected, nextPromise)));
    }

    final synchronized void resolve(AbstractPromise<TFulfill> promise, final TFulfill o) {
        _eventDispatcher.queue(new ResolveEvent<>(promise, o));
    }

    final synchronized <TAnything> void reject(AbstractPromise<TFulfill> promise, final TAnything reason) {
        _eventDispatcher.queue(new RejectEvent<>(promise,reason));
    }
}

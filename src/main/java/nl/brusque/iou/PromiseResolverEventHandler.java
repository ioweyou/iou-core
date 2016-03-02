package nl.brusque.iou;

import nl.brusque.iou.errors.TypeError;
import nl.brusque.iou.errors.TypeErrorException;

final class PromiseResolverEventHandler {
    private final PromiseStateHandler _promiseState = new PromiseStateHandler();
    private final EventDispatcher _eventDispatcher  = new EventDispatcher();

    public PromiseResolverEventHandler(AbstractThenCaller fulfiller, AbstractThenCaller rejector) {
        ResolvableManager resolvableManager = new ResolvableManager();

        _eventDispatcher.addListener(ThenEvent.class, new ThenEventListener<>(_eventDispatcher, _promiseState, resolvableManager));
        _eventDispatcher.addListener(FulfillEvent.class, new FulfillEventListener<>(_eventDispatcher, _promiseState));
        _eventDispatcher.addListener(FireFulfillsEvent.class, new FireFulfillsEventListener<>(_promiseState, resolvableManager, fulfiller));
        _eventDispatcher.addListener(RejectEvent.class, new RejectEventListener<>(_eventDispatcher, _promiseState));
        _eventDispatcher.addListener(FireRejectsEvent.class, new FireRejectsEventListener<>(_promiseState, resolvableManager, rejector));
    }

    final synchronized <TInput> void addThenable(Object onFulfilled, Object onRejected, AbstractPromise<TInput> nextPromise) {
        _eventDispatcher.queue(new ThenEvent<>(new ThenEventValue<>(onFulfilled, onRejected, nextPromise)));
    }

    final synchronized <TInput> AbstractPromise<TInput> resolveWithValue(final AbstractPromise<TInput> promise, final TInput o) {
        return resolvePromise(promise, FulfillEvent.class, RejectEvent.class, o);
    }

    final synchronized <TInput> AbstractPromise<TInput> rejectWithValue(final AbstractPromise<TInput> promise, final TInput o) {
        return resolvePromise(promise, RejectEvent.class, RejectEvent.class, o);
    }

    private <TInput> boolean testObjectEqualsPromise(Object o, AbstractPromise<TInput> promise) {
        return o != null && o.equals(promise);
    }

    private <TInput> AbstractPromise<TInput> resolvePromise(final AbstractPromise<TInput> promise, final Class<? extends DefaultEvent> event, final Class<? extends DefaultEvent> onFailEvent, final Object o) {
        if (!_promiseState.isPending()) {
            return promise;
        }

        try {
            if (testObjectEqualsPromise(o, promise)) {
                throw new TypeErrorException();
            }
        } catch (TypeErrorException e) {
            // 2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError' as the reason.
            _eventDispatcher.queue(EventFactory.create(onFailEvent, new TypeError()));
            return promise;
        }

        _eventDispatcher.queue(EventFactory.create(event, o));

        return promise;
    }
}

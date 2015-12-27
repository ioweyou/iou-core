package nl.brusque.iou;

import nl.brusque.iou.errors.TypeError;
import nl.brusque.iou.errors.TypeErrorException;

import java.util.ArrayDeque;

class PromiseResolverEventHandler<TResult extends AbstractPromise<TResult>> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;

    private final ArrayDeque<Resolvable> _onResolve = new ArrayDeque<>();
    private final AbstractThenCaller _fulfiller;
    private final AbstractThenCaller _rejector;

    public PromiseResolverEventHandler(EventDispatcher eventDispatcher, AbstractThenCaller fulfiller, AbstractThenCaller rejector) {
        _promiseState    = new PromiseStateHandler();
        _eventDispatcher = eventDispatcher;

        _fulfiller = fulfiller;
        _rejector  = rejector;

        eventDispatcher.addListener(ThenEvent.class, new ThenEventListener<>(this));
        eventDispatcher.addListener(FulfillEvent.class, new FulfillEventListener<>(_promiseState, eventDispatcher, this));
        eventDispatcher.addListener(FireFulfillsEvent.class, new FireFulfillsEventListener<>(this));
        eventDispatcher.addListener(RejectEvent.class, new RejectEventListener<>(_promiseState, eventDispatcher, this));
        eventDispatcher.addListener(FireRejectsEvent.class, new FireRejectsEventListener<>(this));
    }

    private class PromiseRejectable implements IThenCallable {
        @Override
        public Object apply(Object o) throws Exception {
            _promiseState.reject(o);
            _eventDispatcher.queue(new FireRejectsEvent());

            return o;
        }
    }

    private class PromiseThenFulfillable implements IThenCallable {
        @Override
        public Object apply(Object o) throws Exception {
            _promiseState.fulfill(o);
            _eventDispatcher.queue(new FireFulfillsEvent());

            return o;
        }
    }

    synchronized void resolvePromiseValue(AbstractPromise promise) {
        promise.then(new PromiseThenFulfillable(), new PromiseRejectable());
    }

    synchronized <TFulfillable, RFulfillable, TRejectable, RRejectable> void addResolvable(IThenCallable<TFulfillable, RFulfillable> fulfillable, IThenCallable<TRejectable, RRejectable> rejectable, TResult nextPromise) {
        _onResolve.add(new Resolvable<>(fulfillable, rejectable, nextPromise));

        if (_promiseState.isRejected()) {
            _eventDispatcher.queue(new FireRejectsEvent());
        } else if (_promiseState.isResolved()) {
            _eventDispatcher.queue(new FireFulfillsEvent());
        }
    }

    synchronized void addThenable(Object onFulfilled, Object onRejected, TResult nextPromise) {
        _eventDispatcher.queue(new ThenEvent<>(new ThenEventValue<>(onFulfilled, onRejected, nextPromise)));
    }

    synchronized void fireResolvables() {
        while (!_onResolve.isEmpty()) {
            Resolvable resolvable = _onResolve.remove();
            IThenCallable fulfillable = resolvable.getFulfillable();

            try {
                if (fulfillable == null) {
                    resolvable.getPromise().resolve(_promiseState.getResolvedWith());
                    return;
                }
                Object result = _fulfiller.call(fulfillable, _promiseState.getResolvedWith());
                if (result == null) {
                    return;
                }

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                resolvable.getPromise().resolve(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                resolvable.getPromise().reject(e);
            }
        }
    }

    synchronized void fireRejectables() {
        while (!_onResolve.isEmpty()) {
            Resolvable resolvable = _onResolve.remove();
            IThenCallable  rejectable  = resolvable.getRejectable();

            try {
                if (rejectable == null) {
                    resolvable.getPromise().reject(_promiseState.RejectedWith());
                    return;
                }

                Object result = _rejector.call(rejectable, _promiseState.RejectedWith());

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                resolvable.getPromise().reject(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                resolvable.getPromise().reject(e);
            }
        }
    }

    synchronized AbstractPromise<TResult> resolveWithValue(final AbstractPromise<TResult> promise, final Object o) {
        return resolvePromise(promise, FulfillEvent.class, RejectEvent.class, o);
    }

    synchronized AbstractPromise<TResult> rejectWithValue(final AbstractPromise<TResult> promise, final Object o) {
        return resolvePromise(promise, RejectEvent.class, RejectEvent.class, o);
    }

    private boolean testObjectEqualsPromise(Object o, AbstractPromise<TResult> promise) {
        return o != null && o.equals(promise);
    }

    private AbstractPromise<TResult> resolvePromise(final AbstractPromise<TResult> promise, final Class<? extends AbstractEvent> event, final Class<? extends AbstractEvent> onFailEvent, final Object o) {
        if (!_promiseState.isPending()) {
            return promise;
        }

        try {
            if (testObjectEqualsPromise(o, promise)) {
                throw new TypeErrorException();
            }
        } catch (TypeErrorException e) {
            // 2.3.1: If `promise` and `x` refer to the same object, fireRejectables `promise` with a `TypeError' as the reason.
            _eventDispatcher.queue(EventFactory.create(onFailEvent, new TypeError()));
            return promise;
        }

        _eventDispatcher.queue(EventFactory.create(event, o));

        return promise;
    }
}

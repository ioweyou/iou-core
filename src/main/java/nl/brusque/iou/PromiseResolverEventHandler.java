package nl.brusque.iou;

import java.util.ArrayDeque;

class PromiseResolverEventHandler<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;

    private final ArrayDeque<FulfillableWithPromise<TResult, TFulfillable, TRejectable>> _onFulfilleds = new ArrayDeque<>();
    private final ArrayDeque<RejectableWithPromise<TResult, TFulfillable, TRejectable>> _onRejecteds   = new ArrayDeque<>();
    private final PromiseFulfillable _promiseResolverFulfillableClass;
    private final PromiseRejectable _promiseResolverRejectableClass;

    public PromiseResolverEventHandler(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseFulfillable promiseFulfiller, PromiseRejectable promiseRejector) {
        _promiseState    = promiseState;
        _eventDispatcher = eventDispatcher;

        _promiseResolverFulfillableClass = promiseFulfiller!=null ? promiseFulfiller : new PromiseFulfillable();
        _promiseResolverRejectableClass  = promiseRejector!=null ? promiseRejector : new PromiseRejectable();

        eventDispatcher.addListener(FulfillEvent.class, new FulfillEventListener<>(promiseState, eventDispatcher, this));
        eventDispatcher.addListener(FireFulfillsEvent.class, new FireFulfillsEventListener<>(this));
        eventDispatcher.addListener(RejectEvent.class, new RejectEventListener<>(promiseState, eventDispatcher, this));
        eventDispatcher.addListener(FireRejectsEvent.class, new FireRejectsEventListener<>(this));
    }

    public class PromiseRejectable implements IRejectable {
        @Override
        public Object reject(Object o) throws Exception {
            _promiseState.reject(o);
            _eventDispatcher.queue(new FireRejectsEvent());

            return o;
        }
    }

    public class PromiseFulfillable implements IFulfillable {
        @Override
        public Object fulfill(Object o) throws Exception {
            _promiseState.fulfill(o);
            _eventDispatcher.queue(new FireFulfillsEvent());

            return o;
        }
    }

    public synchronized void resolvePromiseValue(AbstractPromise promise) {
        promise.then(_promiseResolverFulfillableClass, _promiseResolverRejectableClass);
    }

    public synchronized void addFulfillable(TFulfillable fulfillable, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        _onFulfilleds.add(new FulfillableWithPromise<>(fulfillable, nextPromise));
    }
    public synchronized void addRejectable(TRejectable rejectable, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        _onRejecteds.add(new RejectableWithPromise<>(rejectable, nextPromise));
    }

    public synchronized void resolve() {
        while (!_onFulfilleds.isEmpty()) {
            FulfillableWithPromise<TResult, TFulfillable, TRejectable> fulfilled = _onFulfilleds.remove();

            try {
                Object result = fulfilled.getFulfillable().fulfill(_promiseState.getResolvedWith());
                if (result == null) {
                    return;
                }

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                fulfilled.getPromise().resolve(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                fulfilled.getPromise().reject(e);
            }
        }
    }

    public synchronized void reject() {
        while (!_onRejecteds.isEmpty()) {
            RejectableWithPromise<TResult, TFulfillable, TRejectable> onRejected = _onRejecteds.remove();

            try {
                Object result = onRejected.getRejectable().reject(_promiseState.RejectedWith());
                if (result == null) {
                    return;
                }

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                onRejected.getPromise().reject(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                onRejected.getPromise().reject(e);
            }
        }
    }

}

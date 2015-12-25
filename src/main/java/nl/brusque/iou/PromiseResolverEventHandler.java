package nl.brusque.iou;

import java.util.ArrayDeque;

class PromiseResolverEventHandler<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;

    private final ArrayDeque<Resolvable<TResult, TFulfillable, TRejectable>> _onResolve = new ArrayDeque<>();
    private final PromiseThenCallable _promiseResolverFulfillableClass;
    private final PromiseRejectable _promiseResolverRejectableClass;
    private final DefaultThenCallable<TFulfillable> _fulfiller;
    private final DefaultThenCallable<TRejectable> _rejector;

    public PromiseResolverEventHandler(
            PromiseStateHandler promiseState,
            EventDispatcher eventDispatcher,
            PromiseThenCallable promiseFulfiller,
            PromiseRejectable promiseRejector,
            DefaultThenCallable<TFulfillable> fulfiller,
            DefaultThenCallable<TRejectable> rejector
        ) {
        _promiseState    = promiseState;
        _eventDispatcher = eventDispatcher;

        _promiseResolverFulfillableClass = promiseFulfiller!=null ? promiseFulfiller : new PromiseThenCallable();
        _promiseResolverRejectableClass  = promiseRejector!=null ? promiseRejector : new PromiseRejectable();
        _fulfiller = fulfiller;
        _rejector  = rejector;

        eventDispatcher.addListener(FulfillEvent.class, new FulfillEventListener<>(promiseState, eventDispatcher, this));
        eventDispatcher.addListener(FireFulfillsEvent.class, new FireFulfillsEventListener<>(this));
        eventDispatcher.addListener(RejectEvent.class, new RejectEventListener<>(promiseState, eventDispatcher, this));
        eventDispatcher.addListener(FireRejectsEvent.class, new FireRejectsEventListener<>(this));
    }

    public class PromiseRejectable implements IThenCallable {
        @Override
        public Object apply(Object o) throws Exception {
            _promiseState.reject(o);
            _eventDispatcher.queue(new FireRejectsEvent());

            return o;
        }
    }

    public class PromiseThenCallable implements IThenCallable {
        @Override
        public Object apply(Object o) throws Exception {
            _promiseState.fulfill(o);
            _eventDispatcher.queue(new FireFulfillsEvent());

            return o;
        }
    }

    public synchronized void resolvePromiseValue(AbstractPromise promise) {
        promise.then(_promiseResolverFulfillableClass, _promiseResolverRejectableClass);
    }

    public synchronized void addResolvable(TFulfillable fulfillable, TRejectable rejectable, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        _onResolve.add(new Resolvable<>(fulfillable, rejectable, nextPromise));
    }

    public synchronized void resolve() {
        while (!_onResolve.isEmpty()) {
            Resolvable<TResult, TFulfillable, TRejectable> resolvable = _onResolve.remove();
            TFulfillable fulfillable = resolvable.getFulfillable();

            try {
                if (fulfillable == null) {
                    resolvable.getPromise().resolve(_promiseState.getResolvedWith());
                    return;
                }
                Object result = _fulfiller.apply(fulfillable, _promiseState.getResolvedWith());
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

    public synchronized void reject() {
        while (!_onResolve.isEmpty()) {
            Resolvable<TResult, TFulfillable, TRejectable> resolvable = _onResolve.remove();
            TRejectable  rejectable  = resolvable.getRejectable();

            try {
                if (rejectable == null) {
                    resolvable.getPromise().reject(_promiseState.RejectedWith());
                    return;
                }

                Object result = _rejector.apply(rejectable, _promiseState.RejectedWith());

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                resolvable.getPromise().reject(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                resolvable.getPromise().reject(e);
            }
        }
    }

}

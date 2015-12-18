package nl.brusque.iou.promise;

import nl.brusque.iou.*;
import nl.brusque.iou.errors.TypeError;
import nl.brusque.iou.errors.TypeErrorException;
import nl.brusque.iou.promise.eventdispatcher.*;
import nl.brusque.iou.promise.eventdispatcher.events.*;

import java.util.ArrayDeque;

public abstract class AbstractPromise<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IPromise<AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable, TRejectable> {
    private final PromiseStateHandler _promiseState                = new PromiseStateHandler();
    private final ArrayDeque<FulfillableWithPromise<TResult, TFulfillable, TRejectable>> _onFulfilleds = new ArrayDeque<>();
    private final ArrayDeque<RejectableWithPromise<TResult, TFulfillable, TRejectable>> _onRejecteds   = new ArrayDeque<>();
    private final EventDispatcher _eventDispatcher                 = new EventDispatcher();
    private final PromiseResolver _promiseResolver;

    private class PromiseResolver {
        private final PromiseFulfillable _promiseFulfillable;
        private final PromiseRejectable _promiseRejectable;

        public PromiseResolver(PromiseFulfillable fulfillable, PromiseRejectable rejectable) {
            _promiseFulfillable = fulfillable;
            _promiseRejectable = rejectable;
        }

        public void resolve(IPromise promise) {
            promise.then(_promiseFulfillable, _promiseRejectable);
        }
    }

    // FIXME Separate event listeners from this class
    // <editor-fold desc="Event listeners">
    private class FulfillEventListener implements IEventListener<FulfillEvent> {
        @Override
        public void process(FulfillEvent event) {
            if (!_promiseState.isPending()) {
                return;
            }

            if (event.getValue() instanceof IPromise) {
                _promiseResolver.resolve((IPromise)event.getValue());

                return;
            }

            _promiseState.fulfill(event.getValue());
            _eventDispatcher.queue(new FireFulfillsEvent());
        }
    }

    private class RejectEventListener implements IEventListener<RejectEvent> {
        @Override
        public void process(RejectEvent event) {
            if (!_promiseState.isPending()) {
                return;
            }

            if (event.getValue() instanceof IPromise) {
                _promiseResolver.resolve((IPromise)event.getValue());

                return;
            }

            _promiseState.reject(event.getValue());
            _eventDispatcher.queue(new FireRejectsEvent());
        }
    }

    private class ThenEventListener implements IEventListener<ThenEvent> {
        @Override
        public void process(ThenEvent event) {
            boolean isFulfillable = isFulfillable(event.onFulfilled);
            boolean isRejectable  = isRejectable(event.onRejected);

            if (!isFulfillable || !isRejectable) {
                Log.w(String.format("isFulfillable: %s, isRejectable: %s", isFulfillable, isRejectable));
            }

            if (isFulfillable) {
                addFulfillable((TFulfillable)event.onFulfilled, event.nextPromise);
            }

            if (isRejectable) {
                addRejectable((TRejectable)event.onRejected, event.nextPromise);
            }

            if (_promiseState.isRejected()) {
                _eventDispatcher.queue(new FireRejectsEvent());
            } else if (_promiseState.isResolved()) {
                _eventDispatcher.queue(new FireFulfillsEvent());
            }
        }
    }

    private class FireFulfillsEventListener implements IEventListener<FireFulfillsEvent> {
        @Override
        public void process(FireFulfillsEvent event) {
            nextResolve();
        }
    }

    private class FireRejectsEventListener implements IEventListener<FireRejectsEvent> {
        @Override
        public void process(FireRejectsEvent event) {
            nextReject();
        }
    }
    // </editor-fold>

    public AbstractPromise() {
        _eventDispatcher.addListener(ThenEvent.class, new ThenEventListener());
        _eventDispatcher.addListener(FulfillEvent.class, new FulfillEventListener());
        _eventDispatcher.addListener(RejectEvent.class, new RejectEventListener());
        _eventDispatcher.addListener(FireFulfillsEvent.class, new FireFulfillsEventListener());
        _eventDispatcher.addListener(FireRejectsEvent.class, new FireRejectsEventListener());

        _promiseResolver = new PromiseResolver(new PromiseFulfillable(), new PromiseRejectable());
    }

    private synchronized void addFulfillable(TFulfillable fulfillable, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        _onFulfilleds.add(new FulfillableWithPromise<>(fulfillable, nextPromise));
    }
    private synchronized void addRejectable(TRejectable rejectable, AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise) {
        _onRejecteds.add(new RejectableWithPromise<>(rejectable, nextPromise));
    }

    private class PromiseFulfillable implements IFulfillable {
        @Override
        public Object fulfill(Object o) throws Exception {
            _promiseState.fulfill(o);
            _eventDispatcher.queue(new FireFulfillsEvent());

            return o;
        }
    }

    private class PromiseRejectable implements IRejectable {
        @Override
        public Object reject(Object o) throws Exception {
            _promiseState.reject(o);
            _eventDispatcher.queue(new FireRejectsEvent());

            return o;
        }
    }

    public AbstractPromise<TResult, TFulfillable, TRejectable> resolve(final Object o) {
        if (!_promiseState.isPending()) {
            return this;
        }

        try {
            if (o!=null && o.equals(this)) {
                throw new TypeErrorException();
            }
        } catch (TypeErrorException e) {
            // 2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError' as the reason.
            _eventDispatcher.queue(new RejectEvent(new TypeError()));
            return this;
        }

        _eventDispatcher.queue(new FulfillEvent(o));

        return this;
    }

    private void nextResolve() {
        while (!_onFulfilleds.isEmpty()) {
            FulfillableWithPromise<TResult, TFulfillable, TRejectable> fulfilled = _onFulfilleds.remove();

            try {
                Object result = runFulfill(fulfilled.getFulfillable(), _promiseState.getResolvedWith());
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

    public AbstractPromise<TResult, TFulfillable, TRejectable> reject(final Object o) {
        if (!_promiseState.isPending()) {
            return this;
        }

        try {
            if (o!=null && o.equals(this)) {
                throw new TypeErrorException();
            }
        } catch (TypeErrorException e) {
            // 2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError' as the reason.
            _eventDispatcher.queue(new RejectEvent(new TypeError()));
            return this;
        }

        _eventDispatcher.queue(new RejectEvent(o));

        return this;
    }

    private void nextReject() {
        while (!_onRejecteds.isEmpty()) {
            RejectableWithPromise<TResult, TFulfillable, TRejectable> onRejected = _onRejecteds.remove();

            try {
                Object result = runReject(onRejected.getRejectable(), _promiseState.RejectedWith());
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

    @Override
    public boolean isFulfillable(Object onFulfilled) {
        return onFulfilled != null && onFulfilled instanceof IFulfillable;
    }

    @Override
    public boolean isRejectable(Object onRejected) {
        return onRejected != null && onRejected instanceof IRejectable;
    }

    @Override
    public Object runFulfill(TFulfillable fulfillable, Object o) throws Exception {
        return fulfillable.fulfill(o);
    }

    @Override
    public Object runReject(IRejectable rejectable, Object o) throws Exception {
        return rejectable.reject(o);
    }

    @Override
    public AbstractPromise<TResult, TFulfillable, TRejectable> then() {
        return then(null, null);
    }

    @Override
    public AbstractPromise<TResult, TFulfillable, TRejectable> then(Object onFulfilled) {
        return then(onFulfilled, null);
    }

    @Override
    public AbstractPromise<TResult, TFulfillable, TRejectable> then(Object onFulfilled, Object onRejected) {
        AbstractPromise<TResult, TFulfillable, TRejectable> nextPromise = create();
        _eventDispatcher.queue(new ThenEvent<>(onFulfilled, onRejected, nextPromise));

        return nextPromise;
    }
}

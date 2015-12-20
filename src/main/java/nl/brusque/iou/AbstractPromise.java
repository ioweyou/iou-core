package nl.brusque.iou;

import nl.brusque.iou.errors.TypeError;
import nl.brusque.iou.errors.TypeErrorException;

public abstract class AbstractPromise<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> implements IThenable<AbstractPromise<TResult, TFulfillable, TRejectable>> {
    private final PromiseStateHandler _promiseState = new PromiseStateHandler();
    private final EventDispatcher _eventDispatcher  = new EventDispatcher();


    protected AbstractPromise(Class<TFulfillable> fulfillableClass, Class<TRejectable> rejectableClass) {
        this(fulfillableClass, rejectableClass, null, null, new DefaultThenCallable<TFulfillable>(), new DefaultThenCallable<TRejectable>());
    }

    protected AbstractPromise(Class<TFulfillable> fulfillableClass, Class<TRejectable> rejectableClass, PromiseResolverEventHandler.PromiseThenCallable promiseResolverFulfillableClass, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable>.PromiseRejectable promiseResolverRejectableClass, DefaultThenCallable<TFulfillable> fulfiller, DefaultThenCallable<TRejectable> rejector) {
        PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler =
                new PromiseResolverEventHandler<>(_promiseState, _eventDispatcher, promiseResolverFulfillableClass, promiseResolverRejectableClass, fulfiller, rejector);

        _eventDispatcher.addListener(
                ThenEvent.class,
                new ThenEventListener<>(
                        _promiseState,
                        _eventDispatcher,
                        promiseResolverEventHandler,
                        fulfillableClass,
                        rejectableClass));
    }

    protected abstract TResult create();

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

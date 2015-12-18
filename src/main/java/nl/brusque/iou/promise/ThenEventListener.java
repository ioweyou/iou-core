package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;
import nl.brusque.iou.Log;
import nl.brusque.iou.promise.eventdispatcher.IEventListener;
import nl.brusque.iou.promise.eventdispatcher.events.FireFulfillsEvent;
import nl.brusque.iou.promise.eventdispatcher.events.FireRejectsEvent;
import nl.brusque.iou.promise.eventdispatcher.events.ThenEvent;

class ThenEventListener<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<ThenEvent<TResult, TFulfillable, TRejectable>> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;
    private final Class<TFulfillable> _fulfillableClass;
    private final Class<TRejectable> _rejectableClass;

    public ThenEventListener(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler, Class<TFulfillable> fulfillableClass, Class<TRejectable> rejectableClass) {
        _promiseState                = promiseState;
        _eventDispatcher             = eventDispatcher;
        _promiseResolverEventHandler = promiseResolverEventHandler;
        _fulfillableClass            = fulfillableClass;
        _rejectableClass             = rejectableClass;
    }

    private boolean isFulfillable(Object onFulfilled, Class<TFulfillable> clazz) {
        return onFulfilled != null && clazz.isInstance(onFulfilled);
    }

    private boolean isRejectable(Object onRejected, Class<TRejectable> clazz) {
        return onRejected != null && clazz.isInstance(onRejected);
    }

    @Override
    public void process(ThenEvent<TResult, TFulfillable, TRejectable> event) {
        boolean isFulfillable = isFulfillable(event.onFulfilled, _fulfillableClass);
        boolean isRejectable  = isRejectable(event.onRejected, _rejectableClass);

        if (!isFulfillable || !isRejectable) {
            Log.w(String.format("isFulfillable: %s, isRejectable: %s", isFulfillable, isRejectable));
        }

        if (isFulfillable) {
            _promiseResolverEventHandler.addFulfillable(_fulfillableClass.cast(event.onFulfilled), event.nextPromise);
        }

        if (isRejectable) {
            _promiseResolverEventHandler.addRejectable(_rejectableClass.cast(event.onRejected), event.nextPromise);
        }

        if (_promiseState.isRejected()) {
            _eventDispatcher.queue(new FireRejectsEvent());
        } else if (_promiseState.isResolved()) {
            _eventDispatcher.queue(new FireFulfillsEvent());
        }
    }
}

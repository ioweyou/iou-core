package nl.brusque.iou;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

class ThenEventListener<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> implements IEventListener<ThenEvent<TResult, TFulfillable, TRejectable>> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;
    private final Class<TFulfillable> _fulfillableClass;
    private final Class<TRejectable> _rejectableClass;

    private static final Logger logger = LogManager.getLogger(ThenEventListener.class);

    public ThenEventListener(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler, Class<TFulfillable> fulfillableClass, Class<TRejectable> rejectableClass) {
        _promiseState                = promiseState;
        _eventDispatcher             = eventDispatcher;
        _promiseResolverEventHandler = promiseResolverEventHandler;
        _fulfillableClass            = fulfillableClass;
        _rejectableClass             = rejectableClass;
    }

    private boolean isFulfillable(Object onFulfilled, Class<TFulfillable> clazz) {
        return onFulfilled != null && onFulfilled instanceof IThenCallable; // FIXME Compare to clazz
    }

    private boolean isRejectable(Object onRejected, Class<TRejectable> clazz) {
        return onRejected != null && onRejected instanceof IThenCallable; // FIXME Compare to clazz
    }

    @Override
    public void process(ThenEvent<TResult, TFulfillable, TRejectable> event) {
        boolean isFulfillable = isFulfillable(event.onFulfilled, _fulfillableClass);
        boolean isRejectable  = isRejectable(event.onRejected, _rejectableClass);

        if (!isFulfillable || !isRejectable) {
            logger.warn(String.format("isFulfillable: %s, isRejectable: %s", isFulfillable, isRejectable));
        }

        TFulfillable fulfillable = null;
        if (isFulfillable) {
            fulfillable = _fulfillableClass.cast(event.onFulfilled);
        }

        TRejectable rejectable = null;
        if (isRejectable) {
            rejectable = _rejectableClass.cast(event.onRejected);
        }

        _promiseResolverEventHandler.addResolvable(fulfillable, rejectable, event.nextPromise);
        if (_promiseState.isRejected()) {
            _eventDispatcher.queue(new FireRejectsEvent());
        } else if (_promiseState.isResolved()) {
            _eventDispatcher.queue(new FireFulfillsEvent());
        }
    }
}

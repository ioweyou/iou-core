package nl.brusque.iou;

class ThenEventListener<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<ThenEvent<TResult, TFulfillable, TRejectable>> {
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
        return onFulfilled != null && onFulfilled instanceof IFulfillable; // FIXME Compare to clazz
    }

    private boolean isRejectable(Object onRejected, Class<TRejectable> clazz) {
        return onRejected != null && onRejected instanceof IRejectable; // FIXME Compare to clazz
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

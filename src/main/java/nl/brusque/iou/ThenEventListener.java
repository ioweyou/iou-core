package nl.brusque.iou;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

class ThenEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<ThenEvent<TResult>> {
    private static final Logger logger = LogManager.getLogger(ThenEventListener.class);
    private final EventDispatcher _eventDispatcher;
    private final PromiseStateHandler _promiseState;
    private final ResolvableManager _resolvableManager;

    public ThenEventListener(EventDispatcher eventDispatcher, PromiseStateHandler promiseState, ResolvableManager resolvableManager) {
        _eventDispatcher   = eventDispatcher;
        _promiseState      = promiseState;
        _resolvableManager = resolvableManager;
    }

    private IThenCallable castThenCallable(Object maybeThenCallable) {
        if (maybeThenCallable != null && maybeThenCallable instanceof IThenCallable) {
            return (IThenCallable)maybeThenCallable;
        }

        return null;
    }

    @Override
    public void process(ThenEvent<TResult> event) {
        IThenCallable fulfillable = castThenCallable(event.getFulfillable());
        IThenCallable rejectable = castThenCallable(event.getRejectable());
        boolean isFulfillable = fulfillable!=null;
        boolean isRejectable  = rejectable!=null;

        if (!isFulfillable || !isRejectable) {
            logger.warn(String.format("isFulfillable: %s, isRejectable: %s", isFulfillable, isRejectable));
        }

        addResolvable(fulfillable, rejectable, event.getPromise());
    }

    private <TFulfillable, RFulfillable, TRejectable, RRejectable> void addResolvable(IThenCallable<TFulfillable, RFulfillable> fulfillable, IThenCallable<TRejectable, RRejectable> rejectable, TResult nextPromise) {
        _resolvableManager.add(new Resolvable<>(fulfillable, rejectable, nextPromise));

        if (_promiseState.isRejected()) {
            _eventDispatcher.queue(new FireRejectsEvent());
        } else if (_promiseState.isResolved()) {
            _eventDispatcher.queue(new FireFulfillsEvent());
        }
    }
}

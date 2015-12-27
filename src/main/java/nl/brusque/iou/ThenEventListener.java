package nl.brusque.iou;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

class ThenEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<ThenEvent<TResult>> {
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    private static final Logger logger = LogManager.getLogger(ThenEventListener.class);

    public ThenEventListener(PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
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

        _promiseResolverEventHandler.addResolvable(fulfillable, rejectable, event.getPromise());
    }
}

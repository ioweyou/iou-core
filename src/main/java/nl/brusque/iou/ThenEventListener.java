package nl.brusque.iou;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

final class ThenEventListener<TFulfill, TOutput> implements IEventListener<ThenEvent<TFulfill, TOutput>> {
    private static final Logger logger                 = LogManager.getLogger(ThenEventListener.class);
    private final ResolvableManager<TFulfill> _resolvableManager;
    private final PromiseState<TFulfill, TOutput> _promiseState;
    private final PromiseEventHandler<TFulfill> _promiseEventHandler;


    public ThenEventListener(PromiseState<TFulfill, TOutput> promiseState, ResolvableManager<TFulfill> resolvableManager, PromiseEventHandler<TFulfill> promiseEventHandler) {
        _promiseState        = promiseState;
        _resolvableManager   = resolvableManager;
        _promiseEventHandler = promiseEventHandler;
    }

    @Override
    public void process(ThenEvent<TFulfill, TOutput> event) {
        IThenCallable<TFulfill, TOutput> fulfillable = event.getFulfillable();
        IThenCallable<Object, TOutput> rejectable  = event.getRejectable();
        boolean isFulfillable = fulfillable!=null;
        boolean isRejectable  = rejectable!=null;

        if (!isFulfillable || !isRejectable) {
            logger.warn(String.format("isFulfillable: %s, isRejectable: %s", isFulfillable, isRejectable));
        }

        addResolvable(fulfillable, rejectable, event.getPromise());
    }

    private void addResolvable(IThenCallable<TFulfill, TOutput> fulfillable, IThenCallable<Object, TOutput> rejectable, AbstractPromise<TOutput> nextPromise) {
        _resolvableManager.add(new Resolvable<>(fulfillable, rejectable, nextPromise));

        if (_promiseState.isRejected()) {
            _promiseEventHandler.reject(_promiseState.getRejectedWith());
        } else if (_promiseState.isResolved()) {
            _promiseEventHandler.resolve(_promiseState.getResolvedWith());
        }
    }
}

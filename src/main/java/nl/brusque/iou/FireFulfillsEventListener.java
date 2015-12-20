package nl.brusque.iou;

class FireFulfillsEventListener<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> implements IEventListener<FireFulfillsEvent> {
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    public FireFulfillsEventListener(PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    public void process(FireFulfillsEvent event) {
        _promiseResolverEventHandler.resolve();
    }
}

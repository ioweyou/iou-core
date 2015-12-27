package nl.brusque.iou;

class FireFulfillsEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FireFulfillsEvent> {
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    public FireFulfillsEventListener(PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    public void process(FireFulfillsEvent event) {
        _promiseResolverEventHandler.fireFulfillables();
    }
}

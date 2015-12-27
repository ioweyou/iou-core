package nl.brusque.iou;

class FulfillEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FulfillEvent> {
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    public FulfillEventListener(PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(FulfillEvent event) {
        _promiseResolverEventHandler.fulfillWithValue(event.getValue());
    }
}

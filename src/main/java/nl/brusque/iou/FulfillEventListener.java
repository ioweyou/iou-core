package nl.brusque.iou;

class FulfillEventListener<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<FulfillEvent> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    public FulfillEventListener(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler) {
        _promiseState    = promiseState;
        _eventDispatcher = eventDispatcher;
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(FulfillEvent event) {
        if (!_promiseState.isPending()) {
            return;
        }

        if (event.getValue() instanceof AbstractPromise) {
            _promiseResolverEventHandler.resolvePromiseValue((AbstractPromise<TResult, TFulfillable, TRejectable>)event.getValue());

            return;
        }

        _promiseState.fulfill(event.getValue());
        _eventDispatcher.queue(new FireFulfillsEvent());
    }
}

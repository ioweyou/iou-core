package nl.brusque.iou;

class FulfillEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FulfillEvent> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    public FulfillEventListener(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
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
            _promiseResolverEventHandler.resolvePromiseValue((AbstractPromise)event.getValue());

            return;
        }

        _promiseState.fulfill(event.getValue());
        _eventDispatcher.queue(new FireFulfillsEvent());
    }
}

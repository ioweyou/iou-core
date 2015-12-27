package nl.brusque.iou;

class RejectEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<RejectEvent> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    public RejectEventListener(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
        _promiseState                = promiseState;
        _eventDispatcher             = eventDispatcher;
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(RejectEvent event) {
        if (!_promiseState.isPending()) {
            return;
        }

        if (event.getValue() instanceof AbstractPromise) {
            _promiseResolverEventHandler.resolvePromiseValue((AbstractPromise) event.getValue());

            return;
        }

        _promiseState.reject(event.getValue());
        _eventDispatcher.queue(new FireRejectsEvent());
    }
}

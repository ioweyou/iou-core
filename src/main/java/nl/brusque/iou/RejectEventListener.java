package nl.brusque.iou;

class RejectEventListener<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<RejectEvent> {
    private final PromiseStateHandler _promiseState;
    private final EventDispatcher _eventDispatcher;
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    public RejectEventListener(PromiseStateHandler promiseState, EventDispatcher eventDispatcher, PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler) {
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
            _promiseResolverEventHandler.resolvePromiseValue((AbstractPromise<TResult, TFulfillable, TRejectable>) event.getValue());

            return;
        }

        _promiseState.reject(event.getValue());
        _eventDispatcher.queue(new FireRejectsEvent());
    }
}

package nl.brusque.iou;

class RejectEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<RejectEvent> {
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    public RejectEventListener(PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(RejectEvent event) {
        _promiseResolverEventHandler.rejectWithValue(event.getValue());
    }
}

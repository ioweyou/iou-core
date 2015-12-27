package nl.brusque.iou;

class FireRejectsEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FireRejectsEvent> {
    private final PromiseResolverEventHandler<TResult> _promiseResolverEventHandler;

    public FireRejectsEventListener(PromiseResolverEventHandler<TResult> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(FireRejectsEvent event) {
        _promiseResolverEventHandler.reject();
    }
}

package nl.brusque.iou;

class FireRejectsEventListener<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<FireRejectsEvent> {
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    public FireRejectsEventListener(PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(FireRejectsEvent event) {
        _promiseResolverEventHandler.reject();
    }
}

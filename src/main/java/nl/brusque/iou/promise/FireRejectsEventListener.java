package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;
import nl.brusque.iou.promise.eventdispatcher.IEventListener;
import nl.brusque.iou.promise.eventdispatcher.events.FireRejectsEvent;

class FireRejectsEventListener<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<FireRejectsEvent> {
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    public FireRejectsEventListener(PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    @Override
    public void process(FireRejectsEvent event) {
        _promiseResolverEventHandler.reject();
    }
}

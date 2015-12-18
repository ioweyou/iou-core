package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;
import nl.brusque.iou.promise.eventdispatcher.IEventListener;
import nl.brusque.iou.promise.eventdispatcher.events.FireFulfillsEvent;

class FireFulfillsEventListener<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<FireFulfillsEvent> {
    private final PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> _promiseResolverEventHandler;

    public FireFulfillsEventListener(PromiseResolverEventHandler<TResult, TFulfillable, TRejectable> promiseResolverEventHandler) {
        _promiseResolverEventHandler = promiseResolverEventHandler;
    }

    public void process(FireFulfillsEvent event) {
        _promiseResolverEventHandler.resolve();
    }
}

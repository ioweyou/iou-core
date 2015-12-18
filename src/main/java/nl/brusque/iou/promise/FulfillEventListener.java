package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;
import nl.brusque.iou.promise.eventdispatcher.IEventListener;
import nl.brusque.iou.promise.eventdispatcher.events.FireFulfillsEvent;
import nl.brusque.iou.promise.eventdispatcher.events.FulfillEvent;

class FulfillEventListener<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<FulfillEvent> {
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

        if (event.getValue() instanceof IPromise) {
            _promiseResolverEventHandler.resolvePromiseValue((IPromise)event.getValue());

            return;
        }

        _promiseState.fulfill(event.getValue());
        _eventDispatcher.queue(new FireFulfillsEvent());
    }
}

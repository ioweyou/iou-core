package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;
import nl.brusque.iou.promise.eventdispatcher.IEventListener;
import nl.brusque.iou.promise.eventdispatcher.events.FireRejectsEvent;
import nl.brusque.iou.promise.eventdispatcher.events.RejectEvent;

class RejectEventListener<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> implements IEventListener<RejectEvent> {
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

        if (event.getValue() instanceof IPromise) {
            _promiseResolverEventHandler.resolvePromiseValue((IPromise)event.getValue());

            return;
        }

        _promiseState.reject(event.getValue());
        _eventDispatcher.queue(new FireRejectsEvent());
    }
}

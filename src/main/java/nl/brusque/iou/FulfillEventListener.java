package nl.brusque.iou;

final class FulfillEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FulfillEvent> {
    private final EventDispatcher _eventDispatcher;
    private final PromiseStateHandler _promiseState;

    public FulfillEventListener(EventDispatcher eventDispatcher, PromiseStateHandler promiseState) {
        _eventDispatcher             = eventDispatcher;
        _promiseState                = promiseState;
    }

    @Override
    public void process(FulfillEvent event) {
        fulfillWithValue(event.getValue());
    }

    synchronized void fulfillWithValue(Object value) {
        if (!_promiseState.isPending()) {
            return;
        }

        if (value instanceof AbstractPromise) {
            new PromiseValueResolver(_eventDispatcher, _promiseState).resolve((AbstractPromise)value);

            return;
        }

        _promiseState.fulfill(value);
        _eventDispatcher.queue(new FireFulfillsEvent());
    }
}

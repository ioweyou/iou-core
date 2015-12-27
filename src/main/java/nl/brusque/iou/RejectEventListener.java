package nl.brusque.iou;

class RejectEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<RejectEvent> {
    private final EventDispatcher _eventDispatcher;
    private final PromiseStateHandler _promiseState;

    public RejectEventListener(EventDispatcher eventDispatcher, PromiseStateHandler promiseState) {
        _eventDispatcher = eventDispatcher;
        _promiseState    = promiseState;
    }

    @Override
    public void process(RejectEvent event) {
        rejectWithValue(event.getValue());
    }

    void rejectWithValue(Object value) {
        if (!_promiseState.isPending()) {
            return;
        }

        if (value instanceof AbstractPromise) {
            new PromiseValueResolver(_eventDispatcher, _promiseState).resolve((AbstractPromise)value);

            return;
        }

        _promiseState.reject(value);
        _eventDispatcher.queue(new FireRejectsEvent());
    }
}

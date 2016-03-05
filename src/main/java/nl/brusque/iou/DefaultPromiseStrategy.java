package nl.brusque.iou;

class DefaultPromiseStrategy<TInput, TOutput> implements IPromiseStrategy<TInput, TOutput> {
    private final PromiseStateHandler _stateHandler;

    public DefaultPromiseStrategy(PromiseStateHandler stateHandler) {
        _stateHandler = stateHandler;
    }

    public <TAnythingInput, TAnythingOutput> void then(IThenCallable<TInput, TOutput> onFulfillable, IThenCallable<TAnythingInput, TAnythingOutput> onRejectable) {

    }

    public boolean isPending() {
        return _stateHandler.isPending();
    }

    public boolean isResolved() { return _stateHandler.isResolved(); }

    public boolean isRejected() {
        return _stateHandler.isRejected();
    }
}

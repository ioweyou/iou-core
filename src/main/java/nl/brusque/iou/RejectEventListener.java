package nl.brusque.iou;

final class RejectEventListener<TFulfill, TAnything> implements IEventListener<RejectEvent<TFulfill, TAnything>> {
    private final PromiseState<TFulfill> _stateManager;

    RejectEventListener(PromiseState<TFulfill> stateManager) {
        _stateManager = stateManager;
    }

    @Override
    public void process(RejectEvent<TFulfill, TAnything> event) throws Exception {
        _stateManager.reject(event.getValue());
    }
}

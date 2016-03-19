package nl.brusque.iou;

final class ResolveEventListener<TFulfill, TAnything> implements IEventListener<ResolveEvent<TFulfill, TAnything>> {
    private final PromiseState<TFulfill> _stateManager;

    ResolveEventListener(PromiseState<TFulfill> stateManager) {
        _stateManager = stateManager;
    }

    @Override
    public void process(ResolveEvent<TFulfill, TAnything> event) throws Exception {
        PromiseResolver.resolve(
                _stateManager,
                event.getValue());
    }
}

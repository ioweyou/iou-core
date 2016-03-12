package nl.brusque.iou;

final class ResolveEventListener<TFulfill> implements IEventListener<ResolveEvent<TFulfill>> {
    private final PromiseResolver<TFulfill> _promiseResolver;
    private final AbstractPromise<TFulfill> _promise;

    public ResolveEventListener(AbstractPromise<TFulfill> promise, PromiseState<TFulfill> promiseState) {
        _promiseResolver = new PromiseResolver<>(promiseState);

        _promise = promise;
    }

    @Override
    public void process(ResolveEvent<TFulfill> event) {
        _promiseResolver.resolve(_promise, event.getValue().getValue());
    }
}

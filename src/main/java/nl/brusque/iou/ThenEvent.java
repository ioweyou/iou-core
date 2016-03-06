package nl.brusque.iou;

final class ThenEvent<TFulfill, TOutput> extends DefaultEvent<ThenEventValue<TFulfill, TOutput>> {
    private final ThenEventValue<TFulfill, TOutput> _thenEventValue;

    public ThenEvent(ThenEventValue<TFulfill, TOutput> value) {
        super(value);

        _thenEventValue = value;
    }

    public IThenCallable<TFulfill, TOutput> getFulfillable() {
        return _thenEventValue.getFulfillable();
    }

    public IThenCallable<Object, TOutput> getRejectable() {
        return _thenEventValue.getRejectable();
    }

    public AbstractPromise<TOutput> getPromise() {
        return _thenEventValue.getPromise();
    }

}

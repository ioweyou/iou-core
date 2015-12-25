package nl.brusque.iou;

class ThenEvent<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> extends AbstractEvent {
    private final ThenEventValue<TResult, TFulfillable, TRejectable> _thenEventValue;

    public ThenEvent(ThenEventValue<TResult, TFulfillable, TRejectable> value) {
        super(value); // FIXME Thenable

        _thenEventValue = value;
    }

    public Object getFulfillable() {
        return _thenEventValue.getFulfillable();
    }

    public Object getRejectable() {
        return _thenEventValue.getRejectable();
    }

    public TResult getPromise() {
        return _thenEventValue.getPromise();
    }

}

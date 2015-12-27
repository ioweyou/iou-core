package nl.brusque.iou;

class ThenEvent<TResult extends AbstractPromise<TResult>> extends AbstractEvent {
    private final ThenEventValue<TResult> _thenEventValue;

    public ThenEvent(ThenEventValue<TResult> value) {
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

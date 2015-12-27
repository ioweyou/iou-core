package nl.brusque.iou;

final class ThenEvent<TInput> extends DefaultEvent {
    private final ThenEventValue<TInput> _thenEventValue;

    public ThenEvent(ThenEventValue<TInput> value) {
        super(value); // FIXME Thenable

        _thenEventValue = value;
    }

    public Object getFulfillable() {
        return _thenEventValue.getFulfillable();
    }

    public Object getRejectable() {
        return _thenEventValue.getRejectable();
    }

    public AbstractPromise<TInput> getPromise() {
        return _thenEventValue.getPromise();
    }

}

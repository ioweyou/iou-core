package nl.brusque.iou;

class DefaultEvent<TValue> {
    private final TValue _value;

    public DefaultEvent(TValue value) {
        _value = value;
    }

    public final TValue getValue() {
        return _value;
    }
}

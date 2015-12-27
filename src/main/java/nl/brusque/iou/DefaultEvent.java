package nl.brusque.iou;

class DefaultEvent {
    private final Object _value;

    public DefaultEvent(Object value) {
        _value = value;
    }

    public final Object getValue() {
        return _value;
    }
}

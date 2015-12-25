package nl.brusque.iou;

abstract class AbstractEvent {
    private final Object _value;

    public AbstractEvent(Object value) {
        _value = value;
    }

    public final Object getValue() {
        return _value;
    }
}

package nl.brusque.iou;

class FulfillEvent implements IEvent {
    private final Object _o;

    public FulfillEvent(Object o) {
        _o = o;
    }

    public Object getValue() {
        return _o;
    }
}

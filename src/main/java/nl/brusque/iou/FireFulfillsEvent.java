package nl.brusque.iou;

class FireFulfillsEvent extends AbstractEvent {
    public FireFulfillsEvent() {
        this(null);
    }

    private FireFulfillsEvent(Object value) {
        super(value);
    }
}

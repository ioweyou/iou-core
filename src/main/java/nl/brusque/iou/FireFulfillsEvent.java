package nl.brusque.iou;

final class FireFulfillsEvent extends DefaultEvent {
    public FireFulfillsEvent() {
        this(null);
    }

    private FireFulfillsEvent(Object value) {
        super(value);
    }
}

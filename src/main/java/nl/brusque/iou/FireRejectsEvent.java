package nl.brusque.iou;

class FireRejectsEvent extends AbstractEvent {

    public FireRejectsEvent() {
        this(null);
    }

    private FireRejectsEvent(Object value) {
        super(value);
    }
}

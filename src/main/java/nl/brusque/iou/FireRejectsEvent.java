package nl.brusque.iou;

final class FireRejectsEvent extends DefaultEvent {

    public FireRejectsEvent() {
        this(null);
    }

    private FireRejectsEvent(Object value) {
        super(value);
    }
}

package nl.brusque.iou;

final class RejectEventValue<TInput, TAnything> {
    private final TAnything _value;

    public RejectEventValue(TAnything value) {
        _value   = value;
    }

    public TAnything getValue() {
        return _value;
    }
}

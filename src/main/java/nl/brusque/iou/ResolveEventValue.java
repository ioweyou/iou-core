package nl.brusque.iou;

final class ResolveEventValue<TAnything> {
    private final TAnything _value;

    public ResolveEventValue(TAnything value) {
        _value   = value;
    }

    public TAnything getValue() {
        return _value;
    }
}

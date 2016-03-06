package nl.brusque.iou;


public final class RejectReason<TTInput> {
    private final TTInput _value;
    private final String _reason;

    public RejectReason(TTInput value) {
        this(value, "");
    }

    public RejectReason(TTInput value, String reason) {
        _value  = value;
        _reason = reason;
    }

    public TTInput getValue() { return _value; }
    public String getReason() { return _reason; }
}
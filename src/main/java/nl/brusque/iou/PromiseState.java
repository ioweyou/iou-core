package nl.brusque.iou;

final class PromiseState<TFulfill, TOutput> {
    private final Fulfiller<TFulfill>_fulfiller;
    private final Rejector<TFulfill> _rejector;
    private State _state = State.PENDING;

    private Object _rejectionReason;
    private TFulfill _resolvedWith;

    public PromiseState(Fulfiller<TFulfill> fulfiller, Rejector<TFulfill> rejector) {
        _fulfiller = fulfiller;
        _rejector  = rejector;
    }

    public Object getRejectedWith() {
        return _rejectionReason;
    }

    private enum State {
        RESOLVED,
        PENDING,
        REJECTED
    }

    public boolean isPending() {
        return _state == State.PENDING;
    }

    public boolean isResolved() {
        return _state == State.RESOLVED;
    }

    public boolean isRejected() {
        return _state == State.REJECTED;
    }

    public void fulfill(TFulfill o) {
        if (isPending()) {
            _state = State.RESOLVED;

            _resolvedWith = o;
        }

        _fulfiller.fulfill(_resolvedWith);
    }

    public void reject(Object reason) {
        if (isPending()) {
            _state = State.REJECTED;

            _rejectionReason = reason;
        }

        _rejector.reject(_rejectionReason);
    }

    public TFulfill getResolvedWith() {
        return _resolvedWith;
    }
}

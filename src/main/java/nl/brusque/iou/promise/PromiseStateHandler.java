package nl.brusque.iou.promise;

class PromiseStateHandler {
    private State _state = State.PENDING;

    private Object _rejectionReason;
    private Object _resolvedWith;

    public Object RejectedWith() {
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

    public void fulfill(Object o) {
        _state = State.RESOLVED;

        _resolvedWith = o;
    }

    public void reject(Object reason) {
        _state = State.REJECTED;

        _rejectionReason = reason;
    }

    public Object getResolvedWith() {
        return _resolvedWith;
    }
}

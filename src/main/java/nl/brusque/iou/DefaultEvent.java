package nl.brusque.iou;

class DefaultEvent<TFulfill, TValue> {
    private final TValue _value;
    private final AbstractPromise<TFulfill> _promise;

    DefaultEvent(AbstractPromise<TFulfill> promise, TValue value) {
        _promise = promise;

        _value   = value;
    }

    final TValue getValue() {
        return _value;
    }
    final AbstractPromise<TFulfill> getPromise() {
        return _promise;
    }
}

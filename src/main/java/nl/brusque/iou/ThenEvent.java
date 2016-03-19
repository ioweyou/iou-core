package nl.brusque.iou;

final class ThenEvent<TFulfill, TOutput> extends DefaultEvent<TFulfill, ThenEventValue<TFulfill, TOutput>> {
    ThenEvent(AbstractPromise<TFulfill> promise, ThenEventValue<TFulfill, TOutput> value) {
        super(promise, value);
    }

    IThenCallable<TFulfill, TOutput> getFulfillable() {
        return getValue().getFulfillable();
    }

    IThenCallable<Object, TOutput> getRejectable() {
        return getValue().getRejectable();
    }

    AbstractPromise<TOutput> getNextPromise() {
        return getValue().getPromise();
    }

}

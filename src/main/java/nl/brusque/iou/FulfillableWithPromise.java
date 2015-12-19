package nl.brusque.iou;

class FulfillableWithPromise<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> {
    private final AbstractPromise<TResult, TFulfillable, TRejectable> _promise;
    private final TFulfillable _fulfillable;

    public FulfillableWithPromise(TFulfillable fulfillable, AbstractPromise<TResult, TFulfillable, TRejectable> promise) {
        _promise     = promise;
        _fulfillable = fulfillable;
    }

    public AbstractPromise<TResult, TFulfillable, TRejectable> getPromise() {
        return _promise;
    }

    public TFulfillable getFulfillable() {
        return _fulfillable;
    }
}

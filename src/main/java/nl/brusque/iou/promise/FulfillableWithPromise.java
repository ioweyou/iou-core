package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;

class FulfillableWithPromise<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> {
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

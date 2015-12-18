package nl.brusque.iou.promise;

import nl.brusque.iou.IFulfillable;
import nl.brusque.iou.IPromise;
import nl.brusque.iou.IRejectable;

class RejectableWithPromise<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> {
    private final AbstractPromise<TResult, TFulfillable, TRejectable> _promise;
    private final TRejectable rejectable;

    public RejectableWithPromise(TRejectable rejectable, AbstractPromise<TResult, TFulfillable, TRejectable> promise) {
        _promise        = promise;
        this.rejectable = rejectable;
    }

    public AbstractPromise<TResult, TFulfillable, TRejectable> getPromise() {
        return _promise;
    }

    public TRejectable getRejectable() {
        return rejectable;
    }
}
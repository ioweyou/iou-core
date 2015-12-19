package nl.brusque.iou;

class RejectableWithPromise<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IFulfillable, TRejectable extends IRejectable> {
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
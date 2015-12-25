package nl.brusque.iou;

public class DefaultPromiseResolver<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> implements IPromiseResolver<TResult, TFulfillable, TRejectable> {

    public TResult apply(final TFulfillable onFulfill, final TRejectable onReject, final TResult o) {
        return o.then(onFulfill, onReject);
    }
}

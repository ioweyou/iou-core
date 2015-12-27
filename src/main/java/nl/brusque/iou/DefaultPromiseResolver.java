package nl.brusque.iou;

public class DefaultPromiseResolver<TResult extends AbstractPromise<TResult>> implements IPromiseResolver<TResult> {

    public TResult apply(final IThenCallable onFulfill, final IThenCallable onReject, final TResult o) {
        return o.then(onFulfill, onReject);
    }
}

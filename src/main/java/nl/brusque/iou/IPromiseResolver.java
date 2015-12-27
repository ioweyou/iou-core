package nl.brusque.iou;

interface IPromiseResolver<TResult> {
    TResult apply(final IThenCallable onFulfill, final IThenCallable onReject, final TResult o);
}

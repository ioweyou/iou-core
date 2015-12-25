package nl.brusque.iou;

interface IPromiseResolver<TResult, TFulfillable, TRejectable> {
    TResult apply(final TFulfillable onFulfill, final TRejectable onReject, final TResult o);
}

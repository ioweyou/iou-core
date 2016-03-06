package nl.brusque.iou;

public interface IThenable<TFulfill> {
    <TAnythingOutput> AbstractPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled);
    <TAnythingOutput> AbstractPromise<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected);
}

package nl.brusque.iou;

public interface IThenable<TFulfill> {
    <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled);
    <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected);
}

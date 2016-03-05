package nl.brusque.iou;

public interface IThenable<TInput> {
    <TOutput> IThenable<TOutput> then();
    <TOutput> IThenable<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled);
    <TOutput, TAnythingInput> IThenable<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled, IThenCallable<TAnythingInput, TOutput> onRejected);
}

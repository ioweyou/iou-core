package nl.brusque.iou;

public interface IThenable<TInput> {
    <TOutput> IThenable<TOutput> then();
    <TOutput> IThenable<TOutput> then(TInput onFulfilled);
    <TOutput> IThenable<TOutput> then(TInput onFulfilled, TInput onRejected);
    <TOutput, TAnything> IThenable<TOutput> then(TAnything onFulfilled, IThenCallable<TInput, TOutput> onRejected);
    <TOutput, TAnything> IThenable<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled, TAnything onRejected);
    <TOutput> IThenable<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled);
    <TOutput> IThenable<TOutput> then(IThenCallable<TInput, TOutput> onFulfilled, IThenCallable<TInput, TOutput> onRejected);
}

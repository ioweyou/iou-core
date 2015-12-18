package nl.brusque.iou;

public interface IThenable<TResult extends IThenable> {
    TResult then();
    TResult then(Object onFulfilled);
    TResult then(Object onFulfilled, Object onRejected);
}

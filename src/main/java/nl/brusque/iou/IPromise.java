package nl.brusque.iou;

public interface IPromise<TResult extends IPromise> extends IThenable<TResult> {
    TResult create();
    TResult resolve(Object run);
    TResult reject(Object o);
}

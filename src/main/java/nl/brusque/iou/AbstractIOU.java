package nl.brusque.iou;

public abstract class AbstractIOU<TResult extends AbstractPromise<TResult, TFulfillable, TRejectable>, TFulfillable extends IThenCallable, TRejectable extends IThenCallable> {
    public abstract TResult getPromise();

    public AbstractPromise<TResult, TFulfillable, TRejectable> resolve(Object o) {
        return getPromise().resolve(o);
    }

    public AbstractPromise<TResult, TFulfillable, TRejectable> reject(Object o) {
        return getPromise().reject(o.toString());
    }
}

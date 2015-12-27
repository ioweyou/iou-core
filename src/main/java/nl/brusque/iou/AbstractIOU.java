package nl.brusque.iou;

public abstract class AbstractIOU<TResult extends AbstractPromise<TResult>> {
    public abstract TResult getPromise();

    public AbstractPromise<TResult> resolve(Object o) {
        return getPromise().resolve(o);
    }

    public AbstractPromise<TResult> reject(Object o) {
        return getPromise().reject(o.toString());
    }
}

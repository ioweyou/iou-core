package nl.brusque.iou;

public abstract class AbstractIOU<TInput> {
    public abstract AbstractPromise<TInput> getPromise();

    public AbstractPromise<TInput> resolve(TInput o) {
        return getPromise().resolve(o);
    }

    public AbstractPromise<TInput> reject(TInput o) {
        return getPromise().reject(o);
    }
}

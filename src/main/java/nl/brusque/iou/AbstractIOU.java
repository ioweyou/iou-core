package nl.brusque.iou;

public abstract class AbstractIOU<TPromise extends IPromise> {
    public abstract TPromise getPromise();

    public IPromise resolve(Object o) {
        return getPromise().resolve(o);
    }

    public IPromise reject(Object o) {
        return getPromise().reject(o.toString());
    }
}

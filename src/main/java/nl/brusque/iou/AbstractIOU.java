package nl.brusque.iou;

public abstract class AbstractIOU<TFulfill> {
    public abstract AbstractPromise<TFulfill>  getPromise();

    public void resolve(TFulfill o) {
        getPromise().resolve(o);
    }

    public void reject(Object o) {
        getPromise().reject(o);
    }
}

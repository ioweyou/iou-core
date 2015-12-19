package nl.brusque.iou;

public class DefaultRejector<TRejectable extends IRejectable> {
    public Object reject(final TRejectable rejectable, final Object o) throws Exception {
        return rejectable.reject(o);
    }
}

package nl.brusque.iou;

public class DefaultThenCallable<TFulfillable extends IThenCallable> {
    public Object apply(final TFulfillable fulfillable, final Object o) throws Exception {
        return fulfillable.apply(o);
    }
}

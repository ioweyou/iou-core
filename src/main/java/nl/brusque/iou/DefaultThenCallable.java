package nl.brusque.iou;

public class DefaultThenCallable<TFulfillable extends IThenCallable> {
    public Object call(final TFulfillable fulfillable, final Object o) throws Exception {
        return fulfillable.call(o);
    }
}

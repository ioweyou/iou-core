package nl.brusque.iou;

public class DefaultFulfiller<TFulfillable extends IFulfillable> {
    public Object fulfill(final TFulfillable fulfillable, final Object o) throws Exception {
        return fulfillable.fulfill(o);
    }
}

package nl.brusque.iou.helper;

import nl.brusque.iou.IFulfillable;

public class FulfillableSpy extends Spy implements IFulfillable {
    @Override
    public Object fulfill(Object o) throws Exception {
        return call(o);
    }

}
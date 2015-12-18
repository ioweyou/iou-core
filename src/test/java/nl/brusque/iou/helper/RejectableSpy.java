package nl.brusque.iou.helper;

import nl.brusque.iou.IRejectable;

public class RejectableSpy extends Spy implements IRejectable {
    @Override
    public Object reject(Object o) throws Exception {
        return call(o);
    }
}
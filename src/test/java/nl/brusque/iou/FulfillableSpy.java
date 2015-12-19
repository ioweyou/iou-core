package nl.brusque.iou;

class FulfillableSpy extends Spy implements IFulfillable {
    @Override
    public Object fulfill(Object o) throws Exception {
        return call(o);
    }

}
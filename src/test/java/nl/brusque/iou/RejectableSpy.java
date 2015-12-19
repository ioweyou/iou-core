package nl.brusque.iou;

class RejectableSpy extends Spy implements IRejectable {
    @Override
    public Object reject(Object o) throws Exception {
        return call(o);
    }
}
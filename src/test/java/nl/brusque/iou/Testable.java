package nl.brusque.iou;

abstract class Testable implements Runnable {
    private AbstractPromise _p;

    protected void setPromise(AbstractPromise p) {
        _p = p;
    }

    protected AbstractPromise getPromise() {
        return _p;
    }
}
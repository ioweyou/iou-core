package nl.brusque.iou;

abstract class Testable<TInput> implements Runnable {
    private AbstractPromise<TInput> _p;

    protected void setPromise(AbstractPromise<TInput> p) {
        _p = p;
    }

    protected AbstractPromise<TInput> getPromise() {
        return _p;
    }
}
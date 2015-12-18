package nl.brusque.iou.helper;

import nl.brusque.iou.IPromise;

public abstract class Testable implements Runnable {
    private IPromise _p;

    protected void setPromise(IPromise p) {
        _p = p;
    }

    protected IPromise getPromise() {
        return _p;
    }
}
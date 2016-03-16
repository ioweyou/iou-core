package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;


public class TestableParameters {
    private final MiniMochaSpecificationRunnable _doneHandler;
    private final AbstractPromise _p;

    TestableParameters(AbstractPromise p, MiniMochaSpecificationRunnable doneHandler) {
        _p           = p;
        _doneHandler = doneHandler;
    }

    final AbstractPromise getPromise() { return _p; }

    final void done() {
        _doneHandler.done();
    }

    final void delayedDone(final long milliseconds) {
        _doneHandler.delayedDone(milliseconds);
    }

    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _doneHandler.delayedCall(runnable, milliseconds);
    }

}

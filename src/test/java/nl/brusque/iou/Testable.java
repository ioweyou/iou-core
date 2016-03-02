package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class Testable<TInput> implements Runnable {
    private MiniMochaSpecificationRunnable _doneHandler;
    private AbstractPromise<TInput> _p;

    protected void setPromise(AbstractPromise<TInput> p) {
        _p = p;
    }

    protected AbstractPromise<TInput> getPromise() {
        return _p;
    }

    protected void setDoneHandler(MiniMochaSpecificationRunnable doneHandler) {
        _doneHandler = doneHandler;
    }

    class CallbackAggregator {
        private int _soFar = 0;
        private final int _times;
        private final Runnable _runnable;

        public CallbackAggregator(int times, Runnable runnable) {
            _times    = times;

            _runnable = runnable;
        }

        public void done() {
            _soFar++;

            if (_soFar == _times) {
                Testable.this.done();
            }
        }
    }

    final void done() {
        _doneHandler.done();
    }

    private final ExecutorService _delayedCallExecutor = Executors.newSingleThreadExecutor();

    public final void delayedDone(final long milliseconds) {
        _doneHandler.delayedDone(milliseconds);
    }

    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _doneHandler.delayedCall(runnable, milliseconds);
    }
}
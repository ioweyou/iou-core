package nl.brusque.iou.minimocha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class MiniMochaSpecificationRunnable extends MiniMochaNode implements Runnable {

    private List<IMiniMochaDoneListener> _doneListeners = new ArrayList<>();

    public final void done() {
        for (IMiniMochaDoneListener listener : _doneListeners) {
            listener.done();
        }
    }




    public final void delayedDone(final long milliseconds) {
        delayedCall(new Runnable() {
            @Override
            public void run() {
                done();
            }
        }, milliseconds);
    }

    public void addDoneListener(IMiniMochaDoneListener doneListener) {
        _doneListeners.add(doneListener);
    }
}
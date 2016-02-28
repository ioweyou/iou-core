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


    private final ExecutorService _delayedCallExecutor = Executors.newSingleThreadExecutor();

    public final void delayedDone(final long milliseconds) {
        delayedCall(new Runnable() {
            @Override
            public void run() {
                done();
            }
        }, milliseconds);
    }

    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _delayedCallExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(milliseconds);

                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void addDoneListener(IMiniMochaDoneListener doneListener) {
        _doneListeners.add(doneListener);
    }
}
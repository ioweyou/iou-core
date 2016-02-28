package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaRunnableNode;

import java.util.Timer;
import java.util.TimerTask;

import static nl.brusque.iou.Util.*;

public abstract class IOUMiniMochaRunnableNode extends MiniMochaRunnableNode {
    public <TInput> void testFulfilled(final TInput value, final Testable<TInput> test) {
        specify("already-fulfilled", new Runnable() {
            @Override
            public void run() {
                test.setPromise(resolved(value));

                test.run();
            }
        });

        specify("immediately-fulfilled", new Runnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-fulfilled", new Runnable() {
            @Override
            public void run() {
                final AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        d.resolve(value);
                    }
                }, 50);
            }
        });
    }
    public <TInput> void testRejected(final TInput value, final Testable<TInput> test) {
        specify("already-rejected", new Runnable() {
            @Override
            public void run() {
                test.setPromise(rejected(value));

                test.run();
            }
        });

        specify("immediately-rejected", new Runnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-rejected", new Runnable() {
            @Override
            public void run() {
                final AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        d.reject(value);
                    }
                }, 50);

            }
        });
    }
}
package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaRunnableNode;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;

import java.util.Timer;
import java.util.TimerTask;

import static nl.brusque.iou.Util.*;

public abstract class IOUMiniMochaRunnableNode extends MiniMochaRunnableNode {
    public <TInput> void testFulfilled(final TInput value, final Testable<TInput> test) {
        specify("already-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                //test.addListener(MiniMochaSpecificationRunnable.this);

                test.setPromise(resolved(value));

                test.run();
            }
        });

        specify("immediately-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-fulfilled", new MiniMochaSpecificationRunnable() {
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
        specify("already-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                test.setPromise(rejected(value));

                test.run();
            }
        });

        specify("immediately-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-rejected", new MiniMochaSpecificationRunnable() {
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
package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaRunnableNode;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;

import java.util.Timer;
import java.util.TimerTask;

import static nl.brusque.iou.Util.*;

public abstract class IOUMiniMochaRunnableNode extends MiniMochaRunnableNode {


    abstract class PromiseFactory<TInput> {
        abstract AbstractPromise<TInput> create();
    }

    protected <TAnything, TAnything2> void testPromiseResolution(final PromiseFactory<TAnything> xFactory, final Testable<TAnything2> promiseTest) {
        final String dummy     = "DUMMY";

        specify("via return from a fulfilled promise", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                final AbstractPromise<String> resolvedPromise = resolved(dummy);

                final AbstractPromise promise = resolvedPromise.then(new TestThenCallable<String, AbstractPromise>() {
                    @Override
                    public AbstractPromise apply(String o) throws Exception {
                        return xFactory.create();
                    }
                });

                promiseTest.setDoneHandler(this);
                promiseTest.setPromise(promise);

                promiseTest.run();
            }
        });

        specify("via return from a rejected promise", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                final AbstractPromise<String> rejectedPromise = rejected(dummy);

                final AbstractPromise promise = rejectedPromise.then(null, new TestThenCallable<String, AbstractPromise>() {
                    @Override
                    public AbstractPromise apply(String o) throws Exception {
                        return xFactory.create();
                    }
                });

                promiseTest.setDoneHandler(this);
                promiseTest.setPromise(promise);

                promiseTest.run();
            }
        });
    }

    public <TInput> void testFulfilled(final TInput value, final Testable<TInput> test) {
        specify("already-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                test.setDoneHandler(this);

                test.setPromise(resolved(value));

                test.run();
            }
        });

        specify("immediately-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setDoneHandler(this);
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                final AbstractIOU<TInput> d = deferred();
                test.setDoneHandler(this);
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
                test.setDoneHandler(this);

                test.setPromise(rejected(value));

                test.run();
            }
        });

        specify("immediately-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setDoneHandler(this);

                test.setPromise(d.getPromise());
                test.run();
                d.reject(value);
            }
        });

        specify("eventually-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                final AbstractIOU<TInput> d = deferred();

                test.setDoneHandler(this);
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
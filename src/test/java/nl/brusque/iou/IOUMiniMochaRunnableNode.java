package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaRunnableNode;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;

import java.util.Timer;
import java.util.TimerTask;

import static nl.brusque.iou.Util.*;

public abstract class IOUMiniMochaRunnableNode extends MiniMochaRunnableNode {

    abstract class AnythingFactory<TAnything> {
        abstract TAnything create();
    }


    abstract class ThenableFactory<TInput> extends AnythingFactory<IThenable<TInput>>{

    }

    abstract class PromiseFactory<TInput> extends ThenableFactory<TInput>{

    }

    void handleNonSensicalTest(String stringRepresentation) {
        specify(String.format("Testing %s does not make sense in Java", stringRepresentation), new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                delayedCall(new Runnable() {
                    @Override
                    public void run() {
                        done();
                    }
                }, 0);
            }
        });
    }

    protected <TFulfill> void testPromiseResolution(final ThenableFactory<TFulfill> xFactory, final Testable<TFulfill> promiseTest) {
        final String dummy     = "DUMMY";

        specify("via return from a fulfilled promise", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                final AbstractPromise<String> resolvedPromise = resolved(dummy);

                final AbstractPromise promise = resolvedPromise.then(new TestThenCallable<String, IThenable>() {
                    @Override
                    public IThenable apply(String o) throws Exception {
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

                final AbstractPromise promise = rejectedPromise.then(null, new TestThenCallable<Object, IThenable>() {
                    @Override
                    public IThenable apply(Object o) throws Exception {
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

                final AbstractPromise<TInput> d = resolved(value);
                test.setPromise(d);

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

                final AbstractPromise<TInput> d = rejected(value);
                test.setPromise(d);

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
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
                //done();
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
                        allowMainThreadToFinish();
                        return xFactory.create();
                    }
                });

                allowMainThreadToFinish();

                promiseTest.run(new TestableParameters(promise, this));
            }
        });

        specify("via return from a rejected promise", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                final AbstractPromise<String> rejectedPromise = rejected(dummy);

                allowMainThreadToFinish();
                final AbstractPromise promise = rejectedPromise.then(null, new TestThenCallable<Object, IThenable>() {
                    @Override
                    public IThenable apply(Object o) throws Exception {
                        allowMainThreadToFinish();
                        return xFactory.create();
                    }
                });

                allowMainThreadToFinish();

                promiseTest.run(new TestableParameters(promise, this));
            }
        });
    }










































    <TInput> void testFulfilled(final TInput value, final Testable<TInput> test) {
        specify("already-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                allowMainThreadToFinish();

                final AbstractPromise<TInput> d = resolved(value);

                test.run(new TestableParameters(d, this));
            }
        });

        specify("immediately-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                allowMainThreadToFinish();

                AbstractIOU<TInput> d = deferred();
                test.run(new TestableParameters(d.getPromise(), this));
                d.resolve(value);
            }
        });

        specify("eventually-fulfilled", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                allowMainThreadToFinish();
                final AbstractIOU<TInput> d = deferred();
                test.run(new TestableParameters(d.getPromise(), this));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        d.resolve(value);
                    }
                }, 50);
            }
        });
    }

    <TInput> void testRejected(final TInput value, final Testable<TInput> test) {
        specify("already-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                allowMainThreadToFinish();

                final AbstractPromise<TInput> d = rejected(value);

                test.run(new TestableParameters(d, this));
            }
        });

        specify("immediately-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                allowMainThreadToFinish();
                AbstractIOU<TInput> d = deferred();
                test.run(new TestableParameters(d.getPromise(), this));
                d.reject(value);
            }
        });

        specify("eventually-rejected", new MiniMochaSpecificationRunnable() {
            @Override
            public void run() {
                allowMainThreadToFinish();
                final AbstractIOU<TInput> d = deferred();

                test.run(new TestableParameters(d.getPromise(), this));

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
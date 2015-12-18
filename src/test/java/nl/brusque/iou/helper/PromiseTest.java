package nl.brusque.iou.helper;

import nl.brusque.iou.*;
import nl.brusque.iou.promise.AbstractPromise;

import java.util.Timer;
import java.util.TimerTask;

public class PromiseTest {
    private class TestPromise extends AbstractPromise<IPromise, TestFulfillable, TestRejectable> {
        public TestPromise() {
            super(TestFulfillable.class, TestRejectable.class);
        }

        @Override
        public AbstractPromise<IPromise, TestFulfillable, TestRejectable> create() {
            return new TestPromise();
        }
    }

    private class TestIOU extends AbstractIOU<TestPromise> {
        private final TestPromise _promise = new TestPromise();

        @Override
        public TestPromise getPromise() {
            return _promise;
        }
    }

    public AbstractIOU deferred() {
        return new TestIOU();
    }

    public IPromise resolved() {
        return resolved(null);
    }

    public IPromise resolved(Object o) {
        return deferred().getPromise().resolve(o);
    }

    public IPromise rejected() {
        return rejected("");
    }

    public IPromise rejected(Object o) {
        return deferred().getPromise().reject(o);
    }

    public void describe(String description, Runnable runnable) {
        Log.i(description);
        runnable.run();
    }

    public void specify(String description, Runnable test) {
        Log.i(description);
        test.run();
    }

    public void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testFulfilled(final Object value, final Testable test) {
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
                AbstractIOU d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-fulfilled", new Runnable() {
            @Override
            public void run() {
                final AbstractIOU d = deferred();
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
    public void testRejected(final Object value, final Testable test) {
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
                AbstractIOU d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-rejected", new Runnable() {
            @Override
            public void run() {
                final AbstractIOU d = deferred();
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

    public FulfillableSpy fulfillableStub() {
        return new FulfillableSpy();
    }

    public RejectableSpy rejectableStub() {
        return new RejectableSpy();
    }
}
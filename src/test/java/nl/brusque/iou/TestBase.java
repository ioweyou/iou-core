package nl.brusque.iou;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.Mockito.spy;

class TestBase {
    private static final Logger logger = LogManager.getLogger(TestBase.class);


    private class TestPromise extends AbstractPromise<TestPromise> {
        protected TestPromise(IThenCaller fulfiller, IThenCaller rejector) {
            super(fulfiller, rejector);
        }

        @Override
        public TestPromise create() {
            return new TestPromise(new TestGenericThenCaller(), new TestGenericThenCaller());
        }
    }

    private class TestIOU extends AbstractIOU<TestPromise> {
        private final TestPromise _promise = new TestPromise(new TestGenericThenCaller(), new TestGenericThenCaller());

        @Override
        public TestPromise getPromise() {
            return _promise;
        }
    }

    public AbstractIOU deferred() {
        return new TestIOU();
    }

    public AbstractPromise resolved() {
        return resolved(null);
    }

    public AbstractPromise resolved(Object o) {
        return deferred().resolve(o);
    }

    public AbstractPromise rejected() {
        return rejected("");
    }

    public AbstractPromise rejected(Object o) {
        return deferred().reject(o);
    }

    public void describe(String description, Runnable runnable) {
        logger.info(description);
        runnable.run();
    }

    public void specify(String description, Runnable test) {
        logger.info(description);
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

    public IThenCallable fulfillableStub() {
        return spy(TestThenCallable.class);
    }

    public IThenCallable rejectableStub() {
        return spy(TestThenCallable.class);
    }
}
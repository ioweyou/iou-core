package nl.brusque.iou;

import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.Mockito.spy;

class TestBase {
    private class TestPromise extends AbstractPromise<TestPromise, TestFulfillable, TestRejectable> {
        public TestPromise() {
            super(TestFulfillable.class, TestRejectable.class);
        }

        @Override
        public TestPromise create() {
            return new TestPromise();
        }
    }

    private class TestIOU extends AbstractIOU<TestPromise, TestFulfillable, TestRejectable> {
        private final TestPromise _promise = new TestPromise();

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

    public IFulfillable fulfillableStub() {
        return spy(TestFulfillable.class);
    }

    public IRejectable rejectableStub() {
        return spy(TestRejectable.class);
    }
}
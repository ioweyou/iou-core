package nl.brusque.iou;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

import static org.mockito.Mockito.spy;

class TestUtils {
    private static final Logger logger = LogManager.getLogger(TestUtils.class);

    public static <TInput> AbstractIOU<TInput> deferred() {
        return new TestTypedIOU<>();
    }

    public static <TOutput> AbstractPromise<TOutput> resolved() {
        return resolved(null);
    }

    public static <TInput> AbstractPromise<TInput> resolved(TInput o) {
        AbstractIOU<TInput> d = deferred();

        return d.resolve(o);
    }

    public static AbstractPromise<String> rejected() {
        return rejected("");
    }

    public static <TInput> AbstractPromise<TInput> rejected(TInput o) {
        AbstractIOU<TInput> d = deferred();

        return d.reject(o);
    }

    public static void describe(String description, Runnable runnable) {
        logger.info(description);
        runnable.run();
    }

    public static void specify(String description, Runnable test) {
        logger.info(description);
        test.run();
    }

    public static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <TInput> void testFulfilled(final TInput value, final Testable<TInput> test) {
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
    public static <TInput> void testRejected(final TInput value, final Testable<TInput> test) {
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

    public static class TestMockableCallable<TInput, TOutput> extends TestThenCallable<TInput, TOutput> {

        @Override
        public TOutput apply(TInput o) throws Exception {
            return null;
        }
    }

    public static <TInput, TOutput> IThenCallable<TInput, TOutput> fulfillableStub() {
        TestMockableCallable<TInput, TOutput> callable = new TestMockableCallable<>();

        return spy(callable);
    }

    public static <TInput, TOutput> IThenCallable<TInput, TOutput> rejectableStub() {
        TestMockableCallable<TInput, TOutput> callable = new TestMockableCallable<>();

        return spy(callable);
    }
}
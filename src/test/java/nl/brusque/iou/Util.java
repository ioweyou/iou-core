package nl.brusque.iou;

import static org.mockito.Mockito.spy;

public class Util {
    public static <TInput> AbstractIOU<TInput> deferred() {
        return new TestTypedIOU<>();
    }

    static <TInput> AbstractPromise<TInput> resolved() {
        return resolved(null);
    }

    static <TInput> AbstractPromise<TInput> resolved(TInput o) {
        AbstractIOU<TInput> d = deferred();

        d.resolve(o);
        allowMainThreadToFinish();

        return d.getPromise();
    }

    public static AbstractPromise<String> rejected() {
        return rejected("");
    }

    private static void allowMainThreadToFinish() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static <TInput> AbstractPromise<TInput> rejected(TInput o) {
        AbstractIOU<TInput> d = deferred();

        d.reject(o);
        allowMainThreadToFinish();

        return d.getPromise();
    }

    static class TestMockableCallable<TInput, TOutput> extends TestThenCallable<TInput, TOutput> {

        @Override
        public TOutput apply(TInput o) throws Exception {
            return null;
        }
    }

    static <TInput, TOutput> IThenCallable<TInput, TOutput> fulfillableStub() {
        TestMockableCallable<TInput, TOutput> callable = new TestMockableCallable<>();

        return spy(callable);
    }

    static <TInput, TOutput> IThenCallable<TInput, TOutput> rejectableStub() {
        TestMockableCallable<TInput, TOutput> callable = new TestMockableCallable<>();

        return spy(callable);
    }
}

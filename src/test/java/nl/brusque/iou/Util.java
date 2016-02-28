package nl.brusque.iou;

import static org.mockito.Mockito.spy;

public class Util {
    public static String sanitizeDescriptionName(String name) {
        return name
                .replace(".", "_")
                .replace("(", "[")
                .replace(")", "]");
    }

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

    public static void delay(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

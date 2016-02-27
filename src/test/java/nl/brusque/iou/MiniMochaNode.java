package nl.brusque.iou;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static nl.brusque.iou.Util.sanitizeDescriptionName;
import static org.mockito.Mockito.spy;

public class MiniMochaNode {

    private Date _start;
    private Date _stop;
    private ExecutorService _executor;
    private String _name;

    public void done() {
        _stop = new Date();
        if (!_executor.isTerminated()) {
            _executor.shutdownNow();
        }

        System.out.println(String.format("Start %s, Stop %s", _start, _stop));
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

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = sanitizeDescriptionName(name);
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
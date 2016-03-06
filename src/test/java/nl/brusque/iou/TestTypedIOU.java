package nl.brusque.iou;

public class TestTypedIOU<TInput> extends AbstractIOU<TInput> {
    private final TestTypedPromise<TInput> _promise = new TestTypedPromise<>();

    @Override
    public AbstractPromise<TInput> getPromise() {
        return _promise;
    }
}
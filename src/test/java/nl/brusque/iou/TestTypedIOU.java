package nl.brusque.iou;

class TestTypedIOU<TInput> extends AbstractIOU<TInput> {
    private final TestTypedPromise<TInput> _promise = new TestTypedPromise<>();

    @Override
    public TestTypedPromise<TInput> getPromise() {
        return _promise;
    }
}
package nl.brusque.iou;

class TestTypedPromise<TFulfill> extends AbstractPromise<TFulfill> {
    @Override
    protected <TAnyFulfill> AbstractPromise<TAnyFulfill> create() {
        return new TestTypedPromise<>();
    }
}
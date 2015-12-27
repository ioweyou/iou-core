package nl.brusque.iou;

class TestTypedPromise<TInput> extends AbstractPromise<TInput> {
    protected TestTypedPromise(AbstractThenCaller fulfiller, AbstractThenCaller rejector) {
        super(fulfiller, rejector);
    }

    @Override
    protected <TOutput> AbstractPromise<TOutput> create() {
        return new TestTypedPromise<>(new TestGenericThenCaller(), new TestGenericThenCaller());
    }
}
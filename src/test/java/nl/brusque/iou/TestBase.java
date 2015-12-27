package nl.brusque.iou;

class TestBase {
    private class TestPromise extends AbstractPromise<TestPromise> {
        protected TestPromise(AbstractThenCaller fulfiller, AbstractThenCaller rejector) {
            super(fulfiller, rejector);
        }

        @Override
        public TestPromise create() {
            return new TestPromise(new TestGenericThenCaller(), new TestGenericThenCaller());
        }
    }





}
package nl.brusque.iou;

class TestGenericThenCaller<T, R> extends AbstractThenCaller<T, R> {
    @Override
    IThenCallable<T, R> convert(final IThenCallable<T, R> thenCallable) throws Exception {
        if (thenCallable instanceof TestThenCallable) {
            return thenCallable;
        }

        return new TestThenCallable<T, R>() {

            @Override
            public R apply(T o) throws Exception {
                // DO SOMETHING

                return thenCallable.apply(o);
            }
        };
    }
}

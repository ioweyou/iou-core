package nl.brusque.iou;

class TestGenericThenCallable<T, R> extends AbstractThenCallableStrategy<T, R> {
    @Override
    <TFullfill, TReject> IThenCallable<TFullfill, TReject> convert(final IThenCallable<TFullfill, TReject> thenCallable) throws Exception {
        if (thenCallable instanceof TestThenCallable) {
            return thenCallable;
        }

        return new TestThenCallable<TFullfill, TReject>() {

            @Override
            public TReject apply(TFullfill o) throws Exception {
                // DO SOMETHING

                return thenCallable.apply(o);
            }
        };
    }
}

package nl.brusque.iou;

abstract class AbstractThenCallableStrategy<T, R> {
    abstract <TFullfill, TResult> IThenCallable<TFullfill, TResult> convert(final IThenCallable<TFullfill, TResult> thenCallable) throws Exception;

    R resolve(IThenCallable<T, R> thenCallable, T o) throws Exception {
        return convert(thenCallable).apply(o);
    }

    R reject(IThenCallable<Object, R> thenCallable, Object o) throws Exception {
        return convert(thenCallable).apply(o);
    }
}

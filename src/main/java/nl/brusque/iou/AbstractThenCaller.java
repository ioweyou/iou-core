package nl.brusque.iou;

public abstract class AbstractThenCaller<T, R> {
    abstract IThenCallable<T, R> convert(final IThenCallable<T, R> thenCallable) throws Exception;

    R resolve(IThenCallable<T, R> thenCallable, T o) throws Exception {
        return thenCallable.apply(o);
    }

    final R call(final IThenCallable<T, R> thenCallable, final T o) throws Exception {
        return resolve(convert(thenCallable), o);
    }
}

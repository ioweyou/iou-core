package nl.brusque.iou;

public abstract class AbstractThenCaller {
    abstract <T, R> IThenCallable<T, R> convert(final IThenCallable<T, R> thenCallable) throws Exception;

    <T, R> R resolve(IThenCallable<T, R> thenCallable, T o) throws Exception {
        return thenCallable.apply(o);
    }

    final <T, R> R call(final IThenCallable<T, R> thenCallable, final T o) throws Exception {
        return resolve(convert(thenCallable), o);
    }
}

package nl.brusque.iou;

public interface IThenCallable<T, R> {
    R apply(final T o) throws Exception;
}

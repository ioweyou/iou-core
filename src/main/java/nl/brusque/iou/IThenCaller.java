package nl.brusque.iou;

public interface IThenCaller {
    <T, R, TThenCallable extends IThenCallable<T, R>> R apply(final TThenCallable thenCallable, final T o) throws Exception;
}

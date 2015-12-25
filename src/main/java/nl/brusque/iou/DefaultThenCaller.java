package nl.brusque.iou;

public class DefaultThenCaller {
    public <T, R, TThenCallable extends IThenCallable<T, R>> R apply(final TThenCallable thenCallable, final T o) throws Exception {
        return thenCallable.apply(o);
    }

}

package nl.brusque.iou;

class TestGenericThenCaller extends DefaultThenCaller {
    class MyTestCaller extends DefaultThenCaller {

    }

    @Override
    public <T, R, TThenCallable extends IThenCallable<T, R>> R apply(final TThenCallable thenCallable, final T o) throws Exception {
        return thenCallable.apply(o);
    }
}

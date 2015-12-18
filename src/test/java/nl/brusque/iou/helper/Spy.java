package nl.brusque.iou.helper;

import nl.brusque.iou.Log;

public class Spy implements ISpy {
    private Object _result;
    private boolean _throwsError;
    private final int[] _callCount = {0};
    private final Object[] _calledWith = {null};
    private long _lastCall = 0;
    private String _name = "";

    public void increaseCallCount() {
        _callCount[0]++;
    }

    public void updateCalledWith(Object o) {
        _calledWith[0] = o;
    }

    public int getCallCount() {
        return _callCount[0];
    }

    @Override
    public Object getCalledWith() {
        return _calledWith[0];
    }

    public long lastCall() {
        return _lastCall;
    }

    public String getName() {
        return _name;
    }

    public Object call(Object o) throws Exception {
        Thread.sleep(1); // FIXME delay for '_lastCall'
        increaseCallCount();
        updateCalledWith(o);
        _lastCall = System.currentTimeMillis();

        if (_result.equals("DUMMYA") || _result.equals("DUMMYB") || _result.equals("DUMMYC")) {
            Log.w(String.valueOf(_lastCall));
        }

        if (_throwsError) {
            throw new Exception();
        }

        return _result;
    }

    public ISpy returns(Object o) {
        _result = o;

        _throwsError = false;

        return this;
    }

    public ISpy throwsError() {
        _throwsError = true;

        return this;
    }

    public ISpy name(String name) {
        _name = name;

        return this;
    }
}
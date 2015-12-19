package nl.brusque.iou;

interface ISpy {
    int getCallCount();
    Object getCalledWith();
    Object call(Object o) throws Exception;
    ISpy returns(Object o);
    ISpy throwsError();
    long lastCall();
}
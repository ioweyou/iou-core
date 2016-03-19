package nl.brusque.iou.minimocha;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

class MiniMochaSpecification extends MiniMochaNode implements IMiniMochaDoneListener {
    private MiniMochaSpecificationRunnable _test;
    private Date _start;
    private Date _stop;
    private final Object _waiter = new Object();
    private final long TEST_TIMEOUT        = 10000L;
    private final long TEST_START_OVERHEAD = 500L;

    private final List<AssertionError> _assertionErrors = new ArrayList<>();

    MiniMochaSpecification(String description, MiniMochaSpecificationRunnable test) {
        setName(description);

        _test = test;
    }

    private Thread.UncaughtExceptionHandler createUncaughtExceptionHandler() {
        return new Thread.UncaughtExceptionHandler() {;
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (throwable instanceof AssertionError) {
                    _assertionErrors.add((AssertionError) throwable);
                    done();
                    return;
                }

                throwable.printStackTrace();
                throw new Error("Unexpected uncaughtException", throwable);
            }
        };
    }

    private boolean _isDoneCalled = false;
    public final void done() {

            _stop = new Date();
            _isDoneCalled = true;

            _executor.shutdownNow();

        synchronized(_waiter) {
            _waiter.notifyAll();
       }
    }

    private void testDone() throws TimeoutException {
        if (!_isDoneCalled) {
            throw new TimeoutException("Test timed-out.");
        }
    }

    public final void run() throws InterruptedException, ExecutionException, TimeoutException {

        synchronized(_waiter) {
            final Thread.UncaughtExceptionHandler oldUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(createUncaughtExceptionHandler());
            _executor.submit(new Runnable() {
                @Override
                public void run() {
                    _start = new Date();
                    _test.addDoneListener(MiniMochaSpecification.this);
                    _test.run();

                    try {
                        Thread.sleep(TEST_TIMEOUT); // Give delayed calls a chance to finish
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }

                }
            });

           _waiter.wait(TEST_START_OVERHEAD + TEST_TIMEOUT);
            testDone();

            Thread.setDefaultUncaughtExceptionHandler(oldUncaughtExceptionHandler);
        }



        if (_assertionErrors.size()>0) {
            throw _assertionErrors.get(0);
        }
    }

}
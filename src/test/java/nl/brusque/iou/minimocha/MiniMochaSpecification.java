package nl.brusque.iou.minimocha;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

class MiniMochaSpecification extends MiniMochaNode implements IMiniMochaDoneListener {
    private final Object _synchronizer = new Object();

    private MiniMochaSpecificationRunnable _test;
    private Date _start;
    private Date _stop;
    private final ExecutorService _executor = Executors.newSingleThreadExecutor();
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
                    return;
                }

                System.out.print(throwable.getMessage());
                throw new Error("Unexpected uncaughtException", throwable);
            }
        };
    }

    boolean _isDoneCalled = false;
    public final void done() {
        _stop = new Date();
        _isDoneCalled = true;
        if (!_executor.isTerminated()) {
            _executor.shutdownNow();
        }
        synchronized (_synchronizer) {
            _synchronizer.notify();
        }

        System.out.println(String.format("Start %s, Stop %s", _start, _stop));
    }

    private void testDone() throws TimeoutException {
        if (!_isDoneCalled) {
            throw new TimeoutException("Test timed-out.");
        }
    }

    public final synchronized void run() throws InterruptedException, ExecutionException, TimeoutException {
        Thread.UncaughtExceptionHandler oldUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(createUncaughtExceptionHandler());

        _executor.submit(new Runnable() {
            @Override
            public void run() {
                _start = new Date();
                _test.addDoneListener(MiniMochaSpecification.this);
                _test.run();
            }
        }).get(1000, TimeUnit.MILLISECONDS);

        synchronized (_synchronizer) {
            _synchronizer.wait(5000);
        }

        testDone();

        Thread.setDefaultUncaughtExceptionHandler(oldUncaughtExceptionHandler);

        if (_assertionErrors.size()>0) {
            throw _assertionErrors.get(0);
        }
    }

}
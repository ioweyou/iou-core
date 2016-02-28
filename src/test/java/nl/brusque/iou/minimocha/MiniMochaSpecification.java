package nl.brusque.iou.minimocha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

class MiniMochaSpecification extends MiniMochaNode {
    private final Runnable _test;
    //private final ExecutorService _executor = Executors.newSingleThreadExecutor(new SpecificationExceptionCatchingThreadFactory());
    private final ExecutorService _executor = Executors.newSingleThreadExecutor();
    private final List<AssertionError> _assertionErrors = new ArrayList<>();

    MiniMochaSpecification(String description, Runnable test) {
        _test = test;

        setName(description);
    }

    private class SpecificationExceptionCatchingThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable runnable) {
            Thread threadForRunnable = new Thread(runnable);

            threadForRunnable.setUncaughtExceptionHandler(createUncaughtExceptionHandler());

            return threadForRunnable;
        }
    }

    private Thread.UncaughtExceptionHandler createUncaughtExceptionHandler() {
        return new Thread.UncaughtExceptionHandler() {;
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (throwable instanceof AssertionError) {
                    _assertionErrors.add((AssertionError) throwable);
                    return;
                }

                throw new Error("Unexpected uncaughtException", throwable);
            }
        };
    }

    public final void run() throws InterruptedException, ExecutionException, TimeoutException {
        Thread.UncaughtExceptionHandler oldUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(createUncaughtExceptionHandler());
        try {
            _executor.submit(new Runnable() {
                @Override
                public void run() {
                    _test.run();
                }
            }).get(1000, TimeUnit.MILLISECONDS);
        } catch (AssertionError e) {
            throw e;
        }

        Thread.setDefaultUncaughtExceptionHandler(oldUncaughtExceptionHandler);

        if (_assertionErrors.size()>0) {
            throw _assertionErrors.get(0);
        }
    }

}
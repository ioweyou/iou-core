package nl.brusque.iou.minimocha;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static nl.brusque.iou.Util.sanitizeDescriptionName;

class MiniMochaSpecification extends MiniMochaNode {
    private final Runnable _test;
    private final ExecutorService _executor = Executors.newSingleThreadExecutor();
    private List<AssertionError> _assertionErrors = new ArrayList<>();

    MiniMochaSpecification(String description, Runnable test) {
        _test = test;

        setName(description);
    }

    private void bindThreadExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {;

            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (throwable instanceof AssertionError) {
                    _assertionErrors.add((AssertionError) throwable);
                    return;
                }

                throw new Error("Unexpected uncaughtException", throwable);
            }
        });
    }

    public final void run() throws InterruptedException, ExecutionException, TimeoutException {
        bindThreadExceptionHandler(); // FIXME Bind to specific thread, not global
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

        if (_assertionErrors.size()>0) {
            throw _assertionErrors.get(0);
        }
    }

}
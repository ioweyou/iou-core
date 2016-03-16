package nl.brusque.iou.minimocha;

import java.util.concurrent.*;

class MiniMochaNode {

    private String _name;

    final String getName() {
        return _name;
    }

    void setName(String name) {
        _name = sanitizeDescriptionName(name);
    }

    final ScheduledExecutorService _executor = Executors.newSingleThreadScheduledExecutor();

    public final void allowMainThreadToFinish() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _executor.schedule(runnable, milliseconds, TimeUnit.MILLISECONDS);
    }

    private static String sanitizeDescriptionName(String name) {
        return name
                .replaceAll("[^a-zA-Z0-9_:,`\\-\\)\\( ]+", "_")
                .replaceAll("\\(", "[")
                .replaceAll("\\)", "]")
                .replaceAll("^_+", "")
                .replaceAll("_+$", "");
    }
}
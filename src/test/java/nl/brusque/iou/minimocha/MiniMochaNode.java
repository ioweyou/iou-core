package nl.brusque.iou.minimocha;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MiniMochaNode {

    private String _name;

    public final String getName() {
        return _name;
    }

    void setName(String name) {
        _name = sanitizeDescriptionName(name);
    }

    private final ExecutorService _delayedCallExecutor = Executors.newSingleThreadExecutor();

    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _delayedCallExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(milliseconds);

                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static String sanitizeDescriptionName(String name) {
        return name
                .replaceAll("[^a-zA-Z0-9_:,`\\-\\)\\( ]+", "_")
                .replaceAll("\\(", "[")
                .replaceAll("\\)", "]")
                .replaceAll("^_+", "")
                .replaceAll("_+$", "");
    }
}
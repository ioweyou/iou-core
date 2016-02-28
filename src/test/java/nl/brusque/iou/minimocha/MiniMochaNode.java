package nl.brusque.iou.minimocha;

import java.util.Date;
import java.util.concurrent.ExecutorService;

class MiniMochaNode {

    private Date _start;
    private Date _stop;
    private ExecutorService _executor;
    private String _name;

    public final void done() {
        _stop = new Date();
        if (!_executor.isTerminated()) {
            _executor.shutdownNow();
        }

        System.out.println(String.format("Start %s, Stop %s", _start, _stop));
    }

    public final String getName() {
        return _name;
    }

    void setName(String name) {
        _name = sanitizeDescriptionName(name);
    }

    public static String sanitizeDescriptionName(String name) {
        return name
                .replaceAll("[^a-zA-Z0-9_:,`\\)\\( ]+", "_")
                .replaceAll("\\(", "[")
                .replaceAll("\\)", "]")
                .replaceAll("^_+", "")
                .replaceAll("_+$", "");
    }
}
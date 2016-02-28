package nl.brusque.iou.minimocha;

import java.util.Date;
import java.util.concurrent.ExecutorService;

import static nl.brusque.iou.Util.sanitizeDescriptionName;
import static org.mockito.Mockito.spy;

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
}
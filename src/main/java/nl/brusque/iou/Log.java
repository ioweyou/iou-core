package nl.brusque.iou;

import java.util.logging.Logger;

public class Log {
    private static Logger _logger = Logger.getAnonymousLogger();

    public static void w(String message) {
        _logger.warning(message);
    }

    public static void i(String message) {
        _logger.info(message);
    }

    public static void e(String message) {
        _logger.severe(message);
    }
}

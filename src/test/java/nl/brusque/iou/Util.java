package nl.brusque.iou;

public class Util {
    public static String sanitizeDescriptionName(String name) {
        return name
                .replace(".", "_")
                .replace("(", "[")
                .replace(")", "]");
    }
}

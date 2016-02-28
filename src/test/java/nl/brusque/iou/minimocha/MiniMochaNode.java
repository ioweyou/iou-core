package nl.brusque.iou.minimocha;

class MiniMochaNode {

    private String _name;

    public final String getName() {
        return _name;
    }

    void setName(String name) {
        _name = sanitizeDescriptionName(name);
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
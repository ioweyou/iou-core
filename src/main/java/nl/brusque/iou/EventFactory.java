package nl.brusque.iou;

final class EventFactory {
    public static <T extends DefaultEvent> T create(final Class<T> clazz, Object value) {
        try {
            return clazz.getConstructor(Object.class).newInstance(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

package nl.brusque.iou;

class EventFactory {
    public static <T extends AbstractEvent> T create(final Class<T> clazz, Object value) {
        try {
            return clazz.getConstructor(Object.class).newInstance(value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

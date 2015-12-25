package nl.brusque.iou;

interface IEventListener<T extends AbstractEvent> {
    void process(T event);
}

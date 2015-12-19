package nl.brusque.iou;

interface IEventListener<T extends IEvent> {
    void process(T event);
}

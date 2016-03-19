package nl.brusque.iou;

interface IEventListener<T extends DefaultEvent> {
    void process(T event) throws Exception;
}

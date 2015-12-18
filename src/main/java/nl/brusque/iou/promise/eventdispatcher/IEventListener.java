package nl.brusque.iou.promise.eventdispatcher;

public interface IEventListener<T extends IEvent> {
    void process(T event);
}

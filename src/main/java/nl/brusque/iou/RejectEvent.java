package nl.brusque.iou;

final class RejectEvent<TInput, TAnything> extends DefaultEvent<TInput, TAnything> {
    RejectEvent(AbstractPromise<TInput> promise, TAnything value)
    {
        super(promise, value);
    }
}

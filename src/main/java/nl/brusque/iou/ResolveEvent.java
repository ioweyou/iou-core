package nl.brusque.iou;

final class ResolveEvent<TInput, TAnything> extends DefaultEvent<TInput, TAnything> {
    ResolveEvent(AbstractPromise<TInput> promise, TAnything eventValue) {
        super(promise, eventValue);
    }
}

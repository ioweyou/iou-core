package nl.brusque.iou;

final class RejectEventListener<TInput, TAnything> implements IEventListener<RejectEvent<TInput, TAnything>> {
    private final PromiseState _promiseState;

    public RejectEventListener(PromiseState promiseState) {
        _promiseState = promiseState;
    }

    @Override
    public void process(RejectEvent<TInput, TAnything> event) {
        _promiseState.reject(event.getValue().getValue());
    }
}

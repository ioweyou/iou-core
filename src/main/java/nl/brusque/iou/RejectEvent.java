package nl.brusque.iou;

final class RejectEvent<TInput, TAnything> extends DefaultEvent<RejectEventValue<TInput, TAnything>> {
    public RejectEvent(RejectEventValue<TInput, TAnything> value)
    {
        super(value);
    }
}

package nl.brusque.iou;

final class ResolveEvent<TAnything> extends DefaultEvent<ResolveEventValue<TAnything>> {
    public ResolveEvent(ResolveEventValue<TAnything> value)
    {
        super(value);
    }
}

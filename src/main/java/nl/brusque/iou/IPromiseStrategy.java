package nl.brusque.iou;

public interface IPromiseStrategy<TInput, TOutput> {
    <TAnythingInput, TAnythingOutput> void then(IThenCallable<TInput, TOutput> onFulfillable, IThenCallable<TAnythingInput, TAnythingOutput> onRejectable);
    boolean isPending();
    boolean isResolved();
    boolean isRejected();
}

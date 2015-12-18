package nl.brusque.iou;

public interface IPromise<TResult extends IPromise, TFulfillable extends IFulfillable, TRejectable extends IRejectable> extends IThenable<TResult> {
    boolean isFulfillable(Object onFulfilled);
    boolean isRejectable(Object onRejected);
    Object runFulfill(TFulfillable fulfillable, Object o) throws Exception;
    Object runReject(TRejectable rejectable, Object o) throws Exception;


    TResult create();
    TResult resolve(Object run);
    TResult reject(Object o);

}

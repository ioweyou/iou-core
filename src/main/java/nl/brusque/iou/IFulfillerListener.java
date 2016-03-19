package nl.brusque.iou;

interface IFulfillerListener<TFulfill> {
    void onFulfill(TFulfill o) throws Exception;
}

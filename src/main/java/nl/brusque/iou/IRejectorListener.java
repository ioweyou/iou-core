package nl.brusque.iou;

interface IRejectorListener {
    void onReject(Object value) throws Exception;
}

package nl.brusque.iou;

final class Fulfiller<TFulfill> {
    private final ResolvableManager<TFulfill> _resolvableManager;

    Fulfiller(ResolvableManager<TFulfill> resolvableManager) {
        _resolvableManager = resolvableManager;
    }

    void fulfill(final TFulfill value) {
        _resolvableManager.process(new Function<Resolvable<TFulfill, ?>, Void>() {
            @Override
            Void apply(Resolvable<TFulfill, ?> resolvable) {
                try {
                    resolvable.resolve(value);
                } catch (Exception e) {
                    // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                    resolvable.getPromise().reject(e);
                }

                return null;
            }
        });

    }
}

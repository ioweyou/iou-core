package nl.brusque.iou;

final class Rejector<TFulfill> {
    private final ResolvableManager<TFulfill> _resolvableManager;

    Rejector(ResolvableManager<TFulfill> resolvableManager) {
        _resolvableManager = resolvableManager;
    }

    <TAnything> void reject(final TAnything reason) {
        _resolvableManager.process(new Function<Resolvable<TFulfill, ?>, Void>() {
            @Override
            Void apply(Resolvable<TFulfill, ?> resolvable) {
                try {
                    resolvable.reject(reason);
                } catch (Exception e) {
                    // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                    resolvable.getPromise().reject(e);
                }

                return null;
            }
        });
    }
}

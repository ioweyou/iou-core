package nl.brusque.iou;

final class FireFulfillsEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FireFulfillsEvent> {
    private final PromiseStateHandler _promiseState;
    private final ResolvableManager _resolvableManager;
    private final AbstractThenCaller _fulfiller;

    public FireFulfillsEventListener(PromiseStateHandler promiseState, ResolvableManager resolvableManager, AbstractThenCaller fulfiller) {
        _promiseState      = promiseState;
        _resolvableManager = resolvableManager;
        _fulfiller         = fulfiller;
    }

    @Override
    public void process(FireFulfillsEvent event) {
        _resolvableManager.process(new Function<Resolvable, Void>() {
            @Override
            Void apply(Resolvable resolvable) {
                IThenCallable fulfillable = resolvable.getFulfillable();

                try {
                    if (fulfillable == null) {
                        resolvable.getPromise().resolve(_promiseState.getResolvedWith());
                        return null;
                    }
                    Object result = _fulfiller.call(fulfillable, _promiseState.getResolvedWith());
                    if (result == null) {
                        return null;
                    }

                    // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                    resolvable.getPromise().resolve(result);
                } catch (Exception e) {
                    // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                    resolvable.getPromise().reject(e);
                }

                return null;
            }
        });

    }
}

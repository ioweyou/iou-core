package nl.brusque.iou;

import java.util.ArrayDeque;

class FireFulfillsEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FireFulfillsEvent> {
    private final PromiseStateHandler _promiseState;
    private final ArrayDeque<Resolvable> _onResolve;
    private final AbstractThenCaller _fulfiller;

    public FireFulfillsEventListener(PromiseStateHandler promiseState, ArrayDeque<Resolvable> onResolve, AbstractThenCaller fulfiller) {
        _promiseState = promiseState;
        _onResolve    = onResolve;
        _fulfiller    = fulfiller;
    }

    public void process(FireFulfillsEvent event) {
        while (!_onResolve.isEmpty()) {
            Resolvable resolvable = _onResolve.remove();
            IThenCallable fulfillable = resolvable.getFulfillable();

            try {
                if (fulfillable == null) {
                    resolvable.getPromise().resolve(_promiseState.getResolvedWith());
                    return;
                }
                Object result = _fulfiller.call(fulfillable, _promiseState.getResolvedWith());
                if (result == null) {
                    return;
                }

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                resolvable.getPromise().resolve(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                resolvable.getPromise().reject(e);
            }
        }
    }
}

package nl.brusque.iou;

import java.util.ArrayDeque;

class FireRejectsEventListener<TResult extends AbstractPromise<TResult>> implements IEventListener<FireRejectsEvent> {
    private final PromiseStateHandler _promiseState;
    private final ArrayDeque<Resolvable> _onResolve;
    private final AbstractThenCaller _rejector;

    public FireRejectsEventListener(PromiseStateHandler promiseState, ArrayDeque<Resolvable> onResolve, AbstractThenCaller rejector) {
        _promiseState = promiseState;
        _onResolve    = onResolve;
        _rejector     = rejector;
    }

    @Override
    public void process(FireRejectsEvent event) {
        while (!_onResolve.isEmpty()) {
            Resolvable resolvable = _onResolve.remove();
            IThenCallable  rejectable  = resolvable.getRejectable();

            try {
                if (rejectable == null) {
                    resolvable.getPromise().reject(_promiseState.RejectedWith());
                    return;
                }

                Object result = _rejector.call(rejectable, _promiseState.RejectedWith());

                // 2.2.7.1 If either onFulfilled or onRejected returns a value x, run the Promise Resolution Procedure [[Resolve]](promise2, x).
                resolvable.getPromise().reject(result);
            } catch (Exception e) {
                // 2.2.7.2 If either onFulfilled or onRejected throws an exception e, promise2 must be rejected with e as the reason.
                resolvable.getPromise().reject(e);
            }
        }
    }
}

package nl.brusque.iou;

import nl.brusque.iou.errors.TypeError;

import java.lang.reflect.Method;

final class PromiseResolver<TFulfill> {
    private final PromiseState<TFulfill> _stateHandler;

    public PromiseResolver(PromiseState<TFulfill> stateHandler) {
        _stateHandler    = stateHandler;
    }

    public void resolve(AbstractPromise<TFulfill> promise, Object x) {
        Boolean promiseAndXReferToTheSameObject = promise == x;
        Boolean xIsAPromise = x instanceof AbstractPromise;

        if (promiseAndXReferToTheSameObject) {
            promise.reject(new TypeError());
            return;
        } else if (xIsAPromise) {
            resolveXIsAPromise((AbstractPromise)x);
            return;
        }

        resolveThenablesAndValues(promise, x);
    }

    private void resolveXIsAPromise(AbstractPromise x) {
        _stateHandler.adopt(x);
    }

    private void resolveThenablesAndValues(AbstractPromise<TFulfill> promise, Object x) {
        Boolean isXNonExpensiveThenable = x instanceof IThenable;
        if (isXNonExpensiveThenable) {
            resolveThenable(promise, (IThenable)x);
            return;
        }

        try {
            Method thenableMethod = x.getClass().getMethod("then");

            Boolean isExpensiveThenable = thenableMethod != null;
            if (isExpensiveThenable) {
                resolveExpensiveThenable(promise, x, thenableMethod);
                return;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        resolveValue((TFulfill)x);
    }

    private <TThenable> void resolveThenable(final AbstractPromise<TFulfill> promise, IThenable<TThenable> x) {
        /*x.then(new IThenCallable<TThenable, Void>() {
            @Override
            public Void apply(TThenable y) throws Exception {
                resolve(promise, y);

                return null;
            }
        }, new IThenCallable<Object, Void>() {
            @Override
            public Void apply(Object r) throws Exception {
                promise.reject(new RejectReason<>(r));

                return null;
            }
        });*/
    }

    private <TInput> void resolveExpensiveThenable(AbstractPromise<TInput> promise, Object x, Method then) throws Exception {
        //throw new Exception("Resolving anonymous thenables not implemented.");

    }

    private void resolveValue(TFulfill x) {
        _stateHandler.fulfill(x);
    }
}

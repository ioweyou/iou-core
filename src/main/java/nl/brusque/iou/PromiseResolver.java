package nl.brusque.iou;

import java.lang.reflect.Method;

public class PromiseResolver {
    private final PromiseStateHandler _stateHandler;

    public PromiseResolver(PromiseStateHandler stateHandler) {
        _stateHandler = stateHandler;
    }
    public <TInput> void resolve(AbstractPromise<TInput> promise, Object x) {
        Boolean promiseAndXReferToTheSameObject = promise == x;
        Boolean xIsAPromise = x instanceof AbstractPromise;

        if (promiseAndXReferToTheSameObject) {
            promise.reject(new RejectReason<TInput>(null, "TypeError"));
            return;
        } else if (xIsAPromise) {
            resolveXIsAPromise(promise, (AbstractPromise)x);
            return;
        }

        resolveThenablesAndValues(promise, x);
    }


    private <TInput> void resolveXIsAPromise(AbstractPromise<TInput> promise, AbstractPromise x) {
        _stateHandler.fulfill(x);
    }

    private <TInput> void resolveThenablesAndValues(AbstractPromise<TInput> promise, Object x) {
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
            e.printStackTrace();
        }

        resolveValue(promise, x);
    }

    private <TInput, TThenable> void resolveThenable(final AbstractPromise<TInput> promise, IThenable<TThenable> x) {
        x.then(new IThenCallable<TThenable, Void>() {
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
        });
    }

    private <TInput> void resolveExpensiveThenable(AbstractPromise<TInput> promise, Object x, Method then) throws Exception {
        throw new Exception("Resolving anonymous thenables not implemented.");
        //then.invoke(x)
    }

    private <TInput> void resolveValue(AbstractPromise<TInput> promise, Object x) {
        _stateHandler.fulfill(x);
    }
}

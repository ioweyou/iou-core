package nl.brusque.iou;

import nl.brusque.iou.errors.TypeError;

import java.lang.reflect.Method;

final class PromiseResolver {
    static <TFulfill, TAnything> void resolve(PromiseState<TFulfill> promise, TAnything x) throws Exception {
        Boolean promiseAndXReferToTheSameObject = promise == x;
        Boolean xIsAPromise = x instanceof AbstractPromise;

        if (promiseAndXReferToTheSameObject) {
            promise.reject(new TypeError());
            return;
        } else if (xIsAPromise) {
            resolveXIsAPromise(promise, (AbstractPromise)x);
            return;
        }

        resolveThenablesAndValues(promise, x);
    }

    private static <TFulfill> void resolveXIsAPromise(PromiseState<TFulfill> promise, AbstractPromise x) throws Exception {
        promise.adopt(x);
    }

    private static <TFulfill, TAnything> void resolveThenablesAndValues(PromiseState<TFulfill> promise, TAnything x) throws Exception {
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

        resolveValue(promise, (TFulfill)x);
    }

    private static <TFulfill, TThenable> void resolveThenable(final PromiseState<TFulfill> promise, IThenable<TThenable> x) {
        final Boolean[] isResolvePromiseCalled = new Boolean[]{false};
        final Boolean[] isRejectPromiseCalled = new Boolean[]{false};

        try {
            x.then(new IThenCallable<TThenable, Void>() {
                @Override
                public Void apply(TThenable y) throws Exception {
                    if (isRejectPromiseCalled[0] || isResolvePromiseCalled[0]) {
                        return null;
                    }

                    isResolvePromiseCalled[0] = true;
                    resolve(promise, y);

                    return null;
                }
            }, new IThenCallable<Object, Void>() {
                @Override
                public Void apply(Object r) throws Exception {
                    if (isRejectPromiseCalled[0] || isResolvePromiseCalled[0]) {
                        return null;
                    }

                    isRejectPromiseCalled[0] = true;
                    promise.reject(r);

                    return null;
                }
            });
        } catch (Error | Exception e) {
            boolean ignoreException = isResolvePromiseCalled[0] || isRejectPromiseCalled[0];
            if (!ignoreException) {
                try {
                    promise.reject(e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static <TInput> void resolveExpensiveThenable(PromiseState<TInput> promise, Object x, Method then) throws Exception {
        throw new Exception("Resolving anonymous thenables not implemented.");
    }

    private static <TFulfill> void resolveValue(final PromiseState<TFulfill> promise, TFulfill x) throws Exception {
        promise.fulfill(x);
    }
}

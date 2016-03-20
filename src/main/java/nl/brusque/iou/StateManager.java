package nl.brusque.iou;

import java.util.*;

class StateManager {
    private final HashMap<AbstractPromise, PromiseState> _promiseStates = new HashMap<>();

    void register(AbstractPromise promise, PromiseState promiseState) {
        _promiseStates.put(promise, promiseState);
    }

    <TFulfill> void addFulfillerListener(AbstractPromise<TFulfill> promise, IFulfillerListener<TFulfill> listener) throws Exception {
        getStateForPromise(promise)
                .addFulfillerListener(listener);
    }

    void addRejectorListener(AbstractPromise promise, IRejectorListener listener) throws Exception {
        getStateForPromise(promise)
                .addRejectorListener(listener);
    }

    <TFulfill> boolean isRejected(AbstractPromise<TFulfill> promise) throws Exception {
        return getStateForPromise(promise)
                .isRejected();
    }

    <TFulfill> boolean isResolved(AbstractPromise<TFulfill> promise) throws Exception {
        return getStateForPromise(promise)
                .isResolved();
    }

    <TFulfill> boolean isPending(AbstractPromise<TFulfill> promise) throws Exception {
        return getStateForPromise(promise)
                .isPending();
    }

    <TFulfill> Object getRejectionReason(AbstractPromise<TFulfill> promise) throws Exception {
        return getStateForPromise(promise)
                .getRejectionReason();
    }

    <TFulfill> TFulfill getResolvedWith(AbstractPromise<TFulfill> promise) throws Exception {
        return getStateForPromise(promise)
                .getResolvedWith();
    }

    <TFulfill> PromiseState<TFulfill> getStateForPromise(AbstractPromise<TFulfill> promise) throws Exception {
        if (!_promiseStates.containsKey(promise)) {
            throw new Exception("Non-registered promise.");
        }

        return _promiseStates.get(promise);
    }

    <TFulfill> State getState(AbstractPromise<TFulfill> promise) throws Exception {
        return getStateForPromise(promise)
                .getState();
    }

    <TFulfill> void fulfill(AbstractPromise<TFulfill> promise, TFulfill x) throws Exception {
        getStateForPromise(promise)
                .fulfill(x);
    }

    <TFulfill, TAnything> void reject(AbstractPromise<TFulfill> promise, TAnything x) throws Exception {
        getStateForPromise(promise)
                .reject(x);
    }
}

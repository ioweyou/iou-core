package nl.brusque.iou;

import java.util.ArrayList;
import java.util.List;

final class PromiseState<TFulfill> {
    private final Fulfiller<TFulfill>_fulfiller;
    private final Rejector<TFulfill> _rejector;

    private IStateStrategy<TFulfill> _stateStrategy = new DefaultStateStrategy();
    private StateManager _stateManager = new StateManager();

    PromiseState(Fulfiller<TFulfill> fulfiller, Rejector<TFulfill> rejector) {
        _fulfiller = fulfiller;
        _rejector  = rejector;
    }

    Object getRejectionReason() throws Exception {
        return _stateStrategy.getRejectionReason();
    }

    boolean isRejected() {
        try {
            return _stateStrategy.getState() == State.REJECTED;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    boolean isResolved() {
        try {
            return _stateStrategy.getState() == State.RESOLVED;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    TFulfill getResolvedWith() throws Exception {
        return _stateStrategy.getResolvedWith();
    }

    boolean isPending() {
        try {
            return _stateStrategy.getState() == State.PENDING;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    <TAnything> void registerPromiseState(AbstractPromise<TAnything> promise, PromiseState state) {
        _stateManager.register(promise, state);
    }

    private interface IStateStrategy<TStrategyFulfill> {
        void fulfill(TStrategyFulfill o) throws Exception;
        <TAnything> void reject(TAnything reason) throws Exception;
        Object getRejectionReason() throws Exception;
        State getState() throws Exception;
        TStrategyFulfill getResolvedWith() throws Exception;
    }

    private List<IFulfillerListener<TFulfill>> _fulfillerListeners = new ArrayList<>();
    private List<IRejectorListener> _rejectorListeners = new ArrayList<>();

    private class DefaultStateStrategy implements IStateStrategy<TFulfill> {
        private Object _rejectionReason;
        private TFulfill _resolvedWith;
        private State _state = State.PENDING;


        @Override
        public void fulfill(TFulfill o) throws Exception {
            if (_state == State.PENDING) {
                _state = State.RESOLVED;

                _resolvedWith = o;

            }

            _fulfiller.fulfill(_resolvedWith);
            for (IFulfillerListener<TFulfill> listener : _fulfillerListeners) {
                listener.onFulfill(_resolvedWith);
            }
        }

        @Override
        public <TAnything> void reject(TAnything reason) throws Exception {
            if (_state == State.PENDING) {
                _state = State.REJECTED;

                _rejectionReason = reason;
            }

            _rejector.reject(_rejectionReason);
            for (IRejectorListener listener : _rejectorListeners) {
                listener.onReject(_rejectionReason);
            }
        }

        @Override
        public Object getRejectionReason() {
            return _rejectionReason;
        }

        @Override
        public State getState() {
            return _state;
        }

        @Override
        public TFulfill getResolvedWith() {
            return _resolvedWith;
        }
    }

    private class AdoptedStateStrategy implements IStateStrategy<TFulfill>,  IRejectorListener, IFulfillerListener<TFulfill> {
        private final AbstractPromise<TFulfill> _adoptedPromise;

        // FIXME Trainwrecks: _adoptedPromise.get().<something>

        AdoptedStateStrategy(AbstractPromise<TFulfill> adoptedStatePromise) throws Exception {
            _adoptedPromise      = adoptedStatePromise;

            _stateManager.addFulfillerListener(_adoptedPromise, this);
            _stateManager.addRejectorListener(_adoptedPromise, this);

            if (_stateManager.isRejected(_adoptedPromise)) {
                _rejector.reject(_stateManager.getRejectionReason(_adoptedPromise));
            } else if (_stateManager.isResolved(_adoptedPromise)) {
                _fulfiller.fulfill(_stateManager.getResolvedWith(_adoptedPromise));
            }
        }

        @Override
        public void fulfill(TFulfill o) throws Exception { // FIXME Narrow interface
            // If x is pending, promise must remain pending until x is fulfilled or rejected.
            if (_stateManager.isPending(_adoptedPromise)) {
                return;
            }

            // If/when x is fulfilled, fulfill promise with the same value.
            _fulfiller.fulfill(_stateManager.getResolvedWith(_adoptedPromise));
        }

        @Override
        public <TAnything> void reject(TAnything reason) throws Exception { // FIXME Narrow interface
            // If x is pending, promise must remain pending until x is fulfilled or rejected.
            if (_stateManager.isPending(_adoptedPromise)) {
                return;
            }

            // If/when x is rejected, reject promise with the same reason.
            _rejector.reject(_stateManager.getRejectionReason(_adoptedPromise));
        }

        @Override
        public Object getRejectionReason() throws Exception {
            return _stateManager.getRejectionReason(_adoptedPromise);
        }

        @Override
        public State getState() throws Exception {
            return _stateManager.getState(_adoptedPromise);
        }

        @Override
        public TFulfill getResolvedWith() throws Exception {
            return _stateManager.getResolvedWith(_adoptedPromise);
        }

        @Override
        public void onFulfill(TFulfill o) throws Exception {
            _fulfiller.fulfill(_stateManager.getResolvedWith(_adoptedPromise));
        }

        @Override
        public void onReject(Object value) throws Exception {
            _rejector.reject(_stateManager.getRejectionReason(_adoptedPromise));
        }
    }

    State getState() throws Exception {
        return _stateStrategy.getState();
    }

    void addFulfillerListener(IFulfillerListener listener) {
        _fulfillerListeners.add(listener);
    }

    void addRejectorListener(IRejectorListener listener) {
        _rejectorListeners.add(listener);
    }

    void adopt(AbstractPromise x) throws Exception {
        x.shareStateWith(this);

        _stateStrategy = new AdoptedStateStrategy(x);
    }

    void fulfill(TFulfill o) throws Exception {
        _stateStrategy.fulfill(o);
    }

    <TAnything> void reject(TAnything reason) throws Exception {
        _stateStrategy.reject(reason);
    }
}

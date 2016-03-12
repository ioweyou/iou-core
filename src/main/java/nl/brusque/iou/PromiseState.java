package nl.brusque.iou;

import java.util.ArrayList;
import java.util.List;

final class PromiseState<TFulfill> {
    private final Fulfiller<TFulfill>_fulfiller;
    private final Rejector<TFulfill> _rejector;

    private IStateStrategy<TFulfill> _stateStrategy = new DefaultStateStrategy();

    public PromiseState(Fulfiller<TFulfill> fulfiller, Rejector<TFulfill> rejector) {
        _fulfiller = fulfiller;
        _rejector  = rejector;
    }

    public Object getRejectionReason() {
        return _stateStrategy.getRejectionReason();
    }

    public boolean isRejected() {
        return _stateStrategy.getState() == State.REJECTED;
    }

    public State getState() {
        return _stateStrategy.getState();
    }

    public boolean isResolved() {
        return _stateStrategy.getState() == State.RESOLVED;
    }

    public TFulfill getResolvedWith() {
        return _stateStrategy.getResolvedWith();
    }

    public boolean isPending() {
        return _stateStrategy.getState() == State.PENDING;
    }

    private interface IStateStrategy<TStrategyFulfill> {
        void fulfill(TStrategyFulfill o);
        void reject(Object reason);
        Object getRejectionReason();
        State getState();
        TStrategyFulfill getResolvedWith();
    }

    private List<IFulfillerListener<TFulfill>> _fulfillerListeners = new ArrayList<>();
    private List<IRejectorListener> _rejectorListeners = new ArrayList<>();

    private class DefaultStateStrategy implements IStateStrategy<TFulfill> {
        private Object _rejectionReason;
        private TFulfill _resolvedWith;
        private State _state = State.PENDING;


        @Override
        public void fulfill(TFulfill o) {
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
        public void reject(Object reason) {
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
        private final PromiseState<TFulfill> _adoptedPromiseState;

        public AdoptedStateStrategy(AbstractPromise<TFulfill> adoptedStatePromise) {
            _adoptedPromise      = adoptedStatePromise;
            _adoptedPromiseState = _adoptedPromise.leakState();

            _adoptedPromiseState.addFulfillerListener(this);
            _adoptedPromiseState.addRejectorListener(this);
            if (_adoptedPromiseState.isRejected()) {
                _rejector.reject(_adoptedPromiseState.getRejectionReason());
            } else if (_adoptedPromiseState.isResolved()) {
                _fulfiller.fulfill(_adoptedPromiseState.getResolvedWith());
            }
        }

        @Override
        public void fulfill(TFulfill o) { // FIXME Narrow interface
            // If x is pending, promise must remain pending until x is fulfilled or rejected.
            if (_adoptedPromiseState.isPending()) {
                return;
            }

            // If/when x is fulfilled, fulfill promise with the same value.
            _fulfiller.fulfill(_adoptedPromiseState.getResolvedWith());
        }

        @Override
        public void reject(Object reason) { // FIXME Narrow interface
            // If x is pending, promise must remain pending until x is fulfilled or rejected.
            if (_adoptedPromiseState.isPending()) {
                return;
            }

            // If/when x is rejected, reject promise with the same reason.
            _rejector.reject(_adoptedPromiseState.getRejectionReason());
        }

        @Override
        public Object getRejectionReason() {
            return _adoptedPromiseState.getRejectionReason();
        }

        @Override
        public State getState() {
            return _adoptedPromiseState.getState();
        }

        @Override
        public TFulfill getResolvedWith() {
            return _adoptedPromiseState.getResolvedWith();
        }

        @Override
        public void onFulfill(TFulfill o) {
            _fulfiller.fulfill(_adoptedPromiseState.getResolvedWith());
        }

        @Override
        public void onReject(Object value) {
            _rejector.reject(_adoptedPromiseState.getRejectionReason());
        }
    }


    public void addFulfillerListener(IFulfillerListener listener) {
        _fulfillerListeners.add(listener);
    }

    public void addRejectorListener(IRejectorListener listener) {
        _rejectorListeners.add(listener);
    }

    public void adopt(AbstractPromise x) {
        _stateStrategy = new AdoptedStateStrategy(x);
    }

    public void fulfill(TFulfill o) {
        _stateStrategy.fulfill(o);
    }

    public void reject(Object reason) {
        _stateStrategy.reject(reason);
    }
}

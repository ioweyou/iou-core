package nl.brusque.iou;

import nl.brusque.iou.errors.TypeError;
import nl.brusque.iou.errors.TypeErrorException;

import java.awt.*;
import java.util.ArrayDeque;

class PromiseValueResolver {

    private final EventDispatcher _eventDispatcher;
    private final PromiseStateHandler _promiseState;

    class PromiseRejectable implements IThenCallable {
        @Override
        public Object apply(Object o) throws Exception {
            _promiseState.reject(o);
            _eventDispatcher.queue(new FireRejectsEvent());

            return o;
        }
    }

    class PromiseFulfillable implements IThenCallable {
        @Override
        public Object apply(Object o) throws Exception {
            _promiseState.fulfill(o);
            _eventDispatcher.queue(new FireFulfillsEvent());

            return o;
        }
    }

    public PromiseValueResolver(EventDispatcher eventDispatcher, PromiseStateHandler promiseState) {
        _eventDispatcher = eventDispatcher;
        _promiseState    = promiseState;
    }

    public void resolve(AbstractPromise promise) {
        promise.then(new PromiseFulfillable(), new PromiseRejectable());
    }
}

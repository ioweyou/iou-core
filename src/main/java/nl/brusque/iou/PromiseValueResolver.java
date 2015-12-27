package nl.brusque.iou;

final class PromiseValueResolver {

    private final EventDispatcher _eventDispatcher;
    private final PromiseStateHandler _promiseState;

    class PromiseRejectable<TInput> implements IThenCallable<TInput, TInput> {
        @Override
        public TInput apply(TInput o) throws Exception {
            _promiseState.reject(o);
            _eventDispatcher.queue(new FireRejectsEvent());

            return o;
        }
    }

    class PromiseFulfillable<TInput> implements IThenCallable<TInput, TInput> {
        @Override
        public TInput apply(TInput o) throws Exception {
            _promiseState.fulfill(o);
            _eventDispatcher.queue(new FireFulfillsEvent());

            return o;
        }
    }

    public PromiseValueResolver(EventDispatcher eventDispatcher, PromiseStateHandler promiseState) {
        _eventDispatcher = eventDispatcher;
        _promiseState    = promiseState;
    }

    public <TInput> void resolve(AbstractPromise<TInput> promise) {
        promise.then(new PromiseFulfillable<TInput>(), new PromiseRejectable<TInput>());
    }
}

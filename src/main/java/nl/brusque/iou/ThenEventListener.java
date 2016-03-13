package nl.brusque.iou;

final class ThenEventListener<TFulfill, TOutput> implements IEventListener<ThenEvent<TFulfill, TOutput>> {
    private final ResolvableManager<TFulfill> _resolvableManager;
    private final PromiseState<TFulfill> _promiseState;
    private final PromiseEventHandler<TFulfill> _promiseEventHandler;


    public ThenEventListener(PromiseState<TFulfill> promiseState, ResolvableManager<TFulfill> resolvableManager, PromiseEventHandler<TFulfill> promiseEventHandler) {
        _promiseState        = promiseState;
        _resolvableManager   = resolvableManager;
        _promiseEventHandler = promiseEventHandler;
    }

    @Override
    public void process(ThenEvent<TFulfill, TOutput> event) {
        IThenCallable<TFulfill, TOutput> fulfillable = event.getFulfillable();
        IThenCallable<Object, TOutput> rejectable  = event.getRejectable();
        boolean isFulfillable = fulfillable!=null;
        boolean isRejectable  = rejectable!=null;

        addResolvable(fulfillable, rejectable, event.getPromise());
    }

    private void addResolvable(IThenCallable<TFulfill, TOutput> fulfillable, IThenCallable<Object, TOutput> rejectable, AbstractPromise<TOutput> nextPromise) {
        _resolvableManager.add(new Resolvable<>(fulfillable, rejectable, nextPromise));

        if (_promiseState.isRejected()) {
            _promiseEventHandler.reject(_promiseState.getRejectionReason());
        } else if (_promiseState.isResolved()) {
            _promiseEventHandler.resolve(_promiseState.getResolvedWith());
        }
    }
}

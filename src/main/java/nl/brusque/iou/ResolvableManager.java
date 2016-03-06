package nl.brusque.iou;

import java.util.ArrayDeque;

final class ResolvableManager<TFulfill> {
    private final ArrayDeque<Resolvable<TFulfill, ?>> _onResolve = new ArrayDeque<>();

    public <TOutput> void add(Resolvable<TFulfill, TOutput> resolvable) {
        _onResolve.add(resolvable);
    }

    public void process(Function<Resolvable<TFulfill, ?>, Void> f) {
        while (!_onResolve.isEmpty()) {
            f.apply(_onResolve.remove());
        }
    }
}

package nl.brusque.iou;

import java.util.ArrayDeque;

final class ResolvableManager<TFulfill> {
    private final ArrayDeque<Resolvable<TFulfill, ?>> _onResolve = new ArrayDeque<>();

    <TOutput> void add(Resolvable<TFulfill, TOutput> resolvable) {
        _onResolve.add(resolvable);
    }

    void process(Function<Resolvable<TFulfill, ?>, Void> f) {
        synchronized (_onResolve) {
            while (!_onResolve.isEmpty()) {
                f.apply(_onResolve.remove());
            }
        }
    }
}

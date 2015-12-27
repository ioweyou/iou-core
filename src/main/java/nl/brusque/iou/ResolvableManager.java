package nl.brusque.iou;

import java.util.ArrayDeque;

class ResolvableManager {
    private final ArrayDeque<Resolvable> _onResolve = new ArrayDeque<>();

    public void add(Resolvable resolvable) {
        _onResolve.add(resolvable);
    }

    public void process(Function<Resolvable, Void> f) {
        while (!_onResolve.isEmpty()) {
            f.apply(_onResolve.remove());
        }
    }
}

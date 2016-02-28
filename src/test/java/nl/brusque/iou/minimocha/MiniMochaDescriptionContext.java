package nl.brusque.iou.minimocha;

import java.util.ArrayDeque;

class MiniMochaDescriptionContext extends MiniMochaNode {
    private static ArrayDeque<MiniMochaDescription> _miniMochaContext =
            new ArrayDeque<MiniMochaDescription>() {
                { add(MiniMochaDescription.ROOT); }
            };

    static MiniMochaDescription getContext() {
        return _miniMochaContext.peek();
    }

    static MiniMochaDescription push(MiniMochaDescription description) {
        _miniMochaContext.push(description);

        return getContext();
    }

    static MiniMochaDescription pop() {
        if (_miniMochaContext.size() == 1) {
            return getContext();
        }

        return _miniMochaContext.pop();
    }
}
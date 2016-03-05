package nl.brusque.iou.minimocha;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class MiniMochaRules {
    enum RuleType {
        RunBefore,
        RunAfter
    }

    private final HashMap<RuleType, List<Runnable>> _rules = new HashMap<RuleType, List<Runnable>>() {
        {
            {put(RuleType.RunBefore, new ArrayList<Runnable>()); }
            {put(RuleType.RunAfter, new ArrayList<Runnable>()); }
        }
    };

    public void add(RuleType type, Runnable runnable) {
        List<Runnable> rules = _rules.get(type);

        rules.add(runnable);
    }

    public List<Runnable> get(RuleType type) {
        return _rules.get(type);
    }

}

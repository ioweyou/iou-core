package nl.brusque.iou.minimocha;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.List;

public class MiniMochaRunBefores extends Statement {
    private final Statement fNext;
    private final MiniMochaRules fRules;
    private final List<Runnable> fBefores;

    public MiniMochaRunBefores(Statement next, MiniMochaRules rules) {
        this.fNext    = next;
        this.fRules   = rules;
        this.fBefores = rules.get(MiniMochaRules.RuleType.RunBefore);
    }

    public void evaluate() throws Throwable {
        Method method = Runnable.class.getMethod("run");

        FrameworkMethod frameworkMethod = new FrameworkMethod(method);

        for (Runnable before : this.fBefores) {
            frameworkMethod.invokeExplosively(before);
        }

        this.fNext.evaluate();
    }
}

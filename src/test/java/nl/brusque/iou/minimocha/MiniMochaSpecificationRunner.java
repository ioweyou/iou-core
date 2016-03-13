package nl.brusque.iou.minimocha;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class MiniMochaSpecificationRunner extends BlockJUnit4ClassRunner {
    private final String _descriptionName;
    private final MiniMochaRules _rules;
    private MiniMochaSpecification _specification;
    private Date _start;
    private Date _stop;
    private Description _testDescription;
    private List<FrameworkMethod> _testMethods = new ArrayList<>();

    MiniMochaSpecificationRunner(String descriptionName, MiniMochaRules rules, MiniMochaSpecification specification) throws InitializationError {
        super(specification.getClass());

        _descriptionName = descriptionName;
        _rules           = rules;
        _specification   = specification;
    }

    @Override
    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
        validateNonStaticInnerClassWithDefaultConstructor(errors);
    }

    private void validateNonStaticInnerClassWithDefaultConstructor(List<Throwable> errors) {
        try {
            getTestClass().getJavaClass().getConstructor(MiniMochaSpecificationRunner.this.getTestClass().getJavaClass());
        } catch (NoSuchMethodException e) {
            String reason = "Test classes should be non-static and have a public zero-argument constructor";

            errors.add(new Exception(reason));
        }
    }

    @Override
    protected Object createTest() throws Exception {
        return _specification;
    }

    @Override
    protected Description describeChild(FrameworkMethod method) {
        if (_testDescription == null) {
            _testDescription = Description.createTestDescription(_descriptionName, _specification.getName());
        }

        return _testDescription;
    }

    @Override
    public Description getDescription() {
        return describeChild(null);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        if (_testMethods.size() == 0) {
            try {
                Method method = getTestClass().getJavaClass().getMethod("run");
                method.setAccessible(true);
                FrameworkMethod frameworkMethod = new FrameworkMethod(method);
                _testMethods.add(frameworkMethod);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return _testMethods;
    }

    final void done() {
        _stop = new Date();
    }
    private final ScheduledExecutorService _delayedCallExecutor = Executors.newSingleThreadScheduledExecutor();

    public final void delayedCall(final Runnable runnable, final long milliseconds) {
        _delayedCallExecutor.schedule(runnable, milliseconds, TimeUnit.MILLISECONDS);
    }

    // FIXME withBefores is deprecated, use rules
    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        return new MiniMochaRunBefores(statement,_rules);
    }

    @Override
    public void run(final RunNotifier notifier) {
        _start = new Date();
        MiniMochaSpecificationRunner.super.run(notifier);
        done();
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        // Ignore all regular initalization errors
    }
}
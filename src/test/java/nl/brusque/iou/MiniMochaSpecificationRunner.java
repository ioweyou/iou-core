package nl.brusque.iou;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class MiniMochaSpecificationRunner extends BlockJUnit4ClassRunner {
    private final List<AssertionError> _assertionErrors = new ArrayList<>();
    private final String _descriptionName;
    private MiniMochaSpecification _specification;
    private Date _start;
    private Date _stop;
    private Description _testDescription;
    private List<FrameworkMethod> _testMethods = new ArrayList<>();

    public MiniMochaSpecificationRunner(String descriptionName, MiniMochaSpecification specification) throws InitializationError {
        super(specification.getClass());

        _specification = specification;

        _descriptionName = descriptionName;


    }

    protected void validateConstructor(List<Throwable> errors) {
        validateOnlyOneConstructor(errors);
        validateNonStaticInnerClassWithDefaultConstructor(errors);
    }

    private void validateNonStaticInnerClassWithDefaultConstructor(List<Throwable> errors) {
        try {
            getTestClass().getJavaClass().getConstructor(MiniMochaSpecificationRunner.this.getTestClass().getJavaClass());
        } catch (NoSuchMethodException e) {
            String gripe = "Nested test classes should be non-static and have a public zero-argument constructor";
            errors.add(new Exception(gripe));
        }
    }

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

    public void done() {
        _stop = new Date();

 //       runAssertions();

        System.out.println(String.format("Start %s, Stop %s", _start, _stop));
    }

    private void runAssertions() {
        if (_assertionErrors.size() == 0) {
            return;
        }

        throw _assertionErrors.get(0);
    }

    @Override
    public void run(final RunNotifier notifier) {
        _start = new Date();
        MiniMochaSpecificationRunner.super.run(notifier);
        done();
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
    }
}
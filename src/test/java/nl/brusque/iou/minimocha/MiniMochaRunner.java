package nl.brusque.iou.minimocha;


import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

public class MiniMochaRunner extends Runner {
    private final List<MiniMochaRunner> _mmChildRunners = new ArrayList<>();
    private final Description _suiteDescription;
    private final List<MiniMochaSpecificationRunner> _specificationRunners = new ArrayList<>();

    public MiniMochaRunner(Class<MiniMochaDescription> testClass) throws IllegalAccessException, InstantiationException {
        this(testClass.newInstance());
    }

    public MiniMochaRunner(MiniMochaDescription mmDescription) {
        _suiteDescription = Description.createSuiteDescription(mmDescription.getName());
        for (MiniMochaSpecification specification : mmDescription.getSpecifications()) {
            try {
                MiniMochaSpecificationRunner specificationRunner = new MiniMochaSpecificationRunner(mmDescription.getName(), specification);

                _suiteDescription.addChild(specificationRunner.getDescription());
                _specificationRunners.add(specificationRunner);
            } catch (InitializationError initializationError) {
                initializationError.printStackTrace();
            }
        }

        for (MiniMochaDescription childDescription : mmDescription.getChildDescriptions()) {
            MiniMochaRunner childDescriptionRunner = new MiniMochaRunner(childDescription);

            _suiteDescription.addChild(childDescriptionRunner.getDescription());

            _mmChildRunners.add(childDescriptionRunner);
        }
    }

    @Override
    public Description getDescription() {
        return _suiteDescription;
    }

    @Override
    public void run(RunNotifier notifier) {
        for (MiniMochaSpecificationRunner specificationRunner : _specificationRunners) {
            specificationRunner.run(notifier);
        }

        for (MiniMochaRunner mmChildDescriptionRunner : _mmChildRunners) {
            mmChildDescriptionRunner.run(notifier);
        }
    }
}

package nl.brusque.iou;

import java.util.*;

public class MiniMochaDescription extends MiniMochaNode {
    private MiniMochaDescription _descriptionContext = this;
    private List<MiniMochaDescription> _childDescriptions = new ArrayList<>();
    private List<MiniMochaSpecification> _specifications = new ArrayList<>();

    public MiniMochaDescription() {
        this("MiniMocha");
    }

    public MiniMochaDescription(String name) {
        setName(name);
    }

    public MiniMochaDescription describe(String description, Runnable runnable) {
        MiniMochaDescription mmDescription = new MiniMochaDescription(description);
        MiniMochaDescription oldDescriptionContext = _descriptionContext;
        _descriptionContext = mmDescription;

        _descriptionContext.setName(description);

        runnable.run();
        _descriptionContext = oldDescriptionContext;

        _descriptionContext.addChildDescription(mmDescription);

        return mmDescription;
    }

    private void addChildDescription(MiniMochaDescription mmDescription) {
        _childDescriptions.add(mmDescription);
    }

    public void addSpecification(MiniMochaSpecification specification) {
        _specifications.add(specification);
    }
    public void specify(String description, Runnable test) {
        _descriptionContext.addSpecification(new MiniMochaSpecification(description, test));
    }


    public <TInput> void testFulfilled(final TInput value, final Testable<TInput> test) {
        specify("already-fulfilled", new Runnable() {
            @Override
            public void run() {
                test.setPromise(resolved(value));

                test.run();
            }
        });

        specify("immediately-fulfilled", new Runnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-fulfilled", new Runnable() {
            @Override
            public void run() {
                final AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        d.resolve(value);
                    }
                }, 50);
            }
        });
    }
    public <TInput> void testRejected(final TInput value, final Testable<TInput> test) {
        specify("already-rejected", new Runnable() {
            @Override
            public void run() {
                test.setPromise(rejected(value));

                test.run();
            }
        });

        specify("immediately-rejected", new Runnable() {
            @Override
            public void run() {
                AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();
                d.resolve(value);
            }
        });

        specify("eventually-rejected", new Runnable() {
            @Override
            public void run() {
                final AbstractIOU<TInput> d = deferred();
                test.setPromise(d.getPromise());
                test.run();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        d.reject(value);
                    }
                }, 50);

            }
        });
    }

    public List<MiniMochaDescription> getChildDescriptions() {
        return _childDescriptions;
    }

    public List<MiniMochaSpecification> getSpecifications() {
        return _specifications;
    }
}
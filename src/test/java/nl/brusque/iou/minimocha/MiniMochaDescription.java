package nl.brusque.iou.minimocha;

import java.util.ArrayList;
import java.util.List;

public class MiniMochaDescription extends MiniMochaNode {
    static final MiniMochaDescription ROOT = new MiniMochaDescription("ROOT");

    private List<MiniMochaDescription> _childDescriptions = new ArrayList<>();
    private List<MiniMochaSpecification> _specifications = new ArrayList<>();

    MiniMochaDescription(String description) {
        setName(description);
    }

    public MiniMochaDescription(String description, MiniMochaRunnableNode runnable) {
        this(description);

        MiniMochaDescriptionContext.push(this);

        runnable.run();
    }

    final void addChildDescription(MiniMochaDescription description) {
        _childDescriptions.add(description);
    }

    final void addSpecification(MiniMochaSpecification specification) {
        _specifications.add(specification);
    }

    final List<MiniMochaDescription> getChildDescriptions() {
        return _childDescriptions;
    }

    final List<MiniMochaSpecification> getSpecifications() {
        return _specifications;
    }
}
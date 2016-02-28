package nl.brusque.iou.minimocha;

public abstract class MiniMochaRunnableNode extends MiniMochaNode implements Runnable {
    public final void describe(String description, Runnable runnable) {
        MiniMochaDescriptionContext.push(new MiniMochaDescription(description));

        runnable.run();

        MiniMochaDescription childDescription = MiniMochaDescriptionContext.pop();

        MiniMochaDescriptionContext.getContext()
                .addChildDescription(childDescription);
    }

    public final void specify(String description, MiniMochaSpecificationRunnable test) {
        MiniMochaDescriptionContext.getContext()
                .addSpecification(new MiniMochaSpecification(description, test));
    }
}
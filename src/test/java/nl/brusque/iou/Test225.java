package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.runner.RunWith;

@RunWith(MiniMochaRunner.class)
public class Test225 extends MiniMochaDescription {
    public Test225() {
        super("2.2.5 `onFulfilled` and `onRejected` must be called as functions (i.e. with no `this` value).", new IOUMiniMochaRunnableNode() {
            @Override
            public void run() {
                specify("Unimplemented tests, because they only make sense in a JavaScript-context", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        delayedCall(new Runnable() {
                            @Override
                            public void run() {
                                done();
                            }
                        }, 0);
                    }
                });
            }
        });
    }
}
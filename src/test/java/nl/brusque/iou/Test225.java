package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test225 extends MiniMochaDescription {
    public Test225() {
        super("2.2.5 `onFulfilled` and `onRejected` must be called as functions (i.e. with no `this` value).", new IOUMiniMochaRunnableNode() {
            @Override
            public void run() {
                assertEquals("Unimplemented tests, because they only make sense in a JavaScript-context", true, true);
            }
        });
    }
}
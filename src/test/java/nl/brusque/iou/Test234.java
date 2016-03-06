package nl.brusque.iou;


import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunnableNode;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test234 extends MiniMochaDescription {

    public Test234() {
        super("2.3.4: If `x` is not an object or function, fulfill `promise` with `x`", new IOUMiniMochaRunnableNode() {
            final String dummy = "DUMMY";

            <TAnything> void testValue(final TAnything expectedValue, String stringRepresentation) {
                describe("The value is " + stringRepresentation, new MiniMochaRunnableNode() {
                    @Override
                    public void run() {
                        testFulfilled(dummy, new Testable<String>() {
                            @Override
                            public void run() {
                                IThenable<TAnything> promise2 = getPromise().then(new IThenCallable<String, TAnything>() {
                                    @Override
                                    public TAnything apply(String o) throws Exception {
                                        return expectedValue;
                                    }
                                });

                                promise2.then(new IThenCallable<TAnything, Void>() {

                                    @Override
                                    public Void apply(TAnything actualValue) throws Exception {
                                        assertEquals(expectedValue, actualValue);
                                        done();

                                        return null;
                                    }
                                });
                            }
                        });

                        testRejected(dummy, new Testable<String>() {
                            @Override
                            public void run() {
                                IThenable <TAnything> promise2 = getPromise().then(null, new IThenCallable<Object, TAnything>() {
                                    @Override
                                    public TAnything apply(Object o) throws Exception {
                                        return expectedValue;
                                    }
                                });

                                promise2.then(new IThenCallable<TAnything, Void>() {

                                    @Override
                                    public Void apply(TAnything actualValue) throws Exception {
                                        assertEquals(expectedValue, actualValue);
                                        done();

                                        return null;
                                    }
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void run() {
                testValue(null, "`null`");
                testValue(false, "`false`");
                testValue(true, "`true`");
                testValue(0, "`0`");
             }
        });
    }
}
package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static nl.brusque.iou.Util.deferred;

@RunWith(MiniMochaRunner.class)
public class Test227 extends MiniMochaDescription {
    public Test227() {
        super("2.2.7: `then` must return a promise: `promise2 = promise1.then(onFulfilled, onRejected)`", new IOUMiniMochaRunnableNode() {
            final String dummy     = "DUMMY";
            final String other     = "other";
            final String sentinel  = "sentinel";
            final String sentinel2 = "sentinel2";
            final String sentinel3 = "sentinel3";

            final HashMap<String, Throwable> reasons = new HashMap<String, Throwable>() {{
                put("`null`", null);
                put("an error", new Error());
            }};
            
            @Override
            public void run() {
                specify("is a promise", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        AbstractPromise promise1 = deferred().getPromise();
                        Object promise2 = promise1.then();

                        Assert.assertFalse("AndroidPromise should not be null", promise2 == null);

                        // FIXME Invoking on another thread here to fix synchronization
                        // FIXME Not in original APlus-tests
                        delayedDone(0);
                    }
                });

                describe("2.2.7.2: If either `onFulfilled` or `onRejected` throws an exception `e`, `promise2` must be rejected with `e` as the reason.", new Runnable() {
                    private void testReason(final Throwable expectedReason, String stringRepresentation) {
                        describe(String.format("The reason is %s", stringRepresentation), new Runnable() {

                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable<String>() {
                                    @Override
                                    public void run() {
                                        final AbstractPromise<String> promise1 = getPromise();

                                        IThenable<String> promise2 = promise1.then(new TestThenCallable<String, String>() {
                                            @Override
                                            public String apply(String o) throws Exception {
                                                throw new Exception(expectedReason);
                                            }
                                        });

                                        promise2.then(null, new TestThenCallable<Object, Void>() {
                                            @Override
                                            public Void apply(Object o) throws Exception {
                                                Throwable e = o!=null ? ((Exception)o).getCause() : null;

                                                Assert.assertEquals("Incorrect reason", expectedReason, e);
                                                done();

                                                return null;
                                            }
                                        });
                                    }
                                });

                                testRejected(dummy, new Testable<String>() {
                                    @Override
                                    public void run() {
                                        AbstractPromise<String> promise1 = getPromise();

                                        IThenable<String> promise2 = promise1.then(null, new TestThenCallable<String, String>() {
                                            @Override
                                            public String apply(String o) throws Exception {
                                                throw new Exception(expectedReason);
                                            }
                                        });

                                        promise2.then(null, new TestThenCallable<Object, Void>() {
                                            @Override
                                            public Void apply(Object o) throws Exception {
                                                Throwable e = o!=null ? ((Exception)o).getCause() : null;

                                                Assert.assertEquals("Incorrect reason", expectedReason, e);
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
                        for (String stringRepresentation : reasons.keySet()) {
                            testReason(reasons.get(stringRepresentation), stringRepresentation);
                        }
                    }
                });

                describe("2.2.7.3: If `onFulfilled` is not a function and `promise1` is fulfilled, `promise2` must be fulfilled with the same value.", new Runnable() {
                    private <TAnything, TNonFunction> void testNonFunction(final TNonFunction nonFunction, final String stringRepresentation) {
                        describe(String.format("`onFulfilled` is %s", stringRepresentation), new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                if (nonFunction != null) {
                                    handleNonSensicalTest(stringRepresentation);
                                    return;
                                }

                                testFulfilled(dummy, new Testable<String>() {
                                    @Override
                                    public void run() {
                                        final AbstractPromise<String> promise1 = getPromise();

                                        IThenable<TAnything> promise2 = promise1.then((IThenCallable<String, TAnything>)nonFunction);

                                        promise2.then(new TestThenCallable<TAnything, Void>() {
                                            @Override
                                            public Void apply(TAnything o) throws Exception {
                                                Assert.assertEquals(o, dummy);

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
                        testNonFunction(null, "`null`");
                        testNonFunction(false, "`false`");
                        testNonFunction(5, "`5`");
                        testNonFunction(new Object(), "an object");
                    }
                });

                describe("2.2.7.4: If `onRejected` is not a function and `promise1` is rejected, `promise2` must be rejected with the same reason.", new Runnable() {
                    private <TAnything> void testNonFunction(final TAnything nonFunction, final String stringRepresentation) {
                        describe(String.format("`onRejected` is %s", stringRepresentation), new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                if (nonFunction != null) {
                                    handleNonSensicalTest(stringRepresentation);
                                    return;
                                }

                                testRejected(dummy, new Testable<String>() {
                                    @Override
                                    public void run() {
                                        final AbstractPromise<String> promise1 = getPromise();

                                        IThenable<TAnything> promise2 = promise1.then((IThenCallable<String, TAnything>)nonFunction);

                                        promise2.then(null, new TestThenCallable<TAnything, Void>() {
                                            @Override
                                            public Void apply(TAnything o) throws Exception {
                                                Assert.assertEquals(o, dummy);

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
                        testNonFunction(null, "`null`");
                        testNonFunction(false, "`false`");
                        testNonFunction(5, "`5`");
                        testNonFunction(new Object(), "an object");
                    }
                });
            }
        });
    }
}
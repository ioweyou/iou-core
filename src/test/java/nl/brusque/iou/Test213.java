package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.deferred;
import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test213 extends MiniMochaDescription {
    public Test213() {
        super("2.1.3.1: When rejected, a promise: must not transition to any other state.", new IOUMiniMochaRunnableNode() {
            @Override
            public void run() {
                final String dummy = "DUMMY";
                testRejected(dummy, new Testable<Object>() {
                    final boolean[] onRejectedCalled = {false};


                    @Override
                    public void run() {

                        AbstractPromise<Object> promise = getPromise();

                        promise.then(new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) {
                                assertEquals("onRejected should not have been called", onRejectedCalled[0], false);

                                done();

                                return null;
                            }
                        }, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) throws Exception {
                                onRejectedCalled[0] = true;

                                return null;
                            }
                        });

                        delayedDone(100);
                    }
                });

                specify("trying to reject then immediately fulfill", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {

                            @Override
                            public Void apply(String o) {
                                assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return null;
                            }
                        }, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) {
                                onRejectedCalled[0] = true;

                                return null;
                            }
                        });

                        d.reject(dummy);
                        d.resolve(dummy);
                        delayedDone(100);
                    }
                });

                specify("trying to reject then fulfill, delayed", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final AbstractIOU<String> d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {

                            @Override
                            public Void apply(String o) {
                                assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return null;
                            }
                        }, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) {
                                onRejectedCalled[0] = true;

                                return null;
                            }
                        });


                        delayedCall(new Runnable() {
                            @Override
                            public void run() {
                                d.reject(dummy);
                                d.resolve(dummy);
                            }
                        }, 50);

                        delayedDone(100);
                    }
                });

                specify("trying to reject immediately then fulfill delayed", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final AbstractIOU<String> d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {

                            @Override
                            public Void apply(String o) {
                                assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return null;
                            }
                        }, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) {
                                onRejectedCalled[0] = true;

                                return null;
                            }
                        });


                        d.reject(dummy);
                        delayedCall(new Runnable() {
                            @Override
                            public void run() {
                                d.resolve(dummy);
                            }
                        }, 50);

                        delayedDone(100);
                    }
                });
            }
        });
    }
}
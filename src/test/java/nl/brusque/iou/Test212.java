package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.deferred;
import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test212 extends MiniMochaDescription {
    public Test212() {
        super("2.1.2.1: When fulfilled, a promise: must not transition to any other state", new IOUMiniMochaRunnableNode() {

            @Override
            public void run() {
                final String dummy = "DUMMY";
                testFulfilled(dummy, new Testable<Object>() {
                    final boolean[] onFulfilledCalled = {false};


                    @Override
                    public void run() {

                        AbstractPromise<Object> promise = getPromise();

                        promise.then(new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) {
                                onFulfilledCalled[0] = true;

                                return null;
                            }
                        }, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) throws Exception {
                                assertEquals("OnFulfilled should not have been called", onFulfilledCalled[0], false);

                                done();

                                return null;
                            }
                        });

                        delayedDone(100);
                    }
                });

                specify("trying to call then immediately reject", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        d.resolve(dummy);
                        d.reject(dummy);
                        delayedDone(100);
                    }
                });

                specify("trying to fulfill then reject, delayed", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);
                                done();
                                return o;
                            }
                        });

                        d.resolve(dummy);
                        d.reject(dummy);

                        delayedDone(100);
                    }
                });

                specify("trying to fulfill immediately then reject delayed", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final AbstractIOU<String> d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                assertEquals("onRejected should not have been called", true, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        delayedCall(new Runnable() {
                            @Override
                            public void run() {
                                d.resolve(dummy);
                                d.reject(dummy);
                            }
                        }, 50);


                        delayedDone(100);
                    }
                });
            }
        });
    }
}
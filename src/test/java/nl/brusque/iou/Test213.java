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

                specify("trying to reject then immediately call", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                onRejectedCalled[0] = true;

                                return o;
                            }
                        });

                        d.reject(dummy);
                        d.resolve(dummy);
                        delayedDone(100);
                    }
                });

                specify("trying to fireRejectables then call, delayed", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final AbstractIOU<String> d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                onRejectedCalled[0] = true;

                                return o;
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

                specify("trying to fireRejectables immediately then call delayed", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final AbstractIOU<String> d = deferred();

                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                assertEquals("onFulfilled should not have been called", false, onRejectedCalled[0]);

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                onRejectedCalled[0] = true;

                                return o;
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
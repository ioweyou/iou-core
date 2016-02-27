package nl.brusque.iou;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test213 extends MiniMochaDescription {
    public Test213() {
        describe("2.1.3.1: When rejected, a promise: must not transition to any other state.", new Runnable() {
            @Override
            public void run() {
                final String dummy = "DUMMY";

                specify("trying to reject then immediately call", new Runnable() {
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
                        delay(100);
                    }
                });

                specify("trying to fireRejectables then call, delayed", new Runnable() {
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

                        delay(50);
                        d.reject(dummy);
                        d.resolve(dummy);
                        delay(100);
                    }
                });

                specify("trying to fireRejectables immediately then call delayed", new Runnable() {
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
                        delay(50);
                        d.resolve(dummy);
                        delay(100);
                    }
                });
            }
        });
    }
}
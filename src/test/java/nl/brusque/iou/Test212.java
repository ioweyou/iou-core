package nl.brusque.iou;

import org.junit.Assert;
import org.junit.Test;

public class Test212 extends TestBase {
    @Test
    public void test2121WhenFulfilledAPromiseMustNotTransitionToAnyOtherState() {
        describe("2.1.2.1: When fulfilled, a promise: must not transition to any other state.", new Runnable() {

            @Override
            public void run() {
                final String dummy = "DUMMY";

                specify("trying to apply then immediately reject", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable() {

                            @Override
                            public Object apply(Object o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable() {
                            @Override
                            public Object apply(Object o) {
                                Assert.assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        d.resolve(dummy);
                        d.reject(dummy);
                        delay(100);
                    }
                });

                specify("trying to apply then reject, delayed", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable() {

                            @Override
                            public Object apply(Object o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable() {
                            @Override
                            public Object apply(Object o) {
                                Assert.assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        delay(50);
                        d.resolve(dummy);
                        d.reject(dummy);

                        delay(100);
                    }
                });

                specify("trying to apply immediately then reject delayed", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable() {

                            @Override
                            public Object apply(Object o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable() {
                            @Override
                            public Object apply(Object o) {
                                Assert.assertEquals("onRejected should not have been called", true, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        d.resolve(dummy);
                        delay(50);
                        d.reject(dummy);

                        delay(100);
                    }
                });
            }
        });
    }
}
package nl.brusque.iou;

import nl.brusque.iou.helper.TestFulfillable;
import nl.brusque.iou.helper.PromiseTest;
import nl.brusque.iou.helper.TestRejectable;
import org.junit.Assert;
import org.junit.Test;

public class Test212 extends PromiseTest {
    @Test
    public void test2121WhenFulfilledAPromiseMustNotTransitionToAnyOtherState() {
        describe("2.1.2.1: When fulfilled, a promise: must not transition to any other state.", new Runnable() {

            @Override
            public void run() {
                final String dummy = "DUMMY";

                specify("trying to fulfill then immediately reject", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestFulfillable() {

                            @Override
                            public Object fulfill(Object o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestRejectable() {
                            @Override
                            public Object reject(Object o) {
                                Assert.assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        d.resolve(dummy);
                        d.reject(dummy);
                        delay(100);
                    }
                });

                specify("trying to fulfill then reject, delayed", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestFulfillable() {

                            @Override
                            public Object fulfill(Object o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestRejectable() {
                            @Override
                            public Object reject(Object o) {
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

                specify("trying to fulfill immediately then reject delayed", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestFulfillable() {

                            @Override
                            public Object fulfill(Object o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestRejectable() {
                            @Override
                            public Object reject(Object o) {
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
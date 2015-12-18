package nl.brusque.iou;

import nl.brusque.iou.helper.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

public class Test226 extends PromiseTest {
    @Test
    public void test226ThenMayBeCalledMultipleTimesOnTheSamePromise() {
        describe("2.2.6: `then` may be called multiple times on the same promise.", new Runnable() {
            final String dummy     = "DUMMY";
            final String other     = "other";
            final String sentinel  = "sentinel";
            final String sentinel2 = "sentinel2";
            final String sentinel3 = "sentinel3";

            @Override
            public void run() {
                describe("2.2.6.1: If/when `promise` is fulfilled, all respective `onFulfilled` callbacks must execute in the order of their originating calls to `then`.", new Runnable() {
                    @Override
                    public void run() {
                        describe("multiple boring fulfillment handlers", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(sentinel, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = fulfillableStub().returns(other);
                                        final ISpy handler2 = fulfillableStub().returns(other);
                                        final ISpy handler3 = fulfillableStub().returns(other);
                                        final ISpy spy      = rejectableStub();

                                        IPromise promise = getPromise();
                                        promise.then(handler1, spy);
                                        promise.then(handler2, spy);
                                        promise.then(handler3, spy);

                                        promise.then(new TestFulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        describe("multiple fulfillment handlers, one of which throws", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(sentinel, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = fulfillableStub().returns(other);
                                        final ISpy handler2 = fulfillableStub().throwsError();
                                        final ISpy handler3 = fulfillableStub().returns(other);
                                        final ISpy spy      = rejectableStub();

                                        IPromise promise = getPromise();
                                        promise.then(handler1, spy);
                                        promise.then(handler2, spy);
                                        promise.then(handler3, spy);

                                        promise.then(new TestFulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        describe("results in multiple branching chains with their own fulfillment values", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        IPromise promise = getPromise();

                                        promise
                                            .then(new TestFulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    return sentinel;
                                                }
                                            }).then(new TestFulfillable() {
                                            @Override
                                            public Object fulfill(Object o) {
                                                Assert.assertEquals("Object should equal sentinel", sentinel, o);

                                                return null;
                                            }
                                        });

                                        promise
                                            .then(new TestFulfillable() {
                                                @Override
                                                public Object fulfill(Object o) throws Exception {
                                                    throw new Exception(sentinel2);
                                                }
                                            })
                                            .then(null, new TestRejectable() {
                                                @Override
                                                public Object reject(Object o) throws Exception {
                                                    Exception e = (Exception)o; // :')
                                                    Assert.assertEquals("Object should equal sentinel2", sentinel2, e.getMessage());

                                                    return null;
                                                }
                                            });

                                        promise
                                            .then(new TestFulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    return sentinel3;
                                                }
                                            })
                                            .then(new TestFulfillable() {
                                                @Override
                                                public Object fulfill(Object o) {
                                                    Assert.assertEquals("Object should equal sentinel3", sentinel3, o);

                                                    return null;
                                                }
                                            });

                                    }
                                });
                            }
                        });

                        describe("`onFulfilled` handlers are called in the original order", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = new FulfillableSpy().returns("DUMMYA");
                                        final ISpy handler2 = new FulfillableSpy().returns("DUMMYB");
                                        final ISpy handler3 = new FulfillableSpy().returns("DUMMYC");

                                        IPromise promise = getPromise();
                                        promise.then(handler1);
                                        promise.then(handler2);
                                        promise.then(handler3);

                                        promise.then(new TestFulfillable() {
                                            @Override
                                            public Object fulfill(Object o) throws Exception {
                                                boolean a = handler1.lastCall() < handler2.lastCall();
                                                boolean b = handler2.lastCall() < handler3.lastCall();
                                                boolean c = a && b;

                                                Assert.assertTrue("Handlers called in incorrect order", c);

                                                return null;
                                            }
                                        });
                                    }
                                });

                            describe("even when one handler is added inside another handler", new Runnable() { // FIXME This is not working
                                @Override
                                public void run() {
                                    testFulfilled(dummy, new Testable() {
                                        @Override
                                        public void run() {
                                            final ISpy handler1 = new FulfillableSpy().name("handler1").returns(dummy);
                                            final ISpy handler2 = new FulfillableSpy().name("handler2").returns(dummy);
                                            final ISpy handler3 = new FulfillableSpy().name("handler3").returns(dummy);

                                            final IPromise promise = getPromise();
                                            promise.then(new TestFulfillable() {
                                                @Override
                                                public Object fulfill(Object o) throws Exception {
                                                    handler1.call(o);

                                                    promise.then(handler3);

                                                    return dummy;
                                                }
                                            });
                                            promise.then(handler2);

                                            promise.then(new TestFulfillable() {
                                                @Override
                                                public Object fulfill(Object o) throws Exception {
                                                    new Timer().schedule(new TimerTask() {
                                                        @Override
                                                        public void run() {
                                                            boolean a = handler1.lastCall() < handler2.lastCall();
                                                            boolean b = handler2.lastCall() < handler3.lastCall();
                                                            boolean c = a && b;

                                                            Assert.assertTrue("Handlers called in incorrect order", c);
                                                        }
                                                    }, 50);

                                                    return null;
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            }
                        });
                    }
                });

                describe("2.2.6.2: If/when `promise` is rejected, all respective `onRejected` callbacks must execute in the order of their originating calls to `then`.", new Runnable() {
                    @Override
                    public void run() {
                        describe("multiple boring rejection handlers", new Runnable() {
                            @Override
                            public void run() {
                                testRejected(sentinel, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = rejectableStub().returns(other);
                                        final ISpy handler2 = rejectableStub().returns(other);
                                        final ISpy handler3 = rejectableStub().returns(other);
                                        final ISpy spy      = fulfillableStub();

                                        IPromise promise = getPromise();
                                        promise.then(spy, handler1);
                                        promise.then(spy, handler2);
                                        promise.then(spy, handler3);

                                        promise.then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) {
                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        describe("multiple rejection handlers, one of which throws", new Runnable() {
                            @Override
                            public void run() {
                                testRejected(sentinel, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = rejectableStub().returns(other);
                                        final ISpy handler2 = rejectableStub().throwsError();
                                        final ISpy handler3 = rejectableStub().returns(other);
                                        final ISpy spy = fulfillableStub();

                                        IPromise promise = getPromise();
                                        promise.then(spy, handler1);
                                        promise.then(spy, handler2);
                                        promise.then(spy, handler3);

                                        promise.then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) {
                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        describe("results in multiple branching chains with their own fulfillment values", new Runnable() {
                            @Override
                            public void run() {
                                testRejected(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        IPromise promise = getPromise();

                                        promise
                                                .then(null, new TestRejectable() {
                                                    @Override
                                                    public Object reject(Object o) {
                                                        return sentinel;
                                                    }
                                                }).then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) {
                                                Assert.assertEquals("Object should equal sentinel", sentinel, o);

                                                return null;
                                            }
                                        });

                                        promise
                                                .then(null, new TestRejectable() {
                                                    @Override
                                                    public Object reject(Object o) throws Exception {
                                                        throw new Exception(sentinel2);
                                                    }
                                                })
                                                .then(null, new TestRejectable() {
                                                    @Override
                                                    public Object reject(Object o) throws Exception {
                                                        Exception e = (Exception) o; // :')
                                                        Assert.assertEquals("Object should equal sentinel2", sentinel2, e.getMessage());

                                                        return null;
                                                    }
                                                });

                                        promise
                                                .then(null, new TestRejectable() {
                                                    @Override
                                                    public Object reject(Object o) {
                                                        return sentinel3;
                                                    }
                                                })
                                                .then(new TestFulfillable() {
                                                    @Override
                                                    public Object fulfill(Object o) {
                                                        Assert.assertEquals("Object should equal sentinel3", sentinel3, o);

                                                        return null;
                                                    }
                                                });

                                    }
                                });
                            }
                        });

                        describe("`onRejected` handlers are called in the original order", new Runnable() {
                            @Override
                            public void run() {
                                testRejected(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        final ISpy handler1 = new RejectableSpy().returns("DUMMYA");
                                        final ISpy handler2 = new RejectableSpy().returns("DUMMYB");
                                        final ISpy handler3 = new RejectableSpy().returns("DUMMYC");

                                        IPromise promise = getPromise();
                                        promise.then(null, handler1);
                                        promise.then(null, handler2);
                                        promise.then(null, handler3);

                                        promise.then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) throws Exception {
                                                boolean a = handler1.lastCall() < handler2.lastCall();
                                                boolean b = handler2.lastCall() < handler3.lastCall();
                                                boolean c = a && b;

                                                Assert.assertTrue("Handlers called in incorrect order", c);

                                                return null;
                                            }
                                        });
                                    }
                                });

                                describe("even when one handler is added inside another handler", new Runnable() { // FIXME This is not working
                                    @Override
                                    public void run() {
                                        testRejected(dummy, new Testable() {
                                            @Override
                                            public void run() {
                                                final ISpy handler1 = new RejectableSpy().name("handler1").returns(dummy);
                                                final ISpy handler2 = new RejectableSpy().name("handler2").returns(dummy);
                                                final ISpy handler3 = new RejectableSpy().name("handler3").returns(dummy);

                                                final IPromise promise = getPromise();
                                                promise.then(null, new RejectableSpy() {
                                                    @Override
                                                    public Object reject(Object o) throws Exception {
                                                        handler1.call(o);

                                                        promise.then(null, handler3);

                                                        return dummy;
                                                    }
                                                });
                                                promise.then(null, handler2);

                                                promise.then(null, new RejectableSpy() {
                                                    @Override
                                                    public Object reject(Object o) throws Exception {
                                                        new Timer().schedule(new TimerTask() {
                                                            @Override
                                                            public void run() {
                                                                boolean a = handler1.lastCall() < handler2.lastCall();
                                                                boolean b = handler2.lastCall() < handler3.lastCall();
                                                                boolean c = a && b;

                                                                Assert.assertTrue("Handlers called in incorrect order", c);
                                                            }
                                                        }, 50);

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        delay(5000);

    }
}
package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Matchers;

import static nl.brusque.iou.Util.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MiniMochaRunner.class)
public class Test226 extends MiniMochaDescription {
    public Test226() {
        super("2.2.6: `then` may be called multiple times on the same promise.", new IOUMiniMochaRunnableNode() {
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
                                testFulfilled(sentinel, new Testable<Object>() {
                                    @Override
                                    public void run() {
                                        try {
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();

                                            final IThenCallable<Object, String> spy = rejectableStub();
                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenReturn(other);
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = getPromise();
                                            promise.then(handler1, spy);
                                            promise.then(handler2, spy);
                                            promise.then(handler3, spy);

                                            promise.then(new TestThenCallable<Object, Void>() {
                                                @Override
                                                public Void apply(Object o) {
                                                    Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                    try {
                                                        verify(handler1, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler2, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler3, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(spy, never()).apply(any(Object.class));

                                                        done();
                                                    } catch (Exception e) {
                                                        throw new AssertionError(e);
                                                    }


                                                    //Assert.assertEquals("Handler1 not called with sentinel", sentinel, spy(handler1));
                                                    //Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
                                                    //Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
                                                    //Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                    return null;
                                                }
                                            });
                                        } catch (Exception e) {

                                        }
                                    }
                                });
                            }
                        });

                        describe("multiple fulfillment handlers, one of which throws", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(sentinel, new Testable<Object>() {
                                    @Override
                                    public void run() {
                                        try {
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();

                                            final IThenCallable<Object, String> spy = rejectableStub();
                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenThrow(new Exception());
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = getPromise();
                                            promise.then(handler1, spy);
                                            promise.then(handler2, spy);
                                            promise.then(handler3, spy);

                                            promise.then(new TestThenCallable<Object, Void>() {
                                                @Override
                                                public Void apply(Object o) {
                                                    Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                    try {
                                                        verify(handler1, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler2, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler3, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(spy, never()).apply(any(Object.class));

                                                        done();
                                                    } catch (Exception e) {
                                                        throw new AssertionError(e);
                                                    }


//                                                    Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
//                                                    Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
//                                                    Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
//                                                    Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());

                                                    return null;
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });

                        describe("results in multiple branching chains with their own fulfillment values", new Runnable() {
                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable<Object>() {
                                    @Override
                                    public void run() {
                                        AbstractPromise<Object> promise = getPromise();

                                        promise
                                            .then(new TestThenCallable<String, Object>() {
                                                @Override
                                                public Object apply(String o) {
                                                    return sentinel;
                                                }
                                            }).then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                Assert.assertEquals("Object should equal sentinel", sentinel, o);

                                                return null;
                                            }
                                        });

                                        promise
                                            .then(new TestThenCallable<String, Object>() {
                                                @Override
                                                public Object apply(String o) throws Exception {
                                                    throw new Exception(sentinel2);
                                                }
                                            })
                                            .then(null, new TestThenCallable<Object, Void>() {
                                                @Override
                                                public Void apply(Object o) throws Exception {
                                                    Exception e = (Exception)o; // :')
                                                    Assert.assertEquals("Object should equal sentinel2", sentinel2, e.getMessage());

                                                    return null;
                                                }
                                            });

                                        promise
                                            .then(new TestThenCallable<String, String>() {
                                                @Override
                                                public String apply(String o) {
                                                    return sentinel3;
                                                }
                                            })
                                            .then(new TestThenCallable<String, Void>() {
                                                @Override
                                                public Void apply(String o) {
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
                                testFulfilled(dummy, new Testable<Object>() {
                                    @Override
                                    public void run() {
                                        try {
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();
                                            final InOrder inOrder = inOrder(handler1, handler2, handler3);

                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenReturn(other);
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = getPromise();
                                            promise.then(handler1);
                                            promise.then(handler2);
                                            promise.then(handler3);



                                            promise.then(new TestThenCallable<String, Void>() {
                                                @Override
                                                public Void apply(String o) throws Exception {
//                                                    boolean a = handler1.lastCall() < handler2.lastCall();
//                                                    boolean b = handler2.lastCall() < handler3.lastCall();
//                                                    boolean c = a && b;
//
//                                                    Assert.assertTrue("Handlers called in incorrect order", c);
                                                    inOrder.verify(handler1).apply(any(Object.class));
                                                    inOrder.verify(handler2).apply(any(Object.class));
                                                    inOrder.verify(handler3).apply(any(Object.class));
                                                    done();
                                                    return null;
                                                }
                                            });
                                        } catch (Exception e) {
                                            throw new AssertionError(e);
                                        }
                                    }
                                });

                            describe("even when one handler is added inside another handler", new Runnable() { // FIXME This is not working
                                @Override
                                public void run() {
                                    testFulfilled(dummy, new Testable<Object>() {
                                        @Override
                                        public void run() {
                                            try {
                                                final IThenCallable<Object, String> handler1 = fulfillableStub();
                                                final IThenCallable<Object, String> handler2 = fulfillableStub();
                                                final IThenCallable<Object, String> handler3 = fulfillableStub();
                                                final InOrder inOrder = inOrder(handler1, handler2, handler3);

                                                when(handler1.apply(any(Object.class))).thenReturn(dummy);
                                                when(handler2.apply(any(Object.class))).thenReturn(dummy);
                                                when(handler3.apply(any(Object.class))).thenReturn(dummy);

                                                final AbstractPromise<Object> promise = getPromise();
                                                promise.then(new TestThenCallable<String, String>() {
                                                    @Override
                                                    public String apply(String o) throws Exception {
                                                        handler1.apply(o);

                                                        promise.then(handler3);

                                                        return dummy;
                                                    }
                                                });
                                                promise.then(handler2);


                                                promise.then(new TestThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String o) throws Exception {
                                                        delayedCall(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    inOrder.verify(handler1).apply(any(Object.class));
                                                                    inOrder.verify(handler2).apply(any(Object.class));
                                                                    inOrder.verify(handler3).apply(any(Object.class));

                                                                    done();
                                                                } catch (Exception e) {
                                                                    throw new AssertionError(e);
                                                                }
//                                                                boolean a = handler1.lastCall() < handler2.lastCall();
//                                                                boolean b = handler2.lastCall() < handler3.lastCall();
//                                                                boolean c = a && b;
//
//                                                                Assert.assertTrue("Handlers called in incorrect order", c);
                                                            }
                                                        }, 15);

                                                        return null;
                                                    }
                                                });
                                            } catch (Exception e) {
                                                throw new AssertionError(e);
                                            }
                                        }
                                    });
                                }
                            });
                            }
                        });
                    }
                });
//
//                describe("2.2.6.2: If/when `promise` is rejected, all respective `onRejected` callbacks must execute in the order of their originating calls to `then`.", new Runnable() {
//                    @Override
//                    public void run() {
//                        describe("multiple boring rejection handlers", new Runnable() {
//                            @Override
//                            public void run() {
//                                testRejected(sentinel, new Testable() {
//                                    @Override
//                                    public void run() {
//                                        final ISpy handler1 = rejectableStub().returns(other);
//                                        final ISpy handler2 = rejectableStub().returns(other);
//                                        final ISpy handler3 = rejectableStub().returns(other);
//                                        final ISpy spy      = fulfillableStub();
//
//                                        AbstractPromise promise = getPromise();
//                                        promise.then(spy, handler1);
//                                        promise.then(spy, handler2);
//                                        promise.then(spy, handler3);
//
//                                        promise.then(null, new TestRejectable() {
//                                            @Override
//                                            public Object fireRejectables(Object o) {
//                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);
//
//                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
//                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
//                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
//                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());
//
//                                                return null;
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        });
//
//                        describe("multiple rejection handlers, one of which throws", new Runnable() {
//                            @Override
//                            public void run() {
//                                testRejected(sentinel, new Testable() {
//                                    @Override
//                                    public void run() {
//                                        final ISpy handler1 = rejectableStub().returns(other);
//                                        final ISpy handler2 = rejectableStub().throwsError();
//                                        final ISpy handler3 = rejectableStub().returns(other);
//                                        final ISpy spy = fulfillableStub();
//
//                                        AbstractPromise promise = getPromise();
//                                        promise.then(spy, handler1);
//                                        promise.then(spy, handler2);
//                                        promise.then(spy, handler3);
//
//                                        promise.then(null, new TestRejectable() {
//                                            @Override
//                                            public Object fireRejectables(Object o) {
//                                                Assert.assertEquals("Value should equal sentinel", sentinel, o);
//
//                                                Assert.assertEquals("Handler1 not called with sentinel", sentinel, handler1.getCalledWith());
//                                                Assert.assertEquals("Handler2 not called with sentinel", sentinel, handler2.getCalledWith());
//                                                Assert.assertEquals("Handler3 not called with sentinel", sentinel, handler3.getCalledWith());
//                                                Assert.assertEquals("Rejected spy should not have been called", 0, spy.getCallCount());
//
//                                                return null;
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        });
//
//                        describe("results in multiple branching chains with their own fulfillment values", new Runnable() {
//                            @Override
//                            public void run() {
//                                testRejected(dummy, new Testable() {
//                                    @Override
//                                    public void run() {
//                                        AbstractPromise promise = getPromise();
//
//                                        promise
//                                                .then(null, new TestRejectable() {
//                                                    @Override
//                                                    public Object fireRejectables(Object o) {
//                                                        return sentinel;
//                                                    }
//                                                }).then(null, new TestRejectable() {
//                                            @Override
//                                            public Object fireRejectables(Object o) {
//                                                Assert.assertEquals("Object should equal sentinel", sentinel, o);
//
//                                                return null;
//                                            }
//                                        });
//
//                                        promise
//                                                .then(null, new TestRejectable() {
//                                                    @Override
//                                                    public Object fireRejectables(Object o) throws Exception {
//                                                        throw new Exception(sentinel2);
//                                                    }
//                                                })
//                                                .then(null, new TestRejectable() {
//                                                    @Override
//                                                    public Object fireRejectables(Object o) throws Exception {
//                                                        Exception e = (Exception) o; // :')
//                                                        Assert.assertEquals("Object should equal sentinel2", sentinel2, e.getMessage());
//
//                                                        return null;
//                                                    }
//                                                });
//
//                                        promise
//                                                .then(null, new TestRejectable() {
//                                                    @Override
//                                                    public Object fireRejectables(Object o) {
//                                                        return sentinel3;
//                                                    }
//                                                })
//                                                .then(new TestThenCallable() {
//                                                    @Override
//                                                    public Object call(Object o) {
//                                                        Assert.assertEquals("Object should equal sentinel3", sentinel3, o);
//
//                                                        return null;
//                                                    }
//                                                });
//
//                                    }
//                                });
//                            }
//                        });
//
//                        describe("`onRejected` handlers are called in the original order", new Runnable() {
//                            @Override
//                            public void run() {
//                                testRejected(dummy, new Testable() {
//                                    @Override
//                                    public void run() {
//                                        final ISpy handler1 = new RejectableSpy().returns("DUMMYA");
//                                        final ISpy handler2 = new RejectableSpy().returns("DUMMYB");
//                                        final ISpy handler3 = new RejectableSpy().returns("DUMMYC");
//
//                                        AbstractPromise promise = getPromise();
//                                        promise.then(null, handler1);
//                                        promise.then(null, handler2);
//                                        promise.then(null, handler3);
//
//                                        promise.then(null, new TestRejectable() {
//                                            @Override
//                                            public Object fireRejectables(Object o) throws Exception {
//                                                boolean a = handler1.lastCall() < handler2.lastCall();
//                                                boolean b = handler2.lastCall() < handler3.lastCall();
//                                                boolean c = a && b;
//
//                                                Assert.assertTrue("Handlers called in incorrect order", c);
//
//                                                return null;
//                                            }
//                                        });
//                                    }
//                                });
//
//                                describe("even when one handler is added inside another handler", new Runnable() { // FIXME This is not working
//                                    @Override
//                                    public void run() {
//                                        testRejected(dummy, new Testable() {
//                                            @Override
//                                            public void run() {
//                                                final ISpy handler1 = new RejectableSpy().name("handler1").returns(dummy);
//                                                final ISpy handler2 = new RejectableSpy().name("handler2").returns(dummy);
//                                                final ISpy handler3 = new RejectableSpy().name("handler3").returns(dummy);
//
//                                                final AbstractPromise promise = getPromise();
//                                                promise.then(null, new RejectableSpy() {
//                                                    @Override
//                                                    public Object fireRejectables(Object o) throws Exception {
//                                                        handler1.call(o);
//
//                                                        promise.then(null, handler3);
//
//                                                        return dummy;
//                                                    }
//                                                });
//                                                promise.then(null, handler2);
//
//                                                promise.then(null, new RejectableSpy() {
//                                                    @Override
//                                                    public Object fireRejectables(Object o) throws Exception {
//                                                        new Timer().schedule(new TimerTask() {
//                                                            @Override
//                                                            public void run() {
//                                                                boolean a = handler1.lastCall() < handler2.lastCall();
//                                                                boolean b = handler2.lastCall() < handler3.lastCall();
//                                                                boolean c = a && b;
//
//                                                                Assert.assertTrue("Handlers called in incorrect order", c);
//                                                            }
//                                                        }, 50);
//
//                                                        return null;
//                                                    }
//                                                });
//                                            }
//                                        });
//                                    }
//                                });
//                            }
//                        });
//                    }
//                });
            }
        });

        delay(500);

    }
}
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
                                    public void run(final TestableParameters parameters) {
                                        try {
                                            allowMainThreadToFinish();
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();

                                            final IThenCallable<Object, String> spy = rejectableStub();
                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenReturn(other);
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = parameters.getPromise();
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

                                                        parameters.done();
                                                    } catch (Exception e) {
                                                        throw new AssertionError(e);
                                                    }

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
                                    public void run(final TestableParameters parameters) {
                                        try {
                                            allowMainThreadToFinish();
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();

                                            final IThenCallable<Object, String> spy = rejectableStub();
                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenThrow(new Exception());
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = parameters.getPromise();
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

                                                        parameters.done();
                                                    } catch (Exception e) {
                                                        throw new AssertionError(e);
                                                    }

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
                                testFulfilled(dummy, new Testable<String>() {
                                    @Override
                                    public void run(final TestableParameters parameters) {
                                        allowMainThreadToFinish();
                                        AbstractPromise<String> promise = parameters.getPromise();
                                        final CallbackAggregator semiDone = new CallbackAggregator(3, parameters);

                                        promise
                                            .then(new TestThenCallable<String, String>() {
                                                @Override
                                                public String apply(String o) {
                                                    return sentinel;
                                                }
                                            }).then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                Assert.assertEquals("Object should equal sentinel", sentinel, o);
                                                semiDone.done();

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
                                                    semiDone.done();

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
                                                    semiDone.done();

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
                                    public void run(final TestableParameters parameters) {
                                        try {
                                            allowMainThreadToFinish();
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();
                                            final InOrder inOrder = inOrder(handler1, handler2, handler3);

                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenReturn(other);
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = parameters.getPromise();
                                            promise.then(handler1);
                                            promise.then(handler2);
                                            promise.then(handler3);

                                            promise.then(new TestThenCallable<Object, String>() {
                                                @Override
                                                public String apply(Object o) throws Exception {
                                                    inOrder.verify(handler1).apply(any(Object.class));
                                                    inOrder.verify(handler2).apply(any(Object.class));
                                                    inOrder.verify(handler3).apply(any(Object.class));
                                                    parameters.done();
                                                    return null;
                                                }
                                            });
                                        } catch (Exception e) {
                                            throw new AssertionError(e);
                                        }
                                    }
                                });

                            describe("even when one handler is added inside another handler", new Runnable() {
                                @Override
                                public void run() {
                                    testFulfilled(dummy, new Testable<Object>() {
                                        @Override
                                        public void run(final TestableParameters parameters) {
                                            try {
                                                allowMainThreadToFinish();
                                                final IThenCallable<Object, String> handler1 = fulfillableStub();
                                                final IThenCallable<Object, String> handler2 = fulfillableStub();
                                                final IThenCallable<Object, String> handler3 = fulfillableStub();
                                                final InOrder inOrder = inOrder(handler1, handler2, handler3);

                                                when(handler1.apply(any(Object.class))).thenReturn(null);
                                                when(handler2.apply(any(Object.class))).thenReturn(null);
                                                when(handler3.apply(any(Object.class))).thenReturn(null);

                                                final AbstractPromise<Object> promise = parameters.getPromise();
                                                promise.then(new TestThenCallable<Object, String>() {
                                                    @Override
                                                    public String apply(Object o) throws Exception {
                                                        allowMainThreadToFinish();
                                                        handler1.apply(o);

                                                        promise.then(handler3);

                                                        return null;
                                                    }
                                                });
                                                promise.then(handler2);


                                                promise.then(new TestThenCallable<Object, String>() {
                                                    @Override
                                                    public String apply(Object o) throws Exception {
                                                        delayedCall(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    inOrder.verify(handler1).apply(any(Object.class));
                                                                    inOrder.verify(handler2).apply(any(Object.class));
                                                                    inOrder.verify(handler3).apply(any(Object.class));

                                                                    parameters.done();
                                                                } catch (Exception e) {
                                                                    throw new AssertionError(e);
                                                                }
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

                describe("2.2.6.2: If/when `promise` is rejected, all respective `onRejected` callbacks must execute in the order of their originating calls to `then`.", new Runnable() {
                    @Override
                    public void run() {
                        describe("multiple boring rejection handlers", new Runnable() {
                            @Override
                            public void run() {
                                testRejected(sentinel, new Testable<Object>() {
                                    @Override
                                    public void run(final TestableParameters parameters) {
                                        try {
                                            allowMainThreadToFinish();
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();

                                            final IThenCallable<Object, String> spy = rejectableStub();
                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenReturn(other);
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = parameters.getPromise();
                                            promise.then(spy, handler1);
                                            promise.then(spy, handler2);
                                            promise.then(spy, handler3);

                                            promise.then(null, new TestThenCallable<Object, String>() {
                                                @Override
                                                public String apply(Object o) {
                                                    Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                    try {
                                                        allowMainThreadToFinish();
                                                        verify(handler1, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler2, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler3, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(spy, never()).apply(any(Object.class));

                                                        parameters.done();
                                                    } catch (Exception e) {
                                                        throw new AssertionError(e);
                                                    }

                                                    return null;
                                                }
                                            });
                                        } catch (Exception e) {

                                        }
                                    }
                                });
                            }
                        });

                        describe("multiple rejection handlers, one of which throws", new Runnable() {
                            @Override
                            public void run() {
                                testRejected(sentinel, new Testable<Object>() {
                                    @Override
                                    public void run(final TestableParameters parameters) {
                                        try {
                                            allowMainThreadToFinish();
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();

                                            final IThenCallable<Object, String> spy = rejectableStub();
                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenThrow(new Exception());
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = parameters.getPromise();
                                            promise.then(spy, handler1);
                                            promise.then(spy, handler2);
                                            promise.then(spy, handler3);

                                            promise.then(null, new TestThenCallable<Object, String>() {
                                                @Override
                                                public String apply(Object o) {
                                                    Assert.assertEquals("Value should equal sentinel", sentinel, o);

                                                    try {
                                                        allowMainThreadToFinish();
                                                        verify(handler1, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler2, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(handler3, times(1)).apply(Matchers.eq(sentinel));
                                                        verify(spy, never()).apply(any(Object.class));

                                                        parameters.done();
                                                    } catch (Exception e) {
                                                        throw new AssertionError(e);
                                                    }

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
                                testRejected(dummy, new Testable<String>() {
                                    @Override
                                    public void run(final TestableParameters parameters) {
                                        AbstractPromise<String> promise = parameters.getPromise();
                                        final CallbackAggregator semiDone = new CallbackAggregator(3, parameters);

                                        allowMainThreadToFinish();
                                        promise
                                                .then(null, new TestThenCallable<Object, String>() {
                                                    @Override
                                                    public String apply(Object o) {
                                                        return sentinel;
                                                    }
                                                }).then(new TestThenCallable<String, Object>() {
                                                    @Override
                                                    public Object apply(String o) {
                                                        Assert.assertEquals("Object should equal sentinel", sentinel, o);
                                                        semiDone.done();

                                                        return null;
                                                    }
                                                });

                                        promise
                                                .then(null, new TestThenCallable<Object, String>() {
                                                    @Override
                                                    public String apply(Object o) throws Exception {
                                                        throw new Exception(sentinel2);
                                                    }
                                                })
                                                .then(null, new TestThenCallable<Object, Object>() {
                                                    @Override
                                                    public Object apply(Object o) throws Exception {
                                                        Exception e = (Exception)o; // :')
                                                        Assert.assertEquals("Object should equal sentinel2", sentinel2, e.getMessage());
                                                        semiDone.done();

                                                        return null;
                                                    }
                                                });

                                        promise
                                                .then(null, new TestThenCallable<Object, String>() {
                                                    @Override
                                                    public String apply(Object o) {
                                                        return sentinel3;
                                                    }
                                                })
                                                .then(new TestThenCallable<String, Object>() {
                                                    @Override
                                                    public Object apply(String o) {
                                                        Assert.assertEquals("Object should equal sentinel3", sentinel3, o);
                                                        semiDone.done();

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
                                testRejected(dummy, new Testable<Object>() {
                                    @Override
                                    public void run(final TestableParameters parameters) {
                                        try {
                                            allowMainThreadToFinish();
                                            final IThenCallable<Object, String> handler1 = fulfillableStub();
                                            final IThenCallable<Object, String> handler2 = fulfillableStub();
                                            final IThenCallable<Object, String> handler3 = fulfillableStub();
                                            final InOrder inOrder = inOrder(handler1, handler2, handler3);

                                            when(handler1.apply(any(Object.class))).thenReturn(other);
                                            when(handler2.apply(any(Object.class))).thenReturn(other);
                                            when(handler3.apply(any(Object.class))).thenReturn(other);

                                            AbstractPromise<Object> promise = parameters.getPromise();
                                            promise.then(null, handler1);
                                            promise.then(null, handler2);
                                            promise.then(null, handler3);

                                            promise.then(null, new TestThenCallable<Object, String>() {
                                                @Override
                                                public String apply(Object o) throws Exception {
                                                    allowMainThreadToFinish();
                                                    inOrder.verify(handler1).apply(any(Object.class));
                                                    inOrder.verify(handler2).apply(any(Object.class));
                                                    inOrder.verify(handler3).apply(any(Object.class));
                                                    parameters.done();
                                                    return null;
                                                }
                                            });
                                        } catch (Exception e) {
                                            throw new AssertionError(e);
                                        }
                                    }
                                });

                                describe("even when one handler is added inside another handler", new Runnable() {
                                    @Override
                                    public void run() {
                                        testRejected(dummy, new Testable<Object>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                try {
                                                    allowMainThreadToFinish();
                                                    final IThenCallable<Object, String> handler1 = fulfillableStub();
                                                    final IThenCallable<Object, String> handler2 = fulfillableStub();
                                                    final IThenCallable<Object, String> handler3 = fulfillableStub();
                                                    final InOrder inOrder = inOrder(handler1, handler2, handler3);

                                                    when(handler1.apply(any(Object.class))).thenReturn(null);
                                                    when(handler2.apply(any(Object.class))).thenReturn(null);
                                                    when(handler3.apply(any(Object.class))).thenReturn(null);

                                                    final AbstractPromise<Object> promise = parameters.getPromise();
                                                    promise.then(null, new TestThenCallable<Object, String>() {
                                                        @Override
                                                        public String apply(Object o) throws Exception {
                                                            allowMainThreadToFinish();
                                                            handler1.apply(o);

                                                            promise.then(null, handler3);

                                                            return null;
                                                        }
                                                    });
                                                    promise.then(null, handler2);


                                                    promise.then(null, new TestThenCallable<Object, String>() {
                                                        @Override
                                                        public String apply(Object o) throws Exception {
                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        allowMainThreadToFinish();
                                                                        inOrder.verify(handler1).apply(any(Object.class));
                                                                        inOrder.verify(handler2).apply(any(Object.class));
                                                                        inOrder.verify(handler3).apply(any(Object.class));

                                                                        parameters.done();
                                                                    } catch (Exception e) {
                                                                        throw new AssertionError(e);
                                                                    }
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

            }
        });
    }
}
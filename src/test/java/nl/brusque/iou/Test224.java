package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.*;

@RunWith(MiniMochaRunner.class)
public class Test224 extends MiniMochaDescription {
    public Test224() {
        super("2.2.4: `onFulfilled` or `onRejected` must not be called until the execution context stack contains only platform code.", new IOUMiniMochaRunnableNode() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("`then` returns before the promise becomes fulfilled or rejected", new Runnable() {
                    @Override
                    public void run() {
                        final boolean[] thenHasReturned = {false};
                        testFulfilled(dummy, new Testable<String>() {
                            @Override
                            public void run() {
                                AbstractPromise<String> promise = getPromise();

                                promise.then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) throws Exception {
                                        Assert.assertEquals(thenHasReturned[0], true);
                                        done();

                                        return null;
                                    }
                                });

                                thenHasReturned[0] = true;
                            }
                        });

                        testRejected(dummy, new Testable<String>() {
                            @Override
                            public void run() {
                                thenHasReturned[0] = false;

                                AbstractPromise<String> promise = getPromise();

                                promise.then(null, new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) throws Exception {
                                        Assert.assertEquals(thenHasReturned[0], true);
                                        done();

                                        return null;
                                    }
                                });

                                thenHasReturned[0] = true;
                            }
                        });
                    }
                });

                describe("Clean-stack execution ordering tests (fulfillment case)", new Runnable() {
                    @Override
                    public void run() {
                    specify("when `onFulfilled` is added immediately before the promise is fulfilled", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final boolean[] onFullfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                onFullfilledCalled[0] = true;

                                return null;
                            }
                        });

                        d.resolve(dummy);

                        Assert.assertFalse("onFulfilled should not have been called.", onFullfilledCalled[0]);

                        // FIXME Invoking on another thread here to fix synchronization
                        // FIXME Not in original APlus-tests
                        delayedDone(0);
                        }
                    });

                    specify("when `onFulfilled` is added immediately after the promise is fulfilled", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final boolean[] onFullfilledCalled = {false};

                        d.resolve(dummy);

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                onFullfilledCalled[0] = true;

                                return null;
                            }
                        });

                        Assert.assertFalse("onFulfilled should not have been called.", onFullfilledCalled[0]);

                        // FIXME Invoking on another thread here to fix synchronization
                        // FIXME Not in original APlus-tests
                        delayedDone(0);
                        }
                    });

                    specify("when one `onFulfilled` is added inside another `onFulfilled`", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        final AbstractPromise<String> promise = resolved();
                        final boolean[] firstOnFulfilledFinished = {false};

                        promise.then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                promise.then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertTrue("first onFulfilled should have finished", firstOnFulfilledFinished[0]);
                                        done();

                                        return null;
                                    }
                                });

                                firstOnFulfilledFinished[0] = true;

                                return null;
                            }
                        });
                        }
                    });

                    specify("when `onFulfilled` is added inside an `onRejected`", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        final IThenable<String> promise = rejected();
                        final AbstractPromise<String> promise2 = resolved();
                        final boolean[] firstOnFulfilledFinished = {false};

                        promise.then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                promise2.then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertTrue("first onRejected should have finished", firstOnFulfilledFinished[0]);
                                        done();

                                        return null;
                                    }
                                });

                                firstOnFulfilledFinished[0] = true;

                                return null;
                            }
                        });
                        }
                    });

                    specify("when the promise is fulfilled asynchronously", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        final AbstractIOU<String> d = deferred();
                        final boolean[] firstStackFinished = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertTrue("first stack should have finished", firstStackFinished[0]);
                                done();

                                return null;
                            }
                        });


                        delayedCall(new Runnable() {
                            @Override
                            public void run() {
                                d.resolve(dummy);
                                firstStackFinished[0] = true;
                            }
                        }, 0);
                        }
                    });
                    }
                });

                describe("Clean-stack execution ordering tests (rejection case)", new Runnable() {
                    @Override
                    public void run() {
                    specify("when `onRejected` is added immediately before the promise is rejected", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final boolean[] onRejectedCalled = {false};

                        d.getPromise().then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                onRejectedCalled[0] = true;

                                return null;
                            }
                        });

                        d.reject(dummy);

                        Assert.assertFalse("onRejected should not have been called.", onRejectedCalled[0]);

                        // FIXME Invoking on another thread here to fix synchronization
                        // FIXME Not in original APlus-tests
                        delayedDone(0);
                        }
                    });

                    specify("when `onRejected` is added immediately after the promise is rejected", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final boolean[] onRejectedCalled = {false};

                        d.reject(dummy);

                        d.getPromise().then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                onRejectedCalled[0] = true;

                                return null;
                            }
                        });


                        Assert.assertFalse("onRejected should not have been called.", onRejectedCalled[0]);

                        // FIXME Invoking on another thread here to fix synchronization
                        // FIXME Not in original APlus-tests
                        delayedDone(0);
                        }
                    });

                    specify("when `onRejected` is added inside an `onFulfilled`", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                            final AbstractPromise<String> promise = resolved();
                            final IThenable<String> promise2 = rejected();
                            final boolean[] firstOnFulfilledFinished = {false};

                            promise.then(new TestThenCallable<String, Void>() {
                                @Override
                                public Void apply(String o) {
                                    promise2.then(null, new TestThenCallable<String, Void>() {
                                        @Override
                                        public Void apply(String o) {
                                            Assert.assertTrue("first onFulfilled should have finished", firstOnFulfilledFinished[0]);
                                            done();

                                            return null;
                                        }
                                    });

                                    firstOnFulfilledFinished[0] = true;

                                    return null;
                                }
                            });
                        }
                    });

                    specify("when one `onRejected` is added inside another `onRejected`", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        final IThenable<String> promise = rejected();
                        final boolean[] firstOnRejectedFinished = {false};

                        promise.then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                promise.then(null, new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertTrue("first onRejected should have finished", firstOnRejectedFinished[0]);
                                        done();

                                        return null;
                                    }
                                });

                                firstOnRejectedFinished[0] = true;

                                return null;
                            }
                        });
                        }
                    });

                    specify("when the promise is rejected asynchronously", new MiniMochaSpecificationRunnable() {
                        @Override
                        public void run() {
                        final AbstractIOU<String> d = deferred();
                        final boolean[] firstStackFinished = {false};

                        d.getPromise().then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertTrue("first stack should have finished", firstStackFinished[0]);
                                done();

                                return null;
                            }
                        });


                        delayedCall(new Runnable() {
                            @Override
                            public void run() {
                                d.reject(dummy);

                                firstStackFinished[0] = true;
                            }
                        }, 0);
                        }
                    });
                    }
                });
            }
        });


    }
}
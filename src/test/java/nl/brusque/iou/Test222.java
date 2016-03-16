package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertFalse;
import static nl.brusque.iou.Util.*;

@RunWith(MiniMochaRunner.class)
public class Test222 extends MiniMochaDescription {
    public Test222() {
        super("2.2.2: If `onFulfilled` is a function,", new IOUMiniMochaRunnableNode() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.2.1: it must be called after `promise` is fulfilled, with `promise`’s fulfillment value as its first argument.", new Runnable() {
                    @Override
                    public void run() {
                        testFulfilled(dummy, new Testable<String>() {
                            @Override
                            public void run(final TestableParameters parameters) {
                                AbstractPromise<String> promise = parameters.getPromise();

                                promise.then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) throws Exception {
                                        Assert.assertEquals(o, dummy);
                                        parameters.done();

                                        return null;
                                    }
                                });
                            }
                        });
                    }
                });

                describe("2.2.2.2: it must not be called before `promise` is fulfilled", new Runnable() {
                    @Override
                    public void run() {
                        specify("fulfilled after a delay", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                final AbstractIOU<String> d = deferred();
                                final boolean[] isFulfilled = {false};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        allowMainThreadToFinish();
                                        Assert.assertTrue("isFulfilled should be true", isFulfilled[0]);

                                        done();
                                        return null;
                                    }
                                });

                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        d.resolve(dummy);
                                        isFulfilled[0] = true;
                                    }
                                }, 50);
                            }
                        });

                        specify("never fulfilled", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                final AbstractIOU<String> d = deferred();
                                final boolean[] onFulfilledCalled = {false};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        onFulfilledCalled[0] = true;
                                        done();

                                        return null;
                                    }
                                });

                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        assertFalse("onFulfilled should not have been called", onFulfilledCalled[0]);
                                        done();
                                    }
                                }, 150);

                            }
                        });
                    }
                });

                describe("2.2.2.3: it must not be called more than once.", new Runnable() {
                    @Override
                    public void run() {
                        specify("already-fulfilled", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                final int[] timesCalled = {0};

                                AbstractPromise<String> d = resolved(dummy);

                                d.then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);
                                        done();

                                        return null;
                                    }
                                });
                            }
                        });

                        specify("trying to fulfill a pending promise more than once, immediately", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                AbstractIOU<String> d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);
                                        done();

                                        return null;
                                    }
                                });

                                d.resolve(dummy);
                                d.resolve(dummy);
                            }
                        });

                        specify("trying to fulfill a pending promise more than once, delayed", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                final AbstractIOU<String> d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);
                                        done();

                                        return null;
                                    }
                                });

                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        d.resolve(dummy);
                                        d.resolve(dummy);
                                    }
                                }, 50);

                            }
                        });

                        specify("trying to fulfill a pending promise more than once, immediately then delayed", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                final AbstractIOU<String> d = deferred();
                                final int[] timesCalled = {0};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);
                                        done();

                                        return null;
                                    }
                                });

                                d.resolve(dummy);
                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        d.resolve(dummy);
                                    }
                                }, 50);
                            }
                        });

                        specify("when multiple `then` calls are made, spaced apart in time", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                final AbstractIOU<String> d = deferred();
                                final int[] timesCalled = {0, 0, 0};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        d.getPromise().then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[1]);

                                                return null;
                                            }
                                        });
                                    }
                                }, 50);


                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        d.getPromise().then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[2]);
                                                done();

                                                return null;
                                            }
                                        });
                                    }
                                }, 100);


                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        d.resolve(dummy);
                                    }
                                }, 150);
                            }
                        });

                        specify("when `then` is interleaved with fulfillment", new MiniMochaSpecificationRunnable() {
                            @Override
                            public void run() {
                                AbstractIOU<String> d = deferred();
                                final int[] timesCalled = {0, 0};

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                        return null;
                                    }
                                });

                                d.resolve(dummy);

                                d.getPromise().then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[1]);
                                        done();
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
}
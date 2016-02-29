package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.*;

@RunWith(MiniMochaRunner.class)
public class Test222 extends MiniMochaDescription {
    public Test222() {
        super("2.2.2: If `onFulfilled` is a function,", new IOUMiniMochaRunnableNode() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
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
                                Assert.assertTrue("isFulfilled should be true", isFulfilled[0]);
                                done();
                                return null;
                            }
                        });

                        delay(50);

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
                            AbstractIOU<String> d = deferred();
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
                                    Assert.assertFalse("onFulfilled should not have been called", onFulfilledCalled[0]);
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

                        resolved(dummy).then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);
                                done();

                                return null;
                            }
                        });
                        }
                    });

                    specify("trying to call a pending promise more than once, immediately", new MiniMochaSpecificationRunnable() {
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

                    specify("trying to call a pending promise more than once, delayed", new MiniMochaSpecificationRunnable() {
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

                    specify("trying to call a pending promise more than once, immediately then delayed", new MiniMochaSpecificationRunnable() {
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
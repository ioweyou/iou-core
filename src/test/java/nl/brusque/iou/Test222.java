package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.Assert;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.deferred;
import static nl.brusque.iou.Util.delay;
import static nl.brusque.iou.Util.resolved;

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
                    specify("fulfilled after a delay", new Runnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final boolean[] isFulfilled = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertTrue("isFulfilled should be true", isFulfilled[0]);

                                return null;
                            }
                        });

                        delay(50);

                        d.resolve(dummy);
                        isFulfilled[0] = true;
                        }
                    });

                    specify("never fulfilled", new Runnable() {
                        @Override
                        public void run() {
                            AbstractIOU<String> d = deferred();
                            final boolean[] onFulfilledCalled = {false};

                            d.getPromise().then(new TestThenCallable<String, Void>() {
                                @Override
                                public Void apply(String o) {
                                    onFulfilledCalled[0] = true;

                                    return null;
                                }
                            });

                            delay(150);
                            Assert.assertFalse("onFulfilled should not have been called", onFulfilledCalled[0]);
                        }
                    });
                    }
                });

                describe("2.2.2.3: it must not be called more than once.", new Runnable() {
                    @Override
                    public void run() {
                    specify("already-fulfilled", new Runnable() {
                        @Override
                        public void run() {
                        final int[] timesCalled = {0};

                        resolved(dummy).then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                return null;
                            }
                        });
                        }
                    });

                    specify("trying to call a pending promise more than once, immediately", new Runnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final int[] timesCalled = {0};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                return null;
                            }
                        });

                        d.resolve(dummy);
                        d.resolve(dummy);
                        }
                    });

                    specify("trying to call a pending promise more than once, delayed", new Runnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final int[] timesCalled = {0};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                return null;
                            }
                        });

                        delay(50);
                        d.resolve(dummy);
                        d.resolve(dummy);
                        }
                    });

                    specify("trying to call a pending promise more than once, immediately then delayed", new Runnable() {
                        @Override
                        public void run() {
                            AbstractIOU<String> d = deferred();
                            final int[] timesCalled = {0};

                            d.getPromise().then(new TestThenCallable<String, Void>() {
                                @Override
                                public Void apply(String o) {
                                    Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                    return null;
                                }
                            });

                            d.resolve(dummy);
                            delay(50);
                            d.resolve(dummy);
                        }
                    });

                    specify("when multiple `then` calls are made, spaced apart in time", new Runnable() {
                        @Override
                        public void run() {
                        AbstractIOU<String> d = deferred();
                        final int[] timesCalled = {0, 0, 0};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[0]);

                                return null;
                            }
                        });

                        delay(50);
                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[1]);

                                return null;
                            }
                        });

                        delay(50);
                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertEquals("timesCalled should be 0", 1, ++timesCalled[2]);

                                return null;
                            }
                        });

                        delay(50);
                        d.resolve(dummy);
                        }
                    });

                    specify("when `then` is interleaved with fulfillment", new Runnable() {
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
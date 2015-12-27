package nl.brusque.iou;

import org.junit.Assert;
import org.junit.Test;

import static nl.brusque.iou.TestUtils.*;

public class Test224 extends TestBase {
    @Test
    public void test224onFulfilledOronRejectedMustNotBeCalledUntilTheExecutionContextStackContainsOnlyPlatformCode() {
        describe("2.2.4: `onFulfilled` or `onRejected` must not be called until the execution context stack contains only platform code.", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("Clean-stack execution ordering tests (fulfillment case)", new Runnable() {
                    @Override
                    public void run() {
                    specify("when `onFulfilled` is added immediately before the promise is fulfilled", new Runnable() {
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
                        }
                    });

                    specify("when `onFulfilled` is added immediately after the promise is fulfilled", new Runnable() {
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
                        }
                    });

                    specify("when one `onFulfilled` is added inside another `onFulfilled`", new Runnable() {
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
                                        return null;
                                    }
                                });

                                firstOnFulfilledFinished[0] = true;

                                return null;
                            }
                        });

                        delay(50);
                        }
                    });

                    specify("when `onFulfilled` is added inside an `onRejected`", new Runnable() {
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
                                        return null;
                                    }
                                });

                                firstOnFulfilledFinished[0] = true;

                                return null;
                            }
                        });

                        delay(50);
                        }
                    });

                    specify("when the promise is fulfilled asynchronously", new Runnable() {
                        @Override
                        public void run() {
                        final AbstractIOU<String> d = deferred();
                        final boolean[] firstStackFinished = {false};

                        d.getPromise().then(new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertTrue("first stack should have finished", firstStackFinished[0]);

                                return null;
                            }
                        });

                        new Thread() {
                            @Override
                            public void run() {
                                d.resolve(dummy);
                                firstStackFinished[0] = true;
                            }
                        }.start();


                        delay(50);
                        }
                    });
                    }
                });

                describe("Clean-stack execution ordering tests (rejection case)", new Runnable() {
                    @Override
                    public void run() {
                    specify("when `onRejected` is added immediately before the promise is rejected", new Runnable() {
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
                        }
                    });

                    specify("when `onRejected` is added immediately after the promise is rejected", new Runnable() {
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
                        }
                    });

                    specify("when one `onRejected` is added inside another `onRejected`", new Runnable() {
                        @Override
                        public void run() {
                        final IThenable<String> promise = rejected();
                        final boolean[] firstOnRejectedFinished = {false};

                        promise.then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                promise.then(new TestThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) {
                                        Assert.assertTrue("first onRejected should have finished", firstOnRejectedFinished[0]);

                                        return null;
                                    }
                                });

                                firstOnRejectedFinished[0] = true;

                                return null;
                            }
                        });

                        delay(50);
                        }
                    });

                    specify("when `onRejected` is added inside an `onFulfilled`", new Runnable() {
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

                                        return null;
                                    }
                                });

                                firstOnFulfilledFinished[0] = true;

                                return null;
                            }
                        });

                        delay(50);
                        }
                    });

                    specify("when the promise is rejected asynchronously", new Runnable() {
                        @Override
                        public void run() {
                        final AbstractIOU<String> d = deferred();
                        final boolean[] firstStackFinished = {false};

                        d.getPromise().then(null, new TestThenCallable<String, Void>() {
                            @Override
                            public Void apply(String o) {
                                Assert.assertTrue("first stack should have finished", firstStackFinished[0]);

                                return null;
                            }
                        });


                        new Thread() {
                            @Override
                            public void run() {
                                d.reject(dummy);

                                firstStackFinished[0] = true;
                            }
                        }.start();

                        delay(50);
                        }
                    });
                    }
                });
            }
        });


    }
}
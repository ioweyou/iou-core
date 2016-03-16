package nl.brusque.iou;


import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.deferred;
import static nl.brusque.iou.Util.rejected;
import static nl.brusque.iou.Util.resolved;
import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test232 extends MiniMochaDescription {

    public Test232() {
        super("2.3.2: If `x` is a promise, adopt its state", new IOUMiniMochaRunnableNode() {
            @Override
            public void run() {
                final String sentinel = "SENTINEL";

                describe("2.3.2.1: If `x` is pending, `promise` must remain pending until `x` is fulfilled or rejected.", new Runnable() {
                    @Override
                    public void run() {
                        testPromiseResolution(new PromiseFactory<String>() {
                            @Override
                            AbstractPromise<String> create() {
                                AbstractIOU<String> d = deferred();
                                allowMainThreadToFinish();
                                return d.getPromise();
                            }
                        }, new Testable<String>() {
                            @Override
                            public void run(final TestableParameters parameters) {
                                final boolean[] wasFulfilled = {false};
                                final boolean[] wasRejected = {false};

                                allowMainThreadToFinish();
                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                    @Override
                                    public Void apply(String o) throws Exception {
                                        wasFulfilled[0] = true;

                                        return null;
                                    }
                                }, new IThenCallable<Object, Void>() {
                                    @Override
                                    public Void apply(Object o) throws Exception {
                                        wasRejected[0] = true;

                                        return null;
                                    }
                                });

                                delayedCall(new Runnable() {
                                    @Override
                                    public void run() {
                                        allowMainThreadToFinish();
                                        assertEquals("Expected wasFulfilled to be false", false, wasFulfilled[0]);
                                        assertEquals("Expected wasRejected to be false", false, wasRejected[0]);

                                        parameters.done();
                                    }
                                }, 100);
                            }
                        });
                    }
                });

                describe("2.3.2.2: If/when `x` is fulfilled, fulfill `promise` with the same value.", new Runnable() {
                            @Override
                            public void run() {
                                describe("`x` is already-fulfilled", new Runnable() {
                                    @Override
                                    public void run() {

                                        testPromiseResolution(new PromiseFactory<String>() {
                                            @Override
                                            AbstractPromise<String> create() {
                                                return resolved(sentinel);
                                            }
                                        }, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                AbstractPromise<String> testPromise = parameters.getPromise();

                                                testPromise.then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals(sentinel, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("`x` is eventually-fulfilled", new Runnable() {
                                    @Override
                                    public void run() {
                                        testPromiseResolution(new PromiseFactory<String>() {
                                            @Override
                                            AbstractPromise<String> create() {
                                                final AbstractIOU<String> d = deferred();

                                                delayedCall(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        d.resolve(sentinel);
                                                    }
                                                }, 50);

                                                return d.getPromise();
                                            }
                                        }, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals(sentinel, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }

                );

                describe("2.3.2.3: If/when `x` is rejected, reject `promise` with the same reason.", new Runnable() {
                            @Override
                            public void run() {
                                describe("`x` is already-rejected", new Runnable() {
                                    @Override
                                    public void run() {

                                        testPromiseResolution(new PromiseFactory<String>() {
                                            @Override
                                            AbstractPromise<String> create() {
                                                return rejected(sentinel);
                                            }
                                        }, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals(sentinel, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("`x` is eventually-rejected", new Runnable() {
                                    @Override
                                    public void run() {
                                        testPromiseResolution(new PromiseFactory<String>() {
                                            @Override
                                            AbstractPromise<String> create() {
                                                final AbstractIOU<String> d = deferred();

                                                delayedCall(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        d.reject(sentinel);
                                                    }
                                                }, 50);

                                                return d.getPromise();
                                            }
                                        }, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals(sentinel, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }

                );
            }
        });
    }
}
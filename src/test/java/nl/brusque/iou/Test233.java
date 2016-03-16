package nl.brusque.iou;


import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunnableNode;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static nl.brusque.iou.Util.deferred;
import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test233 extends MiniMochaDescription {

    public Test233() {
        super("2.3.3: Otherwise, if `x` is an object or function,", new IOUMiniMochaRunnableNode() {
            final String DUMMY = "DUMMY";
            final String OTHER = "OTHER";

            <TFulfill> void testCallingResolvePromise(final AnythingFactory<TFulfill> yFactory, String stringRepresentation, final Testable<TFulfill> test) {
                describe("`y` is " + stringRepresentation, new MiniMochaRunnableNode() {

                    @Override
                    public void run() {
                        describe("`then` calls `resolvePromise` synchronously", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                ThenableFactory<TFulfill> xFactory = new ThenableFactory<TFulfill>() {

                                    @Override
                                    IThenable<TFulfill> create() {
                                        return new IThenable<TFulfill>() {

                                            @Override
                                            public IThenable then(IThenCallable onFulfilled) {
                                                return then(onFulfilled, null);
                                            }

                                            @Override
                                            public IThenable then(IThenCallable onFulfilled, IThenCallable onRejected) {
                                                try {
                                                    allowMainThreadToFinish();
                                                    onFulfilled.apply(yFactory.create());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                return null;
                                            }
                                        };
                                    }
                                };

                                testPromiseResolution(xFactory, test);
                            }
                        });

                        describe("`then` calls `resolvePromise` asynchronously", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                ThenableFactory<TFulfill> xFactory = new ThenableFactory<TFulfill>() {

                                    @Override
                                    IThenable<TFulfill> create() {
                                        return new IThenable<TFulfill>() {

                                            @Override
                                            public IThenable then(IThenCallable onFulfilled) {
                                                return then(onFulfilled, null);
                                            }

                                            @Override
                                            public IThenable then(final IThenCallable onFulfilled, IThenCallable onRejected) {
                                                delayedCall(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            allowMainThreadToFinish();
                                                            onFulfilled.apply(yFactory.create());
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, 0);

                                                return null;
                                            }
                                        };
                                    }
                                };

                                testPromiseResolution(xFactory, test);
                            }
                        });
                    }
                });
            }

            <TFulfill, TReason> void testCallingRejectPromise(final TReason r, String stringRepresentation, final Testable<TFulfill> test) {
                describe("`r` is " + stringRepresentation, new MiniMochaRunnableNode() {
                    @Override
                    public void run() {
                        describe("`then` calls `rejectPromise` synchronously", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                ThenableFactory<TFulfill> xFactory = new ThenableFactory<TFulfill>() {
                                    @Override
                                    IThenable<TFulfill> create() {
                                        return new IThenable<TFulfill>() {
                                            @Override
                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled) {
                                                return then(onFulfilled, null);
                                            }

                                            @Override
                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
                                                try {
                                                    allowMainThreadToFinish();
                                                    onRejected.apply(r);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                return null;
                                            }
                                        };
                                    }
                                };

                                testPromiseResolution(xFactory, test);
                            }
                        });

                        describe("`then` calls `rejectPromise` asynchronously", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                ThenableFactory<TFulfill> xFactory = new ThenableFactory<TFulfill>() {
                                    @Override
                                    IThenable<TFulfill> create() {
                                        return new IThenable<TFulfill>() {
                                            @Override
                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled) {
                                                return then(onFulfilled, null);
                                            }

                                            @Override
                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<TFulfill, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {

                                                delayedCall(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            allowMainThreadToFinish();
                                                            onRejected.apply(r);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, 0);

                                                return null;
                                            }
                                        };
                                    }
                                };

                                testPromiseResolution(xFactory, test);
                            }
                        });
                    }
                });
            }

            <TFulfill> void testCallingResolvePromiseFulfillsWith(final AnythingFactory<TFulfill> yFactory, final String stringRepresentation, final TFulfill fulfillmentValue) {
                testCallingResolvePromise(yFactory, stringRepresentation, new Testable<TFulfill>() {
                    @Override
                    public void run(final TestableParameters parameters) {
                        parameters.getPromise().then(new IThenCallable<TFulfill, Void>() {
                            @Override
                            public Void apply(TFulfill value) throws Exception {
                                assertEquals("value should equal fulfillmentValue", fulfillmentValue, value);
                                parameters.done();

                                return null;
                            }
                        });
                    }
                });
            }

            void testCallingResolvePromiseRejectsWith(final AnythingFactory<String> yFactory, final String stringRepresentation, final String rejectionReason) {
                testCallingResolvePromise(yFactory, stringRepresentation, new Testable<String>() {
                    @Override
                        public void run(final TestableParameters parameters) {
                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object reason) throws Exception {
                                // FIXME So much unchecked typecasting
                                String stringReason = "?";

                                try {
                                    stringReason = (String)reason;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                assertEquals("reason should equal rejectionReason", rejectionReason, stringReason);
                                parameters.done();

                                return null;
                            }
                        });
                    }
                });
            }

            <TFulfill, TAnything> void testCallingRejectPromiseRejectsWith(final TAnything reason, String stringRepresentation) {
                testCallingRejectPromise(reason, stringRepresentation, new Testable<TFulfill>() {
                    @Override
                    public void run(final TestableParameters parameters) {
                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object rejectionReason) throws Exception {
                                assertEquals("reason should equal rejectionReason", reason, rejectionReason);
                                parameters.done();

                                return null;
                            }
                        });
                    }
                });
            }

            @Override
            public void run() {
                describe("2.3.3.1: Let `then` be `x.then`", new Runnable() {
                    @Override
                    public void run() {
                        describe("`x` is an object with null prototype", new Runnable() {
                            @Override
                            public void run() {
                                final int[] numberOfTimesThenWasRetrieved = {-1};

                                beforeEach(new Runnable() {
                                    @Override
                                    public void run() {
                                        numberOfTimesThenWasRetrieved[0] = 0;
                                    }
                                });

                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                    @Override
                                    IThenable<String> create() {
                                        return new IThenable<String>() {
                                            @Override
                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                return then(onFulfilled, null);
                                            }

                                            @Override
                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
                                                ++numberOfTimesThenWasRetrieved[0];

                                                try {
                                                    onFulfilled.apply("");
                                                } catch (Exception e) {

                                                }

                                                return null;
                                            }
                                        };
                                    }
                                };

                                testPromiseResolution(xFactory, new Testable<String>() {
                                    @Override
                                    public void run(final TestableParameters parameters) {
                                        parameters.getPromise().then(new IThenCallable<String, Object>() {
                                            @Override
                                            public Object apply(String o) throws Exception {
                                                allowMainThreadToFinish();
                                                assertEquals("Then should be retrieved one time", 1, numberOfTimesThenWasRetrieved[0]);
                                                parameters.done();

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        handleNonSensicalTest("`x` is an object with normal Object.prototype");
                        handleNonSensicalTest("`x` is a function");
                    }
                });

                describe("2.3.3.2: If retrieving the property `x.then` results in a thrown exception `e`, reject `promise` with `e` as the reason.", new MiniMochaRunnableNode() {
                    @Override
                    public void run() {
                        handleNonSensicalTest("Not implemented.");
                    }
                });


                describe("2.3.3.3: If `then` is a function, call it with `x` as `this`, first argument `resolvePromise`, and second argument `rejectPromise`", new MiniMochaRunnableNode() {
                    @Override
                    public void run() {
                        handleNonSensicalTest("Calls with `x` as `this` and two function arguments");
                        handleNonSensicalTest("Uses the original value of `then`");

                        describe("2.3.3.3.1: If/when `resolvePromise` is called with value `y`, run `[[Resolve]](promise, y)`", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                describe("`y` is not a thenable", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        testCallingResolvePromiseFulfillsWith(new AnythingFactory<String>() {
                                            @Override
                                            String create() {
                                                return null;
                                            }
                                        }, "`null`", null);

                                        testCallingResolvePromiseFulfillsWith(new AnythingFactory<String>() {
                                            @Override
                                            String create() {
                                                return DUMMY;
                                            }
                                        }, "an object", DUMMY);
                                    }
                                });

                                final HashMap<String, Thenables.ThenablesFactory> fulfilleds = new Thenables().getFulfilled();
                                final HashMap<String, Thenables.ThenablesFactory> fulfilleds2 = new Thenables().getFulfilled();
                                final HashMap<String, Thenables.ThenablesFactory> rejecteds = new Thenables().getRejected();
                                final HashMap<String, Thenables.ThenablesFactory> rejecteds2 = new Thenables().getRejected();
                                describe("`y` is a thenable", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        for (final String stringRepresentation : fulfilleds.keySet()) {
                                            ThenableFactory yFactory = new ThenableFactory<String>() {

                                                @Override
                                                IThenable<String> create() {
                                                    return fulfilleds.get(stringRepresentation).create(DUMMY);
                                                }
                                            };

                                            testCallingResolvePromiseFulfillsWith(yFactory, stringRepresentation, DUMMY);
                                        }

                                        for (final String stringRepresentation : rejecteds.keySet()) {
                                            ThenableFactory yFactory = new ThenableFactory<String>() {

                                                @Override
                                                IThenable<String> create() {
                                                    return rejecteds.get(stringRepresentation).create(DUMMY);
                                                }
                                            };

                                            testCallingResolvePromiseRejectsWith(yFactory, stringRepresentation, DUMMY);
                                        }
                                    }
                                });

                                describe("`y` is a thenable for a thenable", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {

                                        for (final String outerStringRepresentation : fulfilleds.keySet()) {
                                            final Thenables.ThenablesFactory outerThenableFactory = fulfilleds.get(outerStringRepresentation);

                                            for (final String innerStringRepresentation : fulfilleds2.keySet()) {
                                                final Thenables.ThenablesFactory<String> innerThenableFactory = fulfilleds2.get(innerStringRepresentation);

                                                String stringRepresentation = outerStringRepresentation + " for " + innerStringRepresentation;

                                                ThenableFactory yFactory = new ThenableFactory<String>() {

                                                    @Override
                                                    IThenable create() {
                                                        IThenable<String> inner = innerThenableFactory.create(DUMMY);

                                                        return outerThenableFactory.create(inner);
                                                    }
                                                };

                                                testCallingResolvePromiseFulfillsWith(yFactory, stringRepresentation, DUMMY);
                                            }

                                            for (final String innerStringRepresentation : rejecteds.keySet()) {
                                                final Thenables.ThenablesFactory innerThenableFactory = rejecteds.get(innerStringRepresentation);

                                                String stringRepresentation = outerStringRepresentation + " for " + innerStringRepresentation;

                                                ThenableFactory yFactory = new ThenableFactory<String>() {

                                                    @Override
                                                    IThenable<String> create() {
                                                        IThenable inner = innerThenableFactory.create(DUMMY);

                                                        return outerThenableFactory.create(inner);
                                                    }
                                                };

                                                testCallingResolvePromiseRejectsWith(yFactory, stringRepresentation, DUMMY);
                                            }
                                        }
                                    }
                                });
                            }
                        });

                        describe("2.3.3.3.2: If/when `rejectPromise` is called with reason `r`, reject `promise` with `r`", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                HashMap<String, Reasons.ReasonsFactory> reasons = new Reasons().getReasons();

                                for (final String stringRepresentation : reasons.keySet()) {
                                    testCallingRejectPromiseRejectsWith(reasons.get(stringRepresentation).create(), stringRepresentation);
                                }
                            }
                        });

                        describe("2.3.3.3.3: If both `resolvePromise` and `rejectPromise` are called, or multiple calls to the same argument are made, the first call takes precedence, and any further calls are ignored.", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                describe("calling `resolvePromise` then `rejectPromise`, both synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled, IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onFulfilled.apply(DUMMY);
                                                            onRejected.apply(OTHER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` synchronously then `rejectPromise` asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onFulfilled.apply(DUMMY);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        onRejected.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` then `rejectPromise`, both asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        delayedCall(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    onFulfilled.apply(DUMMY);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, 0);

                                                        delayedCall(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    allowMainThreadToFinish();
                                                                    onRejected.apply(OTHER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, 0);


                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` with an asynchronously-fulfilled promise, then calling `rejectPromise`, both synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                final AbstractIOU<Object> d = deferred();
                                                delayedCall(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        d.resolve(DUMMY);
                                                    }
                                                }, 50);

                                                return new IThenable() {
                                                    @Override
                                                    public IThenable then(IThenCallable onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                        try {
                                                            onFulfilled.apply(d.getPromise());
                                                            allowMainThreadToFinish();
                                                            onRejected.apply(OTHER);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });



                                describe("calling `resolvePromise` with an asynchronously-rejected promise, then calling `rejectPromise`, both synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                final AbstractIOU<Object> d = deferred();
                                                delayedCall(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        d.reject(DUMMY);
                                                    }
                                                }, 50);

                                                return new IThenable() {
                                                    @Override
                                                    public IThenable then(IThenCallable onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                        try {
                                                            onFulfilled.apply(d.getPromise());
                                                            onRejected.apply(OTHER);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `rejectPromise` then `resolvePromise`, both synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onRejected.apply(DUMMY);
                                                            onFulfilled.apply(OTHER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        // FIXME So much unchecked typecasting
                                                        String stringReason = "?";

                                                        try {
                                                            stringReason = (String) reason;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `rejectPromise` synchronously then `resolvePromise` asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onRejected.apply(DUMMY);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        onFulfilled.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        // FIXME So much unchecked typecasting
                                                        String stringReason = "?";

                                                        try {
                                                            stringReason = (String)reason;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                                describe("calling `rejectPromise` then `resolvePromise`, both asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        onRejected.apply(DUMMY);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        allowMainThreadToFinish();
                                                                        onFulfilled.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        // FIXME So much unchecked typecasting
                                                        String stringReason = "?";

                                                        try {
                                                            stringReason = (String)reason;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` twice synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onFulfilled.apply(DUMMY);
                                                            onFulfilled.apply(OTHER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` twice, first synchronously then asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onFulfilled.apply(DUMMY);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        allowMainThreadToFinish();
                                                                        onFulfilled.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` twice, both times asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        onFulfilled.apply(DUMMY);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        allowMainThreadToFinish();
                                                                        onFulfilled.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` with an asynchronously-fulfilled promise, then calling it again, both times synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable() {
                                                    @Override
                                                    public IThenable then(IThenCallable onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                        try {
                                                            final AbstractIOU<Object> d = deferred();

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    d.resolve(DUMMY);
                                                                }
                                                            }, 50);

                                                            onFulfilled.apply(d.getPromise());
                                                            onFulfilled.apply(OTHER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                    @Override
                                                    public Void apply(String value) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, value);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `resolvePromise` with an asynchronously-rejected promise, then calling it again, both times synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable() {
                                                    @Override
                                                    public IThenable then(IThenCallable onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                        try {
                                                            final AbstractIOU<Object> d = deferred();

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    d.reject(DUMMY);
                                                                }
                                                            }, 50);

                                                            onFulfilled.apply(d.getPromise());
                                                            onFulfilled.apply(OTHER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        assertEquals("Value should equal DUMMY", DUMMY, reason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `rejectPromise` twice synchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onRejected.apply(DUMMY);
                                                            onRejected.apply(OTHER);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        // FIXME So much unchecked typecasting
                                                        String stringReason = "?";

                                                        try {
                                                            stringReason = (String)reason;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });


                                describe("calling `rejectPromise` twice, first synchronously then asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            onRejected.apply(DUMMY);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        onRejected.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        // FIXME So much unchecked typecasting
                                                        String stringReason = "?";

                                                        try {
                                                            stringReason = (String)reason;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("calling `rejectPromise` twice, both times asynchronously", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                            @Override
                                            IThenable<String> create() {
                                                return new IThenable<String>() {
                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                        return then(onFulfilled, null);
                                                    }

                                                    @Override
                                                    public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                        try {
                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        onRejected.apply(DUMMY);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);

                                                            delayedCall(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    allowMainThreadToFinish();
                                                                    try {
                                                                        onRejected.apply(OTHER);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }, 0);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        return null;
                                                    }
                                                };
                                            }
                                        };

                                        testPromiseResolution(xFactory, new Testable<String>() {
                                            @Override
                                            public void run(final TestableParameters parameters) {
                                                parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                    @Override
                                                    public Void apply(Object reason) throws Exception {
                                                        allowMainThreadToFinish();
                                                        // FIXME So much unchecked typecasting
                                                        String stringReason = "?";

                                                        try {
                                                            stringReason = (String)reason;
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                        parameters.done();

                                                        return null;
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                                describe("saving and abusing `resolvePromise` and `rejectPromise`", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        specify("Not implemented", new MiniMochaSpecificationRunnable() {
                                            @Override
                                            public void run() {
                                                delayedDone(0);
                                                // FIXME Implement
                                                //throw new Error("Not implemented");
                                            }
                                        });
                                    }
                                });
                            }
                        });

                        describe("2.3.3.3.4: If calling `then` throws an exception `e`,", new MiniMochaRunnableNode() {
                            @Override
                            public void run() {
                                describe("2.3.3.3.4.1: If `resolvePromise` or `rejectPromise` have been called, ignore it.", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        describe("`resolvePromise` was called with a non-thenable", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable<String> create() {
                                                        return new IThenable<String>() {
                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                                try {
                                                                    onFulfilled.apply(DUMMY);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                allowMainThreadToFinish();
                                                                throw new Error(OTHER);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                            @Override
                                                            public Void apply(String value) throws Exception {
                                                                allowMainThreadToFinish();
                                                                assertEquals("Value should equal DUMMY", DUMMY, value);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`resolvePromise` was called with an asynchronously-fulfilled promise", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable create() {
                                                        return new IThenable() {
                                                            @Override
                                                            public IThenable then(IThenCallable onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                                final AbstractIOU<Object> d = deferred();
                                                                delayedCall(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        d.resolve(DUMMY);
                                                                    }
                                                                }, 50);

                                                                try {
                                                                    onFulfilled.apply(d.getPromise());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                throw new Error(OTHER);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                            @Override
                                                            public Void apply(String value) throws Exception {
                                                                allowMainThreadToFinish();
                                                                assertEquals("Value should equal DUMMY", DUMMY, value);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`resolvePromise` was called with an asynchronously-rejected promise", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable create() {
                                                        return new IThenable() {
                                                            @Override
                                                            public IThenable then(IThenCallable onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                                final AbstractIOU<Object> d = deferred();
                                                                delayedCall(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        d.reject(DUMMY);
                                                                    }
                                                                }, 50);

                                                                try {
                                                                    onFulfilled.apply(d.getPromise());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                throw new Error(OTHER);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                            @Override
                                                            public Void apply(Object reason) throws Exception {
                                                                allowMainThreadToFinish();
                                                                assertEquals("Value should equal DUMMY", DUMMY, reason);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`rejectPromise` was called", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable create() {
                                                        return new IThenable() {
                                                            @Override
                                                            public IThenable then(IThenCallable onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                                try {
                                                                    onRejected.apply(DUMMY);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                throw new Error(OTHER);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                            @Override
                                                            public Void apply(Object reason) throws Exception {
                                                                allowMainThreadToFinish();
                                                                // FIXME So much unchecked typecasting
                                                                String stringReason = "?";

                                                                try {
                                                                    stringReason = (String)reason;
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`resolvePromise` then `rejectPromise` were called", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable create() {
                                                        return new IThenable() {
                                                            @Override
                                                            public IThenable then(IThenCallable onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                                try {
                                                                    onFulfilled.apply(DUMMY);
                                                                    onRejected.apply(OTHER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                throw new Error(OTHER);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(new IThenCallable<String, Void>() {
                                                            @Override
                                                            public Void apply(String value) throws Exception {
                                                                allowMainThreadToFinish();
                                                                assertEquals("Value should equal DUMMY", DUMMY, value);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`rejectPromise` then `resolvePromise` were called", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable create() {
                                                        return new IThenable() {
                                                            @Override
                                                            public IThenable then(IThenCallable onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                                                                try {
                                                                    onRejected.apply(DUMMY);
                                                                    onFulfilled.apply(OTHER);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                throw new Error(OTHER);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                            @Override
                                                            public Void apply(Object reason) throws Exception {
                                                                allowMainThreadToFinish();
                                                                // FIXME So much unchecked typecasting
                                                                String stringReason = "?";

                                                                try {
                                                                    stringReason = (String)reason;
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });
                                    }
                                });

                                describe("2.3.3.3.4.2: Otherwise, reject `promise` with `e` as the reason.", new MiniMochaRunnableNode() {
                                    @Override
                                    public void run() {
                                        describe("straightforward case", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable<String> create() {
                                                        return new IThenable<String>() {
                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                                throw new Error(DUMMY);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                            @Override
                                                            public Void apply(Object reason) throws Exception {
                                                                allowMainThreadToFinish();
                                                                // FIXME So much unchecked typecasting
                                                                String stringReason = "?";

                                                                try {
                                                                    stringReason = ((Error)reason).getMessage();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`resolvePromise` is called asynchronously before the `throw`", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable<String> create() {
                                                        return new IThenable<String>() {
                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                                delayedCall(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            allowMainThreadToFinish();
                                                                            onFulfilled.apply(OTHER);
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }, 0);
                                                                throw new Error(DUMMY);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                            @Override
                                                            public Void apply(Object reason) throws Exception {
                                                                allowMainThreadToFinish();
                                                                // FIXME So much unchecked typecasting
                                                                String stringReason = "?";

                                                                try {
                                                                    stringReason = ((Error)reason).getMessage();
                                                                } catch (Exception e) {

                                                                }

                                                                assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                                parameters.done();

                                                                return null;
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                        describe("`rejectPromise` is called asynchronously before the `throw`", new MiniMochaRunnableNode() {
                                            @Override
                                            public void run() {
                                                ThenableFactory<String> xFactory = new ThenableFactory<String>() {
                                                    @Override
                                                    IThenable<String> create() {
                                                        return new IThenable<String>() {
                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(IThenCallable<String, TAnythingOutput> onFulfilled) {
                                                                return then(onFulfilled, null);
                                                            }

                                                            @Override
                                                            public <TAnythingOutput> IThenable<TAnythingOutput> then(final IThenCallable<String, TAnythingOutput> onFulfilled, final IThenCallable<Object, TAnythingOutput> onRejected) {
                                                                delayedCall(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            allowMainThreadToFinish();
                                                                            onRejected.apply(OTHER);
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }, 0);
                                                                throw new Error(DUMMY);
                                                            }
                                                        };
                                                    }
                                                };

                                                testPromiseResolution(xFactory, new Testable<String>() {
                                                    @Override
                                                    public void run(final TestableParameters parameters) {
                                                        parameters.getPromise().then(null, new IThenCallable<Object, Void>() {
                                                            @Override
                                                            public Void apply(Object reason) throws Exception {
                                                                allowMainThreadToFinish();
                                                                // FIXME So much unchecked typecasting
                                                                String stringReason = "?";

                                                                try {
                                                                    stringReason = ((Error)reason).getMessage();
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                assertEquals("Value should equal DUMMY", DUMMY, stringReason);
                                                                parameters.done();

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

                describe("2.3.3.4: If `then` is not a function, fulfill promise with `x`", new MiniMochaRunnableNode() {
                    @Override
                    public void run() {
                        handleNonSensicalTest("Not implemented");
                    }
                });
            }

        });
    }
}
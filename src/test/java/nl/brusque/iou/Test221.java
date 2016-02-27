package nl.brusque.iou;

import org.junit.runner.RunWith;

@RunWith(MiniMochaRunner.class)
public class Test221 extends MiniMochaDescription {
    public Test221() {
        describe("2.2.1: Both `onFulfilled` and `onRejected` are optional arguments.", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.1.1: If `onFulfilled` is not a function, it must be ignored.", new Runnable() {
                    @Override
                    public void run() {
                        describe("applied to a directly-rejected promise", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.reject(dummy).then(o, new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                return null;
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });

                        describe("applied to a promise rejected and then chained off of", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.reject(dummy).then(new TestThenCallable<String, String>() {
                                            @Override
                                            public String apply(String dummyString) {
                                                return dummyString;
                                            }
                                        })
                                        .then(o, new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String dummyString) {
                                                return null;
                                            }
                                        });
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });
                    }
                });

                describe("2.2.1.2: If `onRejected` is not a function, it must be ignored.", new Runnable() {
                    @Override
                    public void run() {
                        describe("applied to a directly-fulfilled promise", new Runnable() {
                            private <TAnything> void testNonFunction(final TAnything o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.resolve(dummy).then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                return null;
                                            }
                                        }, o);
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });

                        describe("applied to a promise fulfilled and then chained off of", new Runnable() {
                            private void testNonFunction(final Object o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU<String> d = deferred();

                                        d.resolve(dummy).then(new TestThenCallable<String, Void>() {
                                            @Override
                                            public Void apply(String o) {
                                                return null;
                                            }
                                        }, o);
                                    }
                                });
                            }

                            @Override
                            public void run() {
                                testNonFunction(null, "`null`");
                                testNonFunction(false, "`false`");
                                testNonFunction(5, "`5`");
                                testNonFunction(new Object(), "an object");
                            }
                        });
                    }
                });
            }
        });
    }
}
package nl.brusque.iou;

import org.junit.Test;

public class Test221 extends TestBase {
    @Test
    public void test2131WhenRejectedAPromiseMustNotTransitionToAnyOtherState() {
        describe("2.2.1: Both `onFulfilled` and `onRejected` are optional arguments.", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.1.1: If `onFulfilled` is not a function, it must be ignored.", new Runnable() {
                    @Override
                    public void run() {
                        describe("applied to a directly-rejected promise", new Runnable() {
                            private void testNonFunction(final Object o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU d = deferred();

                                        d.reject(dummy).then(o, new TestThenCallable() {
                                            @Override
                                            public Object call(Object o) {
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
                            private void testNonFunction(final Object o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU d = deferred();

                                        d.reject(dummy)
                                                .then(new TestThenCallable() {
                                                    @Override
                                                    public Object call(Object o) {
                                                        return null;
                                                    }
                                                })
                                                .then(o, new TestThenCallable() {
                                                    @Override
                                                    public Object call(Object o) {
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
            }
        });

        describe("2.2.1.2: If `onRejected` is not a function, it must be ignored.", new Runnable() {
            final String dummy = "DUMMY";

            @Override
            public void run() {
                describe("2.2.1.1: If `onFulfilled` is not a function, it must be ignored.", new Runnable() {
                    @Override
                    public void run() {
                        describe("applied to a directly-fulfilled promise", new Runnable() {
                            private void testNonFunction(final Object o, String stringRepresentation) {
                                specify(String.format("`onFulfilled` is %s", stringRepresentation), new Runnable() {
                                    @Override
                                    public void run() {
                                        AbstractIOU d = deferred();

                                        d.resolve(dummy)
                                                .then(new TestThenCallable() {
                                                    @Override
                                                    public Object call(Object o) {
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
                                        AbstractIOU d = deferred();

                                        d.resolve(dummy)
                                        .then(new TestThenCallable() {
                                            @Override
                                            public Object call(Object o) {
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
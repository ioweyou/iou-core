package nl.brusque.iou;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class Test227 extends TestBase {
    @Test
    public void test227ThenMustReturnAPromise() {
        describe("2.2.7: `then` must return a promise: `promise2 = promise1.then(onFulfilled, onRejected)`", new Runnable() {
            final String dummy     = "DUMMY";
            final String other     = "other";
            final String sentinel  = "sentinel";
            final String sentinel2 = "sentinel2";
            final String sentinel3 = "sentinel3";

            final HashMap<String, Throwable> reasons = new HashMap<String, Throwable>() {{
                put("`null`", null);
                put("an error", new Error());
            }};



            @Override
            public void run() {
                specify("is a promise", new Runnable() {
                    @Override
                    public void run() {
                        AbstractPromise promise1 = deferred().getPromise();
                        Object promise2 = promise1.then();

                        Assert.assertFalse("AndroidPromise should not be null", promise2 == null);
                    }
                });

                describe("2.2.7.2: If either `onFulfilled` or `onRejected` throws an exception `e`, `promise2` must be rejected with `e` as the reason.", new Runnable() {
                    private void testReason(final Throwable expectedReason, String stringRepresentation) {
                        describe(String.format("The reason is %s", stringRepresentation), new Runnable() {

                            @Override
                            public void run() {
                                testFulfilled(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        final AbstractPromise promise1 = getPromise();

                                        IThenable promise2 = promise1.then(new TestFulfillable() {
                                            @Override
                                            public Object fulfill(Object o) throws Exception {
                                                throw new Exception(expectedReason);
                                            }
                                        });

                                        promise2.then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) throws Exception {
                                                Throwable e = o!=null ? ((Exception)o).getCause() : null;

                                                Assert.assertEquals("Incorrect reason", expectedReason, e);

                                                return null;
                                            }
                                        });
                                    }
                                });

                                testRejected(dummy, new Testable() {
                                    @Override
                                    public void run() {
                                        AbstractPromise promise1 = getPromise();

                                        IThenable promise2 = promise1.then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) throws Exception {
                                                throw new Exception(expectedReason);
                                            }
                                        });

                                        promise2.then(null, new TestRejectable() {
                                            @Override
                                            public Object reject(Object o) throws Exception {
                                                Throwable e = o!=null ? ((Exception)o).getCause() : null;

                                                Assert.assertEquals("Incorrect reason", expectedReason, e);

                                                return null;
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void run() {
                        for (String stringRepresentation : reasons.keySet()) {
                            testReason(reasons.get(stringRepresentation), stringRepresentation);
                        }
                    }
                });

            }
        });

        delay(5000);

    }
}
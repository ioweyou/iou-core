package nl.brusque.iou;


import nl.brusque.iou.errors.TypeError;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Test231 extends TestBase {
    @Test
    public void test231IfPromiseAndXReferToTheSameObjectRejectPromiseWithATypeError() {
        describe("2.3.1: If `promise` and `x` refer to the same object, fireRejectables `promise` with a `TypeError' as the reason.", new Runnable() {
            final String dummy     = "DUMMY";

            @Override
            public void run() {
                specify("via return from a fulfilled promise", new Runnable() {
                    @Override
                    public void run() {
                        final List<IThenable> promises = new ArrayList<>();
                        promises.add(resolved(dummy).then(new TestThenCallable() {
                            @Override
                            public Object apply(Object o) throws Exception {
                                return promises.get(0);
                            }
                        }));

                        promises.get(0).then(null, new TestThenCallable() {
                            @Override
                            public Object apply(Object o) throws Exception {
                                Assert.assertTrue("Object should be TypeError", o instanceof TypeError);

                                return null;
                            }
                        });
                    }
                });

                specify("via return from a rejected promise", new Runnable() {
                    @Override
                    public void run() {
                        final List<IThenable> promises = new ArrayList<>();

                        promises.add(rejected(dummy).then(null, new TestThenCallable() {
                            @Override
                            public Object apply(Object o) throws Exception {
                                return promises.get(0);
                            }
                        }));

                        promises.get(0).then(null, new TestThenCallable() {
                            @Override
                            public Object apply(Object o) throws Exception {
                                Assert.assertTrue("Object should be TypeError", o instanceof TypeError);

                                return null;
                            }
                        });
                    }
                });
            }
        });

        delay(500);
    }
}
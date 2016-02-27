package nl.brusque.iou;


import nl.brusque.iou.errors.TypeError;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(MiniMochaRunner.class)
public class Test231 extends MiniMochaDescription {
    public Test231() {
        describe("2.3.1: If `promise` and `x` refer to the same object, fireRejectables `promise` with a `TypeError' as the reason.", new Runnable() {
            final String dummy     = "DUMMY";

            @Override
            public void run() {
                specify("via return from a fulfilled promise", new Runnable() {
                    @Override
                    public void run() {
                        final List<IThenable<Object>> promises = new ArrayList<>();

                        IThenable<String> r = resolved(dummy);

                        promises.add(r.then(new TestThenCallable<String, Object>() {
                            @Override
                            public Object apply(String o) throws Exception {
                                return promises.get(0);
                            }
                        }));

                        promises.get(0).then(null, new TestThenCallable<Object, Object>() {
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
                        final List<IThenable<Object>> promises = new ArrayList<>();

                        IThenable<String> r = rejected(dummy);

                        promises.add(r.then(null, new TestThenCallable<String, Object>() {
                            @Override
                            public Object apply(String o) throws Exception {
                                return promises.get(0);
                            }
                        }));

                        promises.get(0).then(null, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) throws Exception {
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
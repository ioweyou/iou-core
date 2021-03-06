package nl.brusque.iou;


import nl.brusque.iou.errors.TypeError;
import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import nl.brusque.iou.minimocha.MiniMochaSpecificationRunnable;
import org.junit.Assert;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static nl.brusque.iou.Util.*;

@RunWith(MiniMochaRunner.class)
public class Test231 extends MiniMochaDescription {
    public Test231() {
        super("2.3.1: If `promise` and `x` refer to the same object, reject `promise` with a `TypeError` as the reason.", new IOUMiniMochaRunnableNode() {
            final String dummy     = "DUMMY";

            @Override
            public void run() {
                specify("via return from a fulfilled promise", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final List<IThenable<Object>> promises = new ArrayList<>();

                        IThenable<String> r = resolved(dummy);

                        promises.add(r.then(new TestThenCallable<String, Object>() {
                            @Override
                            public Object apply(String o) throws Exception {
                                allowMainThreadToFinish();
                                return promises.get(0);
                            }
                        }));

                        allowMainThreadToFinish();
                        promises.get(0).then(null, new TestThenCallable<Object, Object>() {

                            @Override
                            public Object apply(Object o) throws Exception {
                                Assert.assertTrue("Object should be TypeError", o instanceof TypeError);
                                done();

                                return null;
                            }
                        });
                    }
                });

                specify("via return from a rejected promise", new MiniMochaSpecificationRunnable() {
                    @Override
                    public void run() {
                        final List<IThenable<Object>> promises = new ArrayList<>();

                        IThenable<String> r = rejected(dummy);

                        promises.add(r.then(null, new TestThenCallable<Object, Object>() {
                            @Override
                            public Object apply(Object o) throws Exception {
                                allowMainThreadToFinish();
                                return promises.get(0);
                            }
                        }));

                        allowMainThreadToFinish();
                        promises.get(0).then(null, new TestThenCallable<Object, Void>() {
                            @Override
                            public Void apply(Object o) throws Exception {
                                Assert.assertTrue("Object should be TypeError", o instanceof TypeError);

                                done();

                                return null;
                            }
                        });
                    }
                });
            }
        });
    }
}
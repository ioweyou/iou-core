package nl.brusque.iou;

import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.runner.RunWith;

import static nl.brusque.iou.Util.deferred;
import static nl.brusque.iou.Util.delay;
import static org.junit.Assert.assertEquals;

@RunWith(MiniMochaRunner.class)
public class Test212 extends MiniMochaDescription {
    public Test212() {
        super("2.1.2.1: When fulfilled, a promise: must not transition to any other state", new IOUMiniMochaRunnableNode() {

            @Override
            public void run() {
                final String dummy = "DUMMY";

                specify("trying to call then immediately reject", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        d.resolve(dummy);
                        d.reject(dummy);
                        delay(100);
                    }
                });

                specify("trying to call then fireRejectables, delayed", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                assertEquals("onRejected should not have been called", false, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        delay(50);
                        d.resolve(dummy);
                        d.reject(dummy);

                        delay(100);
                    }
                });

                specify("trying to call immediately then fireRejectables delayed", new Runnable() {
                    @Override
                    public void run() {
                        AbstractIOU<String> d = deferred();

                        final boolean[] onFulfilledCalled = {false};

                        d.getPromise().then(new TestThenCallable<String, String>() {

                            @Override
                            public String apply(String o) {
                                onFulfilledCalled[0] = true;

                                return o;
                            }
                        }, new TestThenCallable<String, String>() {
                            @Override
                            public String apply(String o) {
                                assertEquals("onRejected should not have been called", true, onFulfilledCalled[0]);

                                return o;
                            }
                        });

                        d.resolve(dummy);
                        delay(50);
                        d.reject(dummy);

                        delay(100);
                    }
                });
            }
        });
    }
}
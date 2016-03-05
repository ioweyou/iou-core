package nl.brusque.iou;


import nl.brusque.iou.minimocha.MiniMochaDescription;
import nl.brusque.iou.minimocha.MiniMochaRunner;
import org.junit.runner.RunWith;

@RunWith(MiniMochaRunner.class)
public class Test233 extends MiniMochaDescription {

    public Test233() {
        super("2.3.3: Otherwise, if `x` is an object or function,", new IOUMiniMochaRunnableNode() {
            @Override
            public void run() {
                final String sentinel = "SENTINEL";


                abstract class AnonymousThenableMethod  {
                    abstract void run(Runnable someParameter);
                }

                abstract class AnonymousThenable {
                    abstract AnonymousThenableMethod then(Runnable onFulfilled);
                }


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

                                AnythingFactory<AnonymousThenable> xFactory = new AnythingFactory<AnonymousThenable>() {
                                    @Override
                                    AnonymousThenable create() {
                                        return new AnonymousThenable() {
                                            @Override
                                            public AnonymousThenableMethod then(Runnable onFulfilled) {
                                                ++numberOfTimesThenWasRetrieved[0];

                                                return new AnonymousThenableMethod() {
                                                    @Override
                                                    public void run(Runnable onFulfilled) {

                                                    }
                                                };
                                            }
                                        };
                                    }
                                };

                                /*testPromiseResolution(xFactory, new Testable<Object>() {
                                    @Override
                                    public void run() {
                                        getPromise().then(new IThenCallable<Object, Object>() {
                                            @Override
                                            public Object apply(Object o) throws Exception {
                                                assertEquals("Then should be retrieved one time", 1, numberOfTimesThenWasRetrieved[0]);
                                                done();

                                                return null;
                                            }
                                        });
                                    }
                                });*/
                            }
                        });
                    }
                });
             }
        });
    }
}
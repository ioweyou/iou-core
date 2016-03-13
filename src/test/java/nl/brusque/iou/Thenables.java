package nl.brusque.iou;

import java.util.HashMap;

public class Thenables {
    abstract class ThenablesFactory<TFulfill> {
        private TFulfill _value;

        protected TFulfill getValue() {
            return _value;
        }

        protected abstract IThenable<TFulfill> getThenable();

        public IThenable<TFulfill> create(TFulfill value) {
            _value = value;

            return getThenable();
        }
    }

    private HashMap<String, ThenablesFactory> _fulfilled = new HashMap<String, ThenablesFactory>() {
        {
            put("a synchronously-fulfilled custom thenable", new ThenablesFactory() {
                @Override
                protected IThenable getThenable() {
                    return new IThenable() {
                        @Override
                        public IThenable then(IThenCallable onFulfilled) {
                            return then(onFulfilled, null);
                        }

                        @Override
                        public IThenable then(IThenCallable onFulfilled, IThenCallable onRejected) {
                            try {
                                onFulfilled.apply(getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return null;
                        }
                    };
                }
            });

            put("an asynchronously-fulfilled custom thenable", new ThenablesFactory() {
                @Override
                protected IThenable getThenable() {
                    return new IThenable() {
                        @Override
                        public IThenable then(IThenCallable onFulfilled) {
                            return then(onFulfilled, null);
                        }

                        @Override
                        public IThenable then(final IThenCallable onFulfilled, IThenCallable onRejected) {
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        onFulfilled.apply(getValue());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.run();


                            return null;
                        }
                    };
                }
            });
        }
    };

    private HashMap<String, ThenablesFactory> _rejected = new HashMap<String, ThenablesFactory>() {
        {
            put("a synchronously-rejected custom thenable", new ThenablesFactory() {
                @Override
                protected IThenable getThenable() {
                    return new IThenable() {
                        @Override
                        public IThenable then(IThenCallable onFulfilled) {
                            return then(onFulfilled, null);
                        }

                        @Override
                        public IThenable then(IThenCallable onFulfilled, final IThenCallable onRejected) {
                            try {
                                onRejected.apply(getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return null;
                        }
                    };
                }
            });

            put("an asynchronously-rejected custom thenable", new ThenablesFactory() {
                @Override
                protected IThenable getThenable() {
                    return new IThenable() {
                        @Override
                        public IThenable then(IThenCallable onFulfilled) {
                            return then(onFulfilled, null);
                        }

                        @Override
                        public IThenable then(final IThenCallable onFulfilled, final IThenCallable onRejected) {
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        onRejected.apply(getValue());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.run();


                            return null;
                        }
                    };
                }
            });
        }
    };

    public HashMap<String, ThenablesFactory> getFulfilled() {
        return _fulfilled;
    }

    public HashMap<String, ThenablesFactory> getRejected() {
        return _rejected;
    }
}
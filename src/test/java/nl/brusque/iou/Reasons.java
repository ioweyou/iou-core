package nl.brusque.iou;

import java.util.Date;
import java.util.HashMap;

import static nl.brusque.iou.Util.rejected;
import static nl.brusque.iou.Util.resolved;

public class Reasons {
    abstract class ReasonsFactory<TReason> {
        protected abstract TReason getReason();

        public RejectReason<TReason> create() {
            return new RejectReason<>(getReason());
        }
    }

    private HashMap<String, ReasonsFactory> _reasons = new HashMap<String, ReasonsFactory>() {
        {
            put("`null`", new ReasonsFactory<Object>() {
                @Override
                protected Object getReason() {
                    return null;
                }
            });
            put("`false`", new ReasonsFactory<Boolean>() {
                @Override
                protected Boolean getReason() {
                    return false;
                }
            });
            put("`0`", new ReasonsFactory<Integer>() {
                @Override
                protected Integer getReason() {
                    return 0;
                }
            });
            put("an error", new ReasonsFactory<Error>() {
                @Override
                protected Error getReason() {
                    return new Error();
                }
            });
            put("a date", new ReasonsFactory<Date>() {
                @Override
                protected Date getReason() {
                    return new Date();
                }
            });
            put("an object", new ReasonsFactory<Object>() {
                @Override
                protected Object getReason() {
                    return new Object();
                }
            });
            put("an always-pending thenable", new ReasonsFactory<IThenable>() {
                @Override
                protected IThenable getReason() {
                    return new IThenable() {
                        @Override
                        public IThenable then(IThenCallable onFulfilled) {
                            return then(onFulfilled, null);
                        }

                        @Override
                        public IThenable then(IThenCallable onFulfilled, IThenCallable onRejected) {
                            return null;
                        }
                    };
                }
            });
            put("a fulfilled promise", new ReasonsFactory<AbstractPromise>() {
                @Override
                protected AbstractPromise getReason() {
                    return resolved("DUMMY");
                }
            });
            put("a rejected promise", new ReasonsFactory<AbstractPromise>() {
                @Override
                protected AbstractPromise getReason() {
                    return rejected("DUMMY");
                }
            });
        }
    };

    public HashMap<String, ReasonsFactory> getReasons() {
        return _reasons;
    }
}
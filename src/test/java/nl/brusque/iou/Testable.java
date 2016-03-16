package nl.brusque.iou;

abstract class Testable<TInput> {

    public abstract void run(TestableParameters parameters);

    class CallbackAggregator {
        private int _soFar = 0;
        private final int _times;
        private final TestableParameters _parameters;

        CallbackAggregator(int times, TestableParameters parameters) {
            _times      = times;
            _parameters = parameters;
        }

        public synchronized void done() {
            _soFar++;

            if (_soFar == _times) {
                _parameters.done();
            }
        }
    }
    final void allowMainThreadToFinish() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
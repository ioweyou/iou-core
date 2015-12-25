package nl.brusque.iou;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class EventDispatcher {
    private final ArrayDeque<IEvent> _eventQueue                                         = new ArrayDeque<>();
    private final HashMap<Class<? extends IEvent>, List<IEventListener<? extends IEvent>>> _eventListeners = new HashMap<>();

    final Thread _looper = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true) {
                processNextEvent();
            }
        }
    });

    public EventDispatcher() {
        _looper.start();
    }

    public void addListener(Class<? extends IEvent> eventType, IEventListener<? extends IEvent> listener) {
        if (!_eventListeners.containsKey(eventType)) {
            _eventListeners.put(eventType, new ArrayList<IEventListener<? extends IEvent>>());
        }

       _eventListeners.get(eventType).add(listener);
    }

    public synchronized void queue(IEvent event) {
        _eventQueue.add(event);

        synchronized (_looper) {
            _looper.notify();
        }
    }

    private synchronized  IEvent dequeue() {
        if (_eventQueue.isEmpty()) {
            return null;
        }

        return _eventQueue.remove();
    }

    private void process(IEvent event) {
        Class<? extends IEvent> clazz = event.getClass();
        if (!_eventListeners.containsKey(clazz)) {
            return;
        }

        for (IEventListener listener : _eventListeners.get(clazz)) {
            listener.process(event); // FIXME unchecked apply
        }
    }

    private void processNextEvent() {
        IEvent event = dequeue();
        if (event != null) {
            process(event);
            return;
        }

        synchronized (_looper) {
            try {
                _looper.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

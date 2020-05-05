package ru.hse.paramfunc.event;

import java.util.*;

public class EventMediator {

    private static final Map<EventType, List<EventListener>> listenerMap;

    static {
        listenerMap = new HashMap<>();
    }

    public static void addListener(EventType eventType, EventListener listener) {
        List<EventListener> listenersValue = listenerMap.get(eventType);
        if (listenersValue == null) {
            List<EventListener> listeners = new ArrayList<>();
            listeners.add(listener);
            listenerMap.put(eventType, listeners);
        } else if (!listenersValue.contains(listener)) {
            listenersValue.add(listener);
            listenerMap.put(eventType, listenersValue);
        }
    }

    public static void notifyAllListeners(EventType eventType, Object... args) {
        List<EventListener> listeners = listenerMap.get(eventType);
        if (listeners != null) {
            //Запускаем в обратном порядке, чтобы UI обработал событие последним//
            List<EventListener> listenersCopy = new ArrayList<>(listeners);
            Collections.reverse(listenersCopy);
            listenersCopy.forEach(l -> l.receive(eventType, args));
        }
    }

}

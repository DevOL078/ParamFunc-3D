package ru.hse.paramfunc.listener;

import java.util.ArrayList;
import java.util.List;

public interface Notifier {

    List<Listener> listeners = new ArrayList<>();

    default void addListener(Listener listener) {
        listeners.add(listener);
    }

    default void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    default void notifyListeners() {
        listeners.forEach(Listener::receive);
    }

}

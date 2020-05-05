package ru.hse.paramfunc.event;

public interface EventListener {

    void receive(EventType eventType, Object... args);

}

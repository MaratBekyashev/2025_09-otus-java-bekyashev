package ru.otus.kafka;

import ru.otus.event.Event;

public interface EventProducer {

    void sendEvent(Event event);
}

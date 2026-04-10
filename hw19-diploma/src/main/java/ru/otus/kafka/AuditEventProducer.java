package ru.otus.kafka;

public interface AuditEventProducer {

    void sendEvent(AuditEvent event);
}

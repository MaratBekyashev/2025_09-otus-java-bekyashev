package ru.otus.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaAuditEventProducer implements AuditEventProducer {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    private static final String TOPIC = "audit-topic";

    @Override
    public void sendEvent(AuditEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
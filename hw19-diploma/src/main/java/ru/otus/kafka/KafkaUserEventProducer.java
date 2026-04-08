package ru.otus.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.event.Event;
import ru.otus.event.task.TaskAssigneeChangedEvent;
import ru.otus.event.task.TaskCommentAddedEvent;
import ru.otus.event.task.TaskCreatedEvent;
import ru.otus.event.task.TaskEvent;
import ru.otus.event.task.TaskStatusChangedEvent;
import ru.otus.event.task.TaskStatusClosedEvent;
import ru.otus.event.user.UserCreatedEvent;
import ru.otus.event.user.UserEvent;
import ru.otus.event.user.UserPasswordChangedEvent;
import ru.otus.event.user.UserRolesChangedEvent;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaUserEventProducer implements EventProducer{

    private final Map<Class<? extends UserEvent>, String> topicMap = Map.of(
            UserCreatedEvent.class,         "user-topic.new-user-created",
            UserPasswordChangedEvent.class,    "user-topic.task-closed",
            UserRolesChangedEvent.class,   "user-topic.task-status-changed"
    );

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendEvent(Event event) {
        if(!(event instanceof TaskEvent)) {
            throw new UnsupportedOperationException("unsupported task type event");
        }
        var eventTopic = topicMap.get(event.getClass());
        if (eventTopic == null){
            throw new UnsupportedOperationException("unsupported task event");
        }
        kafkaTemplate.send(eventTopic, event);
    }
}
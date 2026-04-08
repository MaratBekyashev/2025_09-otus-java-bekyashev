package ru.otus.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.event.Event;
import ru.otus.event.project.ProjectEvent;
import ru.otus.event.task.*;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaTaskEventProducer implements EventProducer{

    private final Map<Class<? extends TaskEvent>, String> topicMap = Map.of(
            TaskCreatedEvent.class,         "task-topic.new-task-created",
            TaskStatusClosedEvent.class,    "task-topic.task-closed",
            TaskStatusChangedEvent.class,   "task-topic.task-status-changed",
            TaskAssigneeChangedEvent.class, "task-topic.task-assignee-changed",
            TaskCommentAddedEvent.class,    "task-topic.new-comment-added"
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
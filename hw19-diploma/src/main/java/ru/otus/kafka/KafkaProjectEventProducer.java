package ru.otus.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.otus.event.Event;
import ru.otus.event.project.ProjectCreatedEvent;
import ru.otus.event.project.ProjectDeletedEvent;
import ru.otus.event.project.ProjectEvent;
import ru.otus.event.project.ProjectMemberAddedEvent;
import ru.otus.event.project.ProjectMemberDeletedEvent;
import ru.otus.event.project.ProjectMemberRoleChangedEvent;
import ru.otus.event.task.TaskAssigneeChangedEvent;
import ru.otus.event.task.TaskCommentAddedEvent;
import ru.otus.event.task.TaskCreatedEvent;
import ru.otus.event.task.TaskEvent;
import ru.otus.event.task.TaskStatusChangedEvent;
import ru.otus.event.task.TaskStatusClosedEvent;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaProjectEventProducer implements EventProducer{

    private final Map<Class<? extends ProjectEvent>, String> topicMap = Map.of(
            ProjectCreatedEvent.class,         "project-topic.project-created",
            ProjectDeletedEvent.class,    "project-topic.project-deleted",
            ProjectMemberAddedEvent.class,   "project-topic.member-added",
            ProjectMemberRoleChangedEvent.class, "project-topic.member-role-changed",
            ProjectMemberDeletedEvent.class,    "project-topic.member-deleted"
    );

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendEvent(Event event) {
        if(!(event instanceof ProjectEvent)) {
            throw new UnsupportedOperationException("unsupported task type event");
        }
        var eventTopic = topicMap.get(event.getClass());
        if (eventTopic == null){
            throw new UnsupportedOperationException("unsupported task event");
        }
        kafkaTemplate.send(eventTopic, event);
    }

}
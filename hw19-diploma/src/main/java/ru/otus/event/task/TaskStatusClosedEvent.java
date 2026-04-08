package ru.otus.event.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.model.task.TaskStatusEnum;

@Getter
@Setter
@NoArgsConstructor
public class TaskStatusClosedEvent implements TaskEvent {

    private Long taskId;

    private TaskStatusEnum newStatus;
}
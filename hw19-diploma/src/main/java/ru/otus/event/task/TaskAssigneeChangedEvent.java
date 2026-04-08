package ru.otus.event.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskAssigneeChangedEvent implements TaskEvent{

    private Long taskId;

    private Long newAssigneeId;

}